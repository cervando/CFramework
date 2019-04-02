package cFramework.nodes.processes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cFramework.communications.MessageMetadata;
import cFramework.log.NodeLog;

public abstract class Process{

	private ProcessWrapper core;
	protected long ID;
	protected Class<?> namer;
	protected NodeLog log;
	private float wellnessUmbral = 1f;
	
	
	public void init(){}
	
	public long getID(){
		return ID;
	}
	
	public void setCore(ProcessWrapper core){
		this.core = core;
	}
	
	protected boolean send(long nodeID, byte[] data){
		if ( currentMetadata != null )
			return core.send(nodeID, currentMetadata, data);
		else
			return core.send(nodeID, new MessageMetadata(0), data);
	}
	
	public abstract void receive(long nodeID,  byte[] data);
	
	
	
	protected MessageMetadata currentMetadata = null;
	public void receive(long nodeID, MessageMetadata m, byte[] data) {
		currentMetadata = m;
		//receive(nodeID,data);
		if ( getWellness() >= wellnessUmbral )
			receive(nodeID, data);
		else {
			
			try {
				Method recovery = this.getClass().getMethod("recovery_receive", int.class, byte[].class);
				recovery.invoke(this, nodeID, data);
				
			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// TODO Auto-generated catch block
				receive(nodeID, data);
			}
			
		}
		
		
		
		
	}
	
	
	public void recovery_receive(long nodeID, MessageMetadata m, byte[] data) {
		currentMetadata = m;
		receive(nodeID,data);
	}
	
	
	
	
	
	public Class<?> getNamer(){
		return namer;
	}
	
	public void setLog(NodeLog log){
		this.log = log;
	}
	
	public float getWellness() {
		return core.getWellness();
	}
	
	public void setWellness(float w) {
		core.setWellness(w);
	}
	
	public void setWellnessUmbral(float w) {
		this.wellnessUmbral = w;
	}
}