package kmiddle2.communications.p2p;

import java.net.BindException;

import kmiddle2.communications.LocalJVMNodeAddress;
import kmiddle2.communications.MessageMetadata;
import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.Protocol;
import kmiddle2.communications.fiels.Address;
import kmiddle2.communications.messages.DataMessage;
import kmiddle2.communications.messages.FindNodeMessage;
import kmiddle2.communications.messages.SearchNodeRequestMessage;
import kmiddle2.communications.messages.SingInActivityMessage;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.communications.routeTables.NodeRouteTable;
import kmiddle2.communications.routeTables.SingletonNodeRouteTable;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;
import kmiddle2.nodes.areas.AreaWrapper;
import kmiddle2.util.IDHelper;

public class AreaProtocols implements Protocol{

	//ActivityWrapper proccess;
	int myNodeID;
	NodeConf nc;
	P2PCommunications myCommunications;
	NodeRouteTable routeTable;
	AreaWrapper areaWrapper;
	NodeLog log;
	
	public AreaProtocols(int areaID,AreaWrapper wrapper, NodeConf nc, NodeLog log){
		myNodeID = areaID;
		areaWrapper = wrapper;
		this.log = log;
		
		try {
			myCommunications = new P2PCommunications(this, nc, this.log);
		} catch (BindException e) {
			log.debug("Port already in use");
			e.printStackTrace();
		}
		routeTable = SingletonNodeRouteTable.getInstance();
	}
	
	public void receive(Address address,  byte[] message){
		Message m =  Message.getMessage(message);
		if ( m.getOperationCode() == OperationCodeConstants.DATA ){
						DataMessage data = (DataMessage)m;
						//Is a direct message for me
						//asdf
						//if ( data.getReceiverID() == myNodeID ) {
						//	data(data.getSenderID(), data.getMetaData(), data.getData());
							//System.out.println("To me");
						//Is a message to one of my childs
						//}else{
						routeData(data.getReceiverID(), data.getSenderID(), data.getMetaData(), data.getData());
							//byte[] newData = //process(myNodeID, data.getData());
							//sendData(data.getReceiverID(), data.getSenderID(), data.getMetaData(), newData);
						//}
						
		}else if ( m.getOperationCode() == OperationCodeConstants.SINGIN_ACTIVITY ){
						singInActivity( ((SingInActivityMessage)m).getNodeID(), address);
			
						
		}else if ( m.getOperationCode() == OperationCodeConstants.FIND_NODE ){
						FindNodeMessage fnm = (FindNodeMessage)m; 
						findNode( fnm.getNodeID(), new Address( fnm.getIP(), fnm.getPort()) );
			
						
		}else if ( m.getOperationCode() == OperationCodeConstants.UPDATE ){
			
			
			
		}else if ( m.getOperationCode() == OperationCodeConstants.SEARCH_NODE_REQUEST ){
						searchNodeRequest(((SearchNodeRequestMessage)m).getNodeID(), address);
		}
	}
	
	//Add this to the routing table, for non-java Activity
	public void singInActivity(int idNode, Address address){
		
	}
	
	//The node i was looking for, add to route table, then send pending messages
	public void findNode(int idNode, Address address){
		addedToRouteTable(idNode, address);
	}
	
	//The node was succefully added to the route table, send pending messages
	public void addedToRouteTable(int idNode, Address address){
		
	}
	
	//Look in the route table for it, if it exist, send the results to the address Argument
	public void searchNodeRequest(int idNode, Address address){
		NodeAddress node = routeTable.get(idNode);
		if ( node != null){
			myCommunications.send(
					new NodeAddress(0, address), 
					new FindNodeMessage(idNode, node.getAddress().getIp(), node.getAddress().getPort()).toByteArray()
			);
		
		}else{																															//Ask to entity to broadcast
			areaWrapper.sendtoFather(new NodeAddress(0, address), new SearchNodeRequestMessage(idNode).toByteArray());
		}		
	}
	
	//One of my activities receive a new route, add to routeTable
	public void update(){
		
	}
	
	//Get ID from the IP, then send to user implementetation
	/*private void data(int id, MessageMetadata m, byte[] data){
		log.receive_debug(id, "");
		areaWrapper.receive(id, m, data);
	}*/
	
	/*
	public byte[] process(int nodeID, byte[] data){
		log.receive_debug(nodeID, "");
		return areaWrapper.process(nodeID, data);
	}
	*/
	
	public void routeData(int sendToID, int senderID, MessageMetadata meta, byte[] data){
		areaWrapper.route(sendToID, senderID,meta,data);
	}
	
	
	public void sendData(int sendToID, MessageMetadata meta, byte[] data){
		sendData(sendToID, 0, meta, data);
	}
	
	public void sendData(int sendToID, int senderID, MessageMetadata meta, byte[] data){
		if ( senderID == 0 )
			senderID = myNodeID;
		
		
		NodeAddress node;
		//This if is a validation to assure a message to anoter area is send to the Area instead of the activity
		if ( IDHelper.isActivitiy(sendToID) && IDHelper.getAreaID(sendToID) != myNodeID ){
			node = routeTable.get(IDHelper.getAreaID(sendToID) );
		}else{
			node = routeTable.get(sendToID);	
		}
		
		if ( node == null ){
			//log.developer("Node not finded: " + sendToID);
			//Add to pending messages
			//TODO
			//Ask to entity to broadcast
			log.debug("NOT FOUND, Looking for:", sendToID);
			areaWrapper.sendtoFather(new NodeAddress(myNodeID, myCommunications.getAddress()), new SearchNodeRequestMessage(sendToID).toByteArray());
		}else{
			//What is the need for this if?
			if ( senderID == myNodeID)
				log.send_debug(sendToID, "");
			myCommunications.send(node, new DataMessage(senderID, sendToID,meta, data).toByteArray());
		}
	}

	@Override
	public P2PCommunications getCommunications() {
		return this.myCommunications;
	}

	@Override
	public NodeAddress getNodeAddress() {
		return new LocalJVMNodeAddress(new NodeAddress(myNodeID, myCommunications.getAddress()), areaWrapper);
	}
}