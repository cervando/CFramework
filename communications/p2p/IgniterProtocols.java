package kmiddle2.communications.p2p;

import java.net.BindException;

import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.Protocol;
import kmiddle2.communications.fiels.Address;
import kmiddle2.communications.messages.HelloMessage;
import kmiddle2.communications.messages.IgniteEntityListMessage;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.util.DefaultValues;

public class IgniterProtocols implements Protocol{

	P2PCommunications myCommunications;
	private NodeAddress serviceAddress = new NodeAddress(0,"127.0.0.1", DefaultValues.SERVICE_PORT);
	private boolean isServiceUP = false;
	
	public IgniterProtocols(NodeLog log){
		try {
			myCommunications = new P2PCommunications(this, new NodeConf(), log);
		} catch (BindException e) {
			System.out.println("Port already in use");
			e.printStackTrace();
		}
		
	}
	
	public void receive(Address address, byte[] message){
		Message m = Message.getMessage(message);
		if ( m.getOperationCode() == OperationCodeConstants.HANDSHAKE ){
			isServiceUP = true;
		}
	}
	
	
	public void sendServiceUpRequest(){
		myCommunications.send(serviceAddress, new HelloMessage().toByteArray());
	}
	
	public boolean isServiceUP(){
		return isServiceUP;
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