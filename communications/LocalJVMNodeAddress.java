package kmiddle2.communications;

import kmiddle2.communications.p2p.P2PCommunications;
import kmiddle2.nodes.Node;

public class LocalJVMNodeAddress extends NodeAddress{

	private P2PCommunications nodeCommunicationsReference;
	
	public LocalJVMNodeAddress(NodeAddress address, Node objectReference){
		this(address.getName(), address.getHost(), address.getPort(), objectReference);
	}
	
	public LocalJVMNodeAddress(int name,String host,int port, Node objectReference){
		super(name, host, port);
		this.nodeCommunicationsReference = objectReference.getProtocol().getCommunications();
	}
	
	public P2PCommunications getNodeReference(){
		return nodeCommunicationsReference;
	}
}