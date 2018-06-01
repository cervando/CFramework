package kmiddle2.nodes.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kmiddle2.communications.p2p.IgniterProtocols;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.entity.Entity;
import kmiddle2.util.OSHelper;

public class Igniter {
	
	
	private IgniterProtocols protocols;
	protected List<String> areas = new ArrayList<String>();
	protected NodeConf configuration = new NodeConf();
	protected NodeLog log;

	protected void setAreas(String[] areas){
		this.areas.addAll(Arrays.asList(areas));
	}
	
	public void addArea(String areaNames, Object... parameters) {
		StringBuilder chain = new StringBuilder();
		chain.append(areaNames);
		for ( int i=0;i < parameters.length;i++) {
			chain.append(":" + parameters[i].getClass().getName() + ":" + parameters[i]);
		}
		areas.add(chain.toString());
	}
	

	protected void run(){
		System.setProperty("java.net.preferIPv4Stack" , "true");
		log = new NodeLog("Igniter", configuration.isDebug());
		if ( areas.size() == 0 ){
			log.message("No areas selected");
			return;
		}
		
		String[] areasArray = new String[areas.size()];
		for ( int i=0;i<areas.size();i++ )
			areasArray[i] = areas.get(i);
		
		
		if ( configuration.isLocal() ) {
			
			runLocal(areasArray, configuration);
		}else {
			protocols = new IgniterProtocols(log);
			protocols.sendServiceUpRequest();
			try {
				Thread.sleep(500);
				if ( !protocols.isServiceUP() ){
					log.developer("Service is down");
					initService();
					Thread.sleep(1000);
				}
				log.developer("Sending Areas List to service");
				protocols.sendList(areasArray, configuration);
				log.developer("Stopping Igniter");
				protocols.stop();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}
	
	
	private void runLocal(String[] areasNames, NodeConf nc) {
		new Entity(areasNames, nc);
	}
	
	
	
	private void initService(){
		String command = "";
		command = "java -cp " + OSHelper.preparePath(System.getProperty("java.class.path")) + " ";
		command += Service.class.getName();
		log.developer("Igniting Service");
		log.developer("Running command: " + command);
		OSHelper.exec(command);
	}
}