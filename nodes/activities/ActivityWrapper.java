package cFramework.nodes.activities;

import cFramework.communications.MessageMetadata;
import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;
import cFramework.communications.p2p.ActivityProtocols;
import cFramework.log.NodeLog;
import cFramework.nodes.Node;
import cFramework.nodes.microglia.Microglia;

public class ActivityWrapper extends Node {

	ActivityProtocols protocols;
	ActivityMessageManager manager;
	Class<? extends Activity> activity;
	NodeLog log;
	Microglia microglia = new Microglia();
	
	public ActivityWrapper(Activity p, NodeAddress father, ActivityConfiguration nc){
		activity = p.getClass();
		log = new NodeLog(p.getID(), p.getNamer(), nc.isDebug());
		p.setLog(log);
		
		protocols = new ActivityProtocols(p.getID(), father, this, nc, log);
		manager = new ActivityMessageManager(activity, this, nc,log);
	}
	
	public boolean send(long nodeID, MessageMetadata m, byte[] data){
		microglia.send();
		return protocols.sendData(nodeID, m, data);
	}
	
	public boolean send(long nodeID, byte[] data){
		microglia.send();
		return protocols.sendData(nodeID, new MessageMetadata(0), data);
	}
 
	public void receive(long nodeID, MessageMetadata m, byte[] data){
		microglia.receive(data);
		manager.receive(nodeID, m, data);
	}

	@Override
	public Protocol getProtocol() {
		return protocols;
	}
	
	public float getWellness() {
		return microglia.getWellness();
	}
	
	public void setWellness(float w) {
		microglia.setWellness(w);
	}
	
}
