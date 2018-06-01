package kmiddle2.communications.routeTables;

import java.util.Hashtable;

import kmiddle2.communications.LocalJVMNodeAddress;
import kmiddle2.communications.NodeAddress;
import kmiddle2.nodes.Node;

public class NodeRouteTable {

	
	protected Hashtable<Integer, NodeAddress> routeTable;
	
	public NodeRouteTable(){
		routeTable = new Hashtable<Integer, NodeAddress>();
	}
	
	public void set(int name,String host,int port, Node objectReference){
		routeTable.put(name, new LocalJVMNodeAddress(name, host, port, objectReference));
	}
	
	public void set(NodeAddress node){
		routeTable.put(node.getName(), node);
	}
	
	public boolean exist(int id){
		return routeTable.containsKey(id);
	}
	
	public NodeAddress get(int id){
		return routeTable.get(id);
	}		
	
}
