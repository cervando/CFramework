package kmiddle2.communications.p2p;

import java.net.BindException;

import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.Protocol;
import kmiddle2.communications.fiels.Address;
import kmiddle2.communications.messages.HandShakeMessage;
import kmiddle2.communications.messages.HelloMessage;
import kmiddle2.communications.messages.IgniteEntityListMessage;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.communications.multicast.Multicast;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.service.Service;
import kmiddle2.util.DefaultValues;

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
					if ( nc.isLocal() ){
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