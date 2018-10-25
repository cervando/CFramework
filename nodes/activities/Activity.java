package kmiddle2.nodes.activities;

import kmiddle2.communications.MessageMetadata;
import kmiddle2.log.NodeLog;

public abstract class Activity{

	private ActivityWrapper core;
	protected int ID;
	protected Class<?> namer;
	protected NodeLog log;
	
	public void init(){}
	
	public int getID(){
		return ID;
	}
	
	public void setCore(ActivityWrapper core){
		this.core = core;
	}
	
	protected void send(int nodeID, byte[] data){
		if ( currentMetadata != null )
			core.send(nodeID, currentMetadata, data);
		else
			core.send(nodeID, new MessageMetadata(0), data);
	}
	
	public abstract void receive(int nodeID,  byte[] data);
	
	protected MessageMetadata currentMetadata = null;
	public void receive(int nodeID, MessageMetadata m, byte[] data) {
		currentMetadata = m;
		receive(nodeID,data);
	}
	
	public Class<?> getNamer(){
		return namer;
	}
	
	public void setLog(NodeLog log){
		this.log = log;
	}
}
