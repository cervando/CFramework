package cFramework.communications;

import cFramework.communications.p2p.P2PCommunications;
import cFramework.nodes.Node;

/** A class encapsuling an object reference along with ID, IP and port, this make posible use shared memory or IP connections according to the case
 * 
 * @author Armando Cervantes
 *
 */
public class LocalJVMNodeAddress extends NodeAddress{

	private P2PCommunications nodeCommunicationsReference;
	
	/**
	 * 
	 * @param address			object Containing ID, IP and port from a middleware Node			
	 * @param objectReference	Reference to that Node
	 */
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