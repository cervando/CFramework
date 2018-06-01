package kmiddle2.nodes.service;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kmiddle2.communications.p2p.IgniterProtocols;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.entity.Entity;
import kmiddle2.util.OSHelper;

/**
 * 
 * @author Armando Cervantes
 *
 */

public class Igniter {
	
	
	private IgniterProtocols protocols;
	protected List<String> areas = new ArrayList<String>();
	protected NodeConf configuration = new NodeConf();
	protected NodeLog log;

	/**
	 * Add an String array with the names of the areas
	 * @param areas List of areas to be ignited
	 */
	protected void setAreas(String[] areas){
		this.areas.addAll(Arrays.asList(areas));
	}
	
	
	/**
	 * Add an area with specific parameters
	 * @param areaNames		Name of the area
	 * @param parameters 	List of objects to be send
	 */
	public void addArea(String areaNames, Object... parameters) {
		StringBuilder chain = new StringBuilder();
		chain.append(areaNames);
		for ( int i=0;i < parameters.length;i++) {
			chain.append(":" + parameters[i].getClass().getName() + ":" + parameters[i]);
		}
		areas.add(chain.toString());
	}
	

	

	/**
	 * Ignites an entity on this JAVA VM
	 * @param areasNames Array of areas to be ignited
	 * @param nc Configuration for this entity
	 */
	private void runLocal(String[] areasNames, NodeConf nc) {
		new Entity(areasNames, nc);
	}
	
	
	/**
	 * Ignites a new Service process
	 */
	private void initService(){
		String command = "";
		command = "java -cp " + OSHelper.preparePath(System.getProperty("java.class.path")) + " ";
		command += Service.class.getName();
		log.developer("Igniting Service");
		log.developer("Running command: " + command);
		OSHelper.exec(command);
	}
	
	
	
	
	
	/**
	 * This is the start point of the framework
	 */
	protected void run(){
		//This propertie allows compatibility with OS X
		System.setProperty("java.net.preferIPv4Stack" , "true");
		//Log system for the igniter
		log = new NodeLog("Igniter", configuration.isDebug());
		
		if ( areas.size() == 0 ){
			log.message("No areas selected");
			return;
		}
		
		
		String[] areasArray = new String[areas.size()];
		for ( int i=0;i<areas.size();i++ )
			areasArray[i] = areas.get(i);
		
		
		//By default the configuration is local, this means it will only run in this machine
		if ( configuration.isLocal() ) {
			runLocal(areasArray, configuration);
		}else {
			//Init P2P communication
			protocols = new IgniterProtocols(log);
			
			//Check if there is a Service running on this machine
			protocols.sendServiceUpRequest();
			try {
				Thread.sleep(500);
				//If service is down, ignites a service and wait for it to instanciate
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
}