package cFramework.communications.routeTables;

import java.util.ArrayList;
import java.util.Hashtable;

import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.NodeAddress;
import cFramework.nodes.Node;

public class NodeRouteTable {

	
	protected Hashtable<Long, ArrayList<NodeAddress>> routeTable;
	
	public NodeRouteTable(){
		routeTable = new Hashtable<Long, ArrayList<NodeAddress>>();
	}
	
	public void set(long name,String host,int port, Node objectReference){
		set(new LocalJVMNodeAddress(name, host, port, objectReference));
	}
	
	
	public void set(ArrayList<NodeAddress> nodes){
		for ( int i=0; i< nodes.size();i++) {
			set(nodes.get(i));
		}
	}
	public void set(NodeAddress node){
		ArrayList<NodeAddress> addresses = routeTable.get((long)node.getName());
		if ( addresses == null ) {
			addresses = new ArrayList<NodeAddress>();
			routeTable.put((long)node.getName(), addresses);
		}
		addresses.add(node);
	}
	
	public ArrayList<NodeAddress> get(long id){
		return routeTable.get((long)id);
	}		
	
	public boolean exist(long id){
		return routeTable.containsKey((long)id);
	}
}
