package cFramework.communications;

import cFramework.communications.p2p.P2PCommunications;
import cFramework.nodes.Node;

public class LocalJVMNodeAddress extends NodeAddress{

	private P2PCommunications nodeCommunicationsReference;
	
	public LocalJVMNodeAddress(NodeAddress address, Node objectReference){
		this(address.getName(), address.getHost(), address.getPort(), objectReference);
	}
	
	public LocalJVMNodeAddress(long name,String host,int port, Node objectReference){
		super(name, host, port);
		this.nodeCommunicationsReference = objectReference.getProtocol().getCommunications();
	}
	
	public P2PCommunications getNodeReference(){
		return nodeCommunicationsReference;
	}
}