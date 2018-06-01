package kmiddle2.communications.p2p;

import java.net.BindException;

import javax.xml.stream.events.NotationDeclaration;

import kmiddle2.communications.BinaryArrayNotificable;
import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.Protocol;
import kmiddle2.communications.fiels.Address;
import kmiddle2.communications.messages.FindNodeMessage;
import kmiddle2.communications.messages.HandShakeMessage;
import kmiddle2.communications.messages.MessageReceiverable;
import kmiddle2.communications.messages.SearchMulticastMessage;
import kmiddle2.communications.messages.SearchNodeRequestMessage;
import kmiddle2.communications.messages.SingInAreaNotificationMessage;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.communications.multicast.Multicast;
import kmiddle2.communications.routeTables.NodeRouteTable;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;

public class EntityProtocols implements BinaryArrayNotificable, Protocol{

	P2PCommunications myCommunications;
	Multicast multicastConection;
	MessageReceiverable listener;
	NodeRouteTable routeTable;
	NodeLog log;
	NodeConf conf;
	
	public EntityProtocols(MessageReceiverable listener, NodeConf nc, int port,NodeRouteTable routeTable, NodeLog log) throws BindException{
		this.listener = listener;
		this.routeTable = routeTable;
		this.log = log;
		this.conf = nc;
		myCommunications = new P2PCommunications(this, nc, port + nc.getEntityID(),log);
		if ( !nc.isLocal()) {
			multicastConection = new Multicast(this, nc, log);													//Connecto to multicast group
			multicastConection.setUp();
		}
	}
	
	public void receive(Address address, byte[] message){
		Message m =  Message.getMessage(message);
			
		if ( m.getOperationCode() == OperationCodeConstants.FIND_NODE ){
			FindNodeMessage fnm = (FindNodeMessage)m; 
			findNode( fnm.getNodeID(), new Address( fnm.getIP(), fnm.getPort()) );
			
		}else if ( m.getOperationCode() == OperationCodeConstants.UPDATE ){
			
		}else if ( m.getOperationCode() == OperationCodeConstants.SEARCH_NODE_REQUEST ){
			if (!conf.isLocal())
				searchNodeRequest(((SearchNodeRequestMessage)m).getNodeID(), address);
			
		//}else if(m.getOperationCode()== OperationCodeConstants.SINGIN_AREA_NOTIFICATION){
			//Add to the routing table
			
		} else if (m.getOperationCode()== OperationCodeConstants.SEARCH_MULTICAST){										/* Un vecino solicita una direcciones */
			SearchMulticastMessage smm = (SearchMulticastMessage)m;			
			SearchNodeMulticast( new Address(smm.getIP(), smm.getPort()), smm.getLookedName());
			
		}else if ( m.getOperationCode() == OperationCodeConstants.HELLO ){
			myCommunications.send(new NodeAddress(0, address), new HandShakeMessage().toByteArray());
			
		}
	}
	
	/**
	 * //The node ID was found, add to route table, then send pending messages
	 * @param idNode
	 * @param address
	 */
	private void findNode(int idNode, Address address){		
		addedToRouteTable(idNode, address);
		
	}
	
	/**
	 * The node was succefully added to the route table, send pending messages
	 * @param idNode
	 * @param address
	 */
	private void addedToRouteTable(int idNode, Address address){
		routeTable.set(new NodeAddress(idNode, address));
		
	}
	
	/**
	 * Look in the route table for it, if it exist, send the results to the address Argument
	 * @param idNode
	 * @param address
	 */
	private void searchNodeRequest(int idNode, Address address){
		multicastConection.send(
				new SearchMulticastMessage(
						0,  
						address.getIp(),  
						address.getPort(),
						idNode
				).toByteArray()
		);
	}
	
	//One of my activities receive a new route, add to routeTable
	private void update(){
		
	}
	
	
	@Override
	public P2PCommunications getCommunications() {
		// TODO Auto-generated method stub
		return this.myCommunications;
	}
	
	
	private void SingInAreaNotification(int myID, Address address){
		multicastConection.send(
				new SingInAreaNotificationMessage(
						myID,  
						address.getIp(),  
						address.getPort()
				).toByteArray()
		);
	}
	
	private void SearchNodeMulticast(Address address, int lookedNodeID){
		NodeAddress node = routeTable.get(lookedNodeID);
		if ( node != null ){
			myCommunications.send(
						new NodeAddress(0, address), 
						new FindNodeMessage(
								lookedNodeID, 
								node.getHost(), 
								node.getPort()
						).toByteArray()
			);
		}
	}

	@Override
	public NodeAddress getNodeAddress() {
		// TODO Auto-generated method stub
		return new NodeAddress(0, myCommunications.getAddress());
	}
}
 