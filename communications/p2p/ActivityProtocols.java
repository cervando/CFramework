package cFramework.communications.p2p;

import java.net.BindException;

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
import cFramework.nodes.activities.ActivityWrapper;
import cFramework.util.IDHelper;

public class ActivityProtocols implements Protocol{

	ActivityWrapper proccessCore;
	long myNodeID;
	NodeAddress father;
	NodeConf nc;
	P2PCommunications myCommunications;
	NodeRouteTable routeTable;
	NodeRouteTable helpers;
	NodeLog log;

	public ActivityProtocols(long myNodeID, NodeAddress father, ActivityWrapper process, NodeConf nc, NodeLog log){
		this.myNodeID = myNodeID;
		this.father = father;
		this.proccessCore = process;
		this.nc = nc;
		this.log = log;
		try {
			myCommunications = new P2PCommunications(this, nc, log);
		} catch (BindException e) {
			System.out.println("Port already in use");
			e.printStackTrace();
		}
		routeTable = SingletonNodeRouteTable.getInstance();
		helpers = new NodeRouteTable();
	}
	
	public void receive(Address address, byte[] message){
		Message m =  Message.getMessage(message);
		int OPCode = m.getOperationCode(); 
		
		if ( OPCode == OperationCodeConstants.DATA ){
			log.receive_debug(((DataMessage)m).getSenderID(), "");
			data(address, (DataMessage)m);
			
		}else if ( OPCode == OperationCodeConstants.SINGIN_ACTIVITY ){
			singInActivity( ((SingInActivityMessage)m).getNodeID(), address);
			
		}else if ( OPCode == OperationCodeConstants.FIND_NODE ){
			FindNodeMessage fnm = (FindNodeMessage)m; 
			findNode( fnm.getNodeID(), new Address( fnm.getIP(), fnm.getPort()) );
			
		}
	}
	
	//Add this to the routing table, as my Activity/ActivityHelper
	public void singInActivity(long idNode, Address address){
		helpers.set(new NodeAddress(idNode, address));
	}
	
	//The node i was looking for, add to route table, then send pending messages
	public void findNode(long idNode, Address address){
		//Add to route table
		if ( routeTable.exist(idNode)){
		
		}else
			routeTable.set(new NodeAddress(idNode, address));
		//Send pending messages
		
		//Send the new direction to the service
		update();
	}
	

	//One of my activities receive a new route, add to routeTable
	public void update(){
		
	}
	
	//Get ID from the IP, then send to user implementetation
	private void data(Address address, DataMessage msg){
		proccessCore.receive(msg.getSenderID(), msg.getMetaData(), msg.getData());
	}
	
	private void searchNodeRequest(long node){
		myCommunications.send(father, new SearchNodeRequestMessage(node).toByteArray());
	}
	
	
	public boolean sendData(long sendToID, MessageMetadata meta, byte[] data){
		log.send_debug(sendToID, "");
		long senderID = myNodeID;
		NodeAddress node;
		if ( IDHelper.getAreaID(sendToID) != father.getName() ){
			node = routeTable.get(IDHelper.getAreaID(sendToID) );
			//senderID = IDHelper.getAreaID(senderID);
		}else{
			node = routeTable.get(sendToID);	
		}
		
		
		if ( node == null ){

			log.debug("NOT FOUND, Looking for:", sendToID);
			searchNodeRequest(sendToID);
			//Add to pending messages
			return false;
		}else
			return myCommunications.send(node, new DataMessage(senderID, sendToID, meta, data).toByteArray());		
	}
	
	@Override
	public P2PCommunications getCommunications() {
		// TODO Auto-generated method stub
		return this.myCommunications;
	}

	@Override
	public NodeAddress getNodeAddress() {
		// TODO Auto-generated method stub
		return new NodeAddress(myNodeID, myCommunications.getAddress());
	}

}
 