package cFramework.nodes.service;

import cFramework.communications.p2p.ServiceProtocol;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.nodes.entity.Entity;
import cFramework.util.OSHelper;

public class Service {

	ServiceProtocol protocol;
	NodeLog log;
	
	public Service(){
		//This propertie allows compatibility whic OS X
		System.setProperty("java.net.preferIPv4Stack" , "true");
		//Log system for the service
		log = new NodeLog("Service", null);
		log.message("This is the Cmiddle Service, it is not necessary shut it down to run a new entity");
		protocol = new ServiceProtocol(this, log);
	}
	
	/**
	 * Creates a new entity on this machine
	 * @param nc 	Configuration for the entity
	 * @param list 	List of areas that the entity, it follows the format:
	 * Path of the source files, area1,....,areaN
	 */
	public void createEntity(NodeConf nc, String list){
		log.message("Setting up Entity " + nc.getEntityID() );
		
		String path = list.substring(0, list.indexOf(','));
		String areas = list.substring(list.indexOf(',') + 1);
		String command = "java -cp " + OSHelper.preparePath(path) + " ";
	
		//Get the name of the Enity class
		command += Entity.class.getName() + " ";
		command += "--list " + areas + " ";	
		command += "--nodeConfiguration " + nc.toInt() + " ";
		OSHelper.exec(command);
	}
	
	
	
	public static void main(String args[]){
		new Service();
	}	
}