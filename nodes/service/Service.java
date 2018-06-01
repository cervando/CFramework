package kmiddle2.nodes.service;

import kmiddle2.communications.p2p.ServiceProtocol;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.entity.Entity;
import kmiddle2.util.OSHelper;

public class Service {

	ServiceProtocol protocol;
	NodeLog log;
	
	public Service(){
		System.setProperty("java.net.preferIPv4Stack" , "true");
		log = new NodeLog("Service", null);
		log.message("This is the Kmiddle Service, it is not necessary shut it down to run a new entity");
		protocol = new ServiceProtocol(this, log);
	}
	
	public void createEntity(NodeConf nc, String list){
		log.message("Setting up Entity " + nc.getEntityID() );
		
		
		String path = list.substring(0, list.indexOf(','));
		String areas = list.substring(list.indexOf(',') + 1);
		String command = "java -cp " + OSHelper.preparePath(path) + " ";
	
		command += Entity.class.getName() + " ";																		//Nombre de la clase con class.getName
		command += "--list " + areas + " ";																				//Argumentos de la clase
		command += "--nodeConfiguration " + nc.toInt() + " ";
		OSHelper.exec(command);
	}
	
	public static void main(String args[]){
		new Service();
	}	
}