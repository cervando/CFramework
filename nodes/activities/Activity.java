package kmiddle2.nodes.activities;

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
		core.send(nodeID, data);
	}
	
	public abstract void receive(int nodeID, byte[] data);
	
	public Class<?> getNamer(){
		return namer;
	}
	
	public void setLog(NodeLog log){
		this.log = log;
	}
}
