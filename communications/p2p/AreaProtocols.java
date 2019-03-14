package cFramework.communications.p2p;

import java.net.BindException;

import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.MessageMetadata;
import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;
import cFramework.communications.fiels.Address;
import cFramework.communications.messages.DataMessage;
import cFramework.communications.messages.FindNodeMessage;
import cFramework.communications.messages.SearchNodeRequestMessage;
import cFramework.communications.messages.SingInActivityMessage;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.communications.routeTables.NodeRouteTable;
import cFramework.communications.routeTables.SingletonNodeRouteTable;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;
import cFramework.nodes.areas.AreaWrapper;
import cFramework.util.IDHelper;

public class AreaProtocols implements Protocol{

	//ActivityWrapper proccess;
	long myNodeID;
	NodeConf nc;
	P2PCommunications myCommunications;
	NodeRouteTable routeTable;
	AreaWrapper areaWrapper;
	NodeLog log;
	
	public AreaProtocols(long areaID,AreaWrapper wrapper, NodeConf nc, NodeLog log){
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
	public void singInActivity(long idNode, Address address){
		
	}
	
	//The node i was looking for, add to route table, then send pending messages
	public void findNode(long idNode, Address address){
		addedToRouteTable(idNode, address);
	}
	
	//The node was succefully added to the route table, send pending messages
	public void addedToRouteTable(long idNode, Address address){
		
	}
	
	//Look in the route table for it, if it exist, send the results to the address Argument
	public void searchNodeRequest(long idNode, Address address){
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
	
	public void routeData(long sendToID, long senderID, MessageMetadata meta, byte[] data){
		areaWrapper.route(sendToID, senderID,meta,data);
	}
	
	
	public boolean sendData(long sendToID, MessageMetadata meta, byte[] data){
		return sendData(sendToID, 0, meta, data);
	}
	
	public boolean sendData(long sendToID, long senderID, MessageMetadata meta, byte[] data){
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
			return false;
		}else{
			//What is the need for this if?
			if ( senderID == myNodeID)
				log.send_debug(sendToID, "");
			return myCommunications.send(node, new DataMessage(senderID, sendToID,meta, data).toByteArray());
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