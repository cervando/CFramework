package cFramework.nodes.activities;

import java.lang.reflect.InvocationTargetException;

import cFramework.communications.MessageMetadata;
import cFramework.log.NodeLog;

public class ActivityMessageManager {

	Class<? extends Activity> myClass;
	ActivityConfiguration nc;
	private ActivityWrapper pc;
	private Activity p;
	private NodeLog log;
	
	public ActivityMessageManager(Class<? extends Activity> myClass, ActivityWrapper pc, ActivityConfiguration nc, NodeLog log){
		this.myClass = myClass;
		this.nc = nc;
		this.pc = pc;
		this.log = log;
		
		if (nc.getType() == ActivityConfiguration.TYPE_SINGLETON){
			try {
				p = myClass.getConstructor().newInstance();
				p.setLog(log);
				p.setCore(pc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void receive(final long nodeID, final MessageMetadata m, final byte[] data){
		//Determinate if there is enough resources, create a helper if there is in high load 
		//Consider if a helper has already requested
		if (nc.getType() == ActivityConfiguration.TYPE_PARALLEL){
			new Thread(){
				public void run(){
					try {
						p = myClass.getConstructor().newInstance();
						p.setLog(log);
						p.setCore(pc);
						p.receive(nodeID, m, data);
					} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
						e.printStackTrace();
					}
				}
			}.start();
			
		}else if (nc.getType() == ActivityConfiguration.TYPE_SINGLETON){
			p.receive(nodeID, m, data);
		}
	}
}
