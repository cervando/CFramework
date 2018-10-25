package kmiddle2.nodes.activities;

import kmiddle2.communications.MessageMetadata;
import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.Protocol;
import kmiddle2.communications.p2p.ActivityProtocols;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.Node;

public class ActivityWrapper extends Node {

	ActivityProtocols protocols;
	ActivityMessageManager manager;
	Class<? extends Activity> activity;
	NodeLog log;
	
	public ActivityWrapper(Activity p, NodeAddress father, ActivityConfiguration nc){
		activity = p.getClass();
		log = new NodeLog(p.getID(), p.getNamer(), nc.isDebug());
		p.setLog(log);
		
		protocols = new ActivityProtocols(p.getID(), father, this, nc, log);
		manager = new ActivityMessageManager(activity, this, nc,log);
	}
	
	public void send(int nodeID, MessageMetadata m, byte[] data){
		protocols.sendData(nodeID, m, data);
	}
	
	
	public void send(int nodeID, byte[] data){
		protocols.sendData(nodeID, new MessageMetadata(0), data);
	}
 
	public void receive(int nodeID, MessageMetadata m, byte[] data){
		manager.receive(nodeID, m, data);
	}

	@Override
	public Protocol getProtocol() {
		return protocols;
	}
	
}
