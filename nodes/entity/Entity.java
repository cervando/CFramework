	package kmiddle2.nodes.entity;

import java.net.BindException;

import kmiddle2.communications.messages.MessageReceiverable;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.p2p.EntityProtocols;
import kmiddle2.communications.routeTables.NodeRouteTable;
import kmiddle2.communications.routeTables.SingletonNodeRouteTable;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.util.DefaultValues;

public class Entity implements MessageReceiverable{

	EntityProtocols communications;
	NodeConf nc;
	private AreasManager areas;
	private NodeRouteTable routeTable;
	private NodeLog log;
	
	
	public Entity(String[] areasNames, NodeConf nc){
		this.nc = nc;
		log = new NodeLog("Entity " + nc.getEntityID(), nc.isDebug());
		routeTable = SingletonNodeRouteTable.getInstance();
		
		try {
			this.communications = new EntityProtocols(this, this.nc, DefaultValues.ENTITY_PORT, routeTable, log);						//init sockets p2p and multicast
		} catch (BindException e) {
			log.message("Port already in use");
			return;
		}
		areas = new AreasManager(routeTable, communications, log);		
		areas.addList(areasNames, nc);		
	}
	
	@Override
	public void receive(Message m) {}
	
	public static void main(String[] args){
		System.setProperty("java.net.preferIPv4Stack" , "true");
		String list = "";
		int nodeConfValue = 0;
		
		for ( int  i = 0; i < args.length; i+=2){																						//Extract arguments
			if ( args[i].equals("--list"))
				list = args[i+1];
				
			else if ( args[i].equals("--nodeConfiguration"))
				nodeConfValue = Integer.parseInt(args[i+1]);
		}
		new Entity(list.split(","), new NodeConf(nodeConfValue));
	}	
}