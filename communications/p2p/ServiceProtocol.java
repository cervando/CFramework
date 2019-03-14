package cFramework.communications.p2p;

import java.net.BindException;

import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;
import cFramework.communications.fiels.Address;
import cFramework.communications.messages.HandShakeMessage;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.messages.IgniteEntityListMessage;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.communications.multicast.Multicast;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.nodes.service.Service;
import cFramework.util.DefaultValues;

public class ServiceProtocol implements Protocol {

	private P2PCommunications myCommunications;
	private Multicast multicastConection;
	private Service service;
	private boolean isEntityUp = false;
	private NodeLog log;
	
	public ServiceProtocol(Service service, NodeLog logger){
		this.service = service;
		this.log = logger;
		NodeConf nc = new NodeConf();
		nc.setEntityID((byte)-1);
		try {
			myCommunications = new P2PCommunications(this,DefaultValues.SERVICE_PORT, log);
			multicastConection = new Multicast(this, nc, log);													//Connecto to multicast group
			multicastConection.setUp();
			log.message("Running on " + DefaultValues.SERVICE_PORT);
		} catch (BindException e) {
			log.message("Port already in use");
		}
	}
	
	@Override
	public void receive(Address address, byte[] message) {
		Message m =  Message.getMessage(message);
		if ( m.getOperationCode() == OperationCodeConstants.IGNITE_ENTITY_LIST ){
					final NodeConf nc = ((IgniteEntityListMessage)m).getNodeConfiguration();
					if ( !nc.isLocal() ){
									final String list = ((IgniteEntityListMessage)m).getList();
									myCommunications.send(
											new NodeAddress(0, 
													new Address("127.0.0.1", DefaultValues.ENTITY_PORT + nc.getEntityID()
											)), 
											new HelloMessage().toByteArray()
									);									
									new Thread(){
										public void run(){
											try {
												Thread.sleep(500);
												if ( !isEntityUp )
													service.createEntity(nc, list);
												else
													log.message("Entity " + nc.getEntityID()  +" Already UP");
												isEntityUp = false;
											} catch (InterruptedException e) {
												e.printStackTrace();
											}
										}
									}.start();
					}
		}
		
		else if ( m.getOperationCode() == OperationCodeConstants.HELLO ){
					myCommunications.send(new NodeAddress(0, address), new HandShakeMessage().toByteArray());
		}
		
		else if ( m.getOperationCode() == OperationCodeConstants.HANDSHAKE ){
					isEntityUp = true;
		}
		
	}

	@Override
	public P2PCommunications getCommunications() {
		return myCommunications;
	}

	@Override
	public NodeAddress getNodeAddress() {
		return new NodeAddress(0, myCommunications.getAddress());
	}

}