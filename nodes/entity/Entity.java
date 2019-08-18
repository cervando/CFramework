package cFramework.nodes.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.util.concurrent.Semaphore;

import cFramework.communications.messages.MessageReceiverable;
import cFramework.communications.messages.base.Message;
import cFramework.communications.p2p.EntityProtocols;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.util.DefaultValues;
import cFramework.util.IDHelper;

/**
 * 
 * @author Armando Cervantes
 *
 */
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
			//init sockets p2p and multicast
			this.communications = new EntityProtocols(this, this.nc, DefaultValues.ENTITY_PORT, routeTable, log);						
		} catch (BindException e) {
			log.message("Port already in use");
			log.message("It is posible that an entity with the EntityID " + nc.getEntityID() + " is already running");
			log.message("Press Enter to close");
			try {
				System.in.read();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return;
		}
		//Creates an area Manager 
		areas = new AreasManager(routeTable, communications, log);
		//The area manager init the list of Areas
		areas.addList(areasNames, nc);		
		
		//This allow the System to receive command directly from console
		new Thread(){
			public void run(){
				BufferedReader buffer=new BufferedReader(new InputStreamReader(System.in));
				
				String command;
				while (true) {
					try {
						command = buffer.readLine();
						String words[] = command.split(" ");
						if ( words[0].toUpperCase().equals("FAIL") ) {
							long id = 0;
							if ( words.length == 3) { 
								id = IDHelper.generateID(words[1], words[2]);
							}else 
								id = IDHelper.generateID(words[1]);
							// TODO
							System.out.println("send fail to ID: " + id);
							//communications.sendFailMessage(id);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	@Override
	public void receive(Message m) {}
	
	
	
	public static void main(String[] args){
		//This propertie allows compatibility with OS X
		System.setProperty("java.net.preferIPv4Stack" , "true");
		String list = "";
		int nodeConfValue = 0;
		
		//Extract arguments
		for ( int  i = 0; i < args.length; i+=2){																						
			if ( args[i].equals("--list"))
				list = args[i+1];
				
			else if ( args[i].equals("--nodeConfiguration"))
				nodeConfValue = Integer.parseInt(args[i+1]);
		}
		new Entity(list.split(","), new NodeConf(nodeConfValue));
	}	
}