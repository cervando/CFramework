package cFramework.communications.routeTables;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.NodeAddress;
import cFramework.nodes.Node;

public class NodeRouteTable {

	
	protected Hashtable<Long, List<NodeAddress>> routeTable;
	
	public NodeRouteTable(){
		routeTable = new Hashtable<Long, List<NodeAddress>>();
	}
	
	public void set(long name,String host,int port, Node objectReference){
		set(new LocalJVMNodeAddress(name, host, port, objectReference));
	}
	
	public void set(NodeAddress node){
		List<NodeAddress> addresses = routeTable.get((long)node.getName());
		if ( addresses == null ) {
			addresses = new ArrayList<NodeAddress>();
			routeTable.put((long)node.getName(), addresses);
		}
		addresses.add(node);
	}
	
	public List<NodeAddress> get(long id){
		return routeTable.get((long)id);
	}		
	
	public boolean exist(long id){
		return routeTable.containsKey((long)id);
	}
}
