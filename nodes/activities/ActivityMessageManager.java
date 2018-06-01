package kmiddle2.nodes.activities;

import kmiddle2.log.NodeLog;

public class ActivityMessageManager {

	Class<? extends Activity> myClass;
	ActConf nc;
	private ActivityWrapper pc;
	private Activity p;
	private NodeLog log;
	
	public ActivityMessageManager(Class<? extends Activity> myClass, ActivityWrapper pc, ActConf nc, NodeLog log){
		this.myClass = myClass;
		this.nc = nc;
		this.pc = pc;
		this.log = log;
	}
	
	public void receive(final int nodeID, final byte[] data){
		//Determinate if there is enough resources, create a helper if there is in high load 
		//Consider if a helper has already requested
		if (nc.getType() == ActConf.TYPE_PARALLEL){
			new Thread(){
				public void run(){
					try {
						p = myClass.newInstance();
						p.setLog(log);
						p.setCore(pc);
						p.receive(nodeID, data);
					} catch (InstantiationException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}.start();
			
		}else if (nc.getType() == ActConf.TYPE_SINGLETON){
			if ( p == null ){
				try {
					p = myClass.newInstance();
					p.setLog(log);
					p.setCore(pc);
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			p.receive(nodeID, data);
		}
	}
}
