package cFramework.communications.p2p;

import java.net.BindException;

import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;
import cFramework.communications.fiels.Address;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.messages.IgniteEntityListMessage;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.util.DefaultValues;

public class IgniterProtocols implements Protocol{

	P2PCommunications myCommunications;
	private NodeAddress serviceAddress = new NodeAddress(0,"127.0.0.1", DefaultValues.SERVICE_PORT);
	
	
	/**
	 * Creates a P2P communication socket
	 * @param log
	 */
	public IgniterProtocols(NodeLog log){
		try {
			myCommunications = new P2PCommunications(this, new NodeConf(), log);
		} catch (BindException e) {
			System.out.println("Port already in use");
			e.printStackTrace();
		}
		
	}
	
	
	public void receive(Address address, byte[] message){
	}
	
	
	public void sendServiceUpRequest(){
		
	}
	
	public boolean isServiceUP(){
		return myCommunications.send(serviceAddress, new HelloMessage().toByteArray());
	}
	
	
	public void sendList(String[] list, NodeConf nc){
		String prefix = "";
		String path = System.getProperty("java.class.path");
		StringBuilder builder = new StringBuilder();
		builder.append(path + ",");
		for(String s : list) {
			builder.append(prefix);
			prefix = ",";
		    builder.append(s);
		}
		
		String newList = builder.toString();
		myCommunications.send(serviceAddress, new IgniteEntityListMessage(nc, newList).toByteArray() );
	}
	
	
	public void stop(){
		myCommunications.stop();
	}
	
	

	@Override
	public P2PCommunications getCommunications() {
		return this.myCommunications;
	}

	@Override
	public NodeAddress getNodeAddress() {
		return new NodeAddress(0, myCommunications.getAddress());
	}
	
}