package cFramework.communications.routeTables;

import java.util.Hashtable;

import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.NodeAddress;
import cFramework.nodes.Node;

public class NodeRouteTable {

	
	protected Hashtable<Long, NodeAddress> routeTable;
	
	public NodeRouteTable(){
		routeTable = new Hashtable<Long, NodeAddress>();
	}
	
	public void set(long name,String host,int port, Node objectReference){
		routeTable.put(name, new LocalJVMNodeAddress(name, host, port, objectReference));
	}
	
	public void set(NodeAddress node){
		routeTable.put(node.getName(), node);
	}
	
	public boolean exist(long id){
		return routeTable.containsKey((long)id);
	}
	
	public NodeAddress get(long id){
		return routeTable.get((long)id);
	}		
	
}
