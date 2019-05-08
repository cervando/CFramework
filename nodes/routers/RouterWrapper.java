package cFramework.nodes.routers;

import cFramework.communications.MessageMetadata;
import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;
import cFramework.communications.messages.SingInAreaNotificationMessage;
import cFramework.communications.p2p.AreaProtocols;
import cFramework.communications.p2p.EntityProtocols;
import cFramework.log.NodeLog;
import cFramework.nodes.Node;
import cFramework.nodes.NodeConf;

public class RouterWrapper extends Node{

	NodeAddress father;
	EntityProtocols entityProtocols;
	AreaProtocols protocols;
	private Router area;
	private ProcessInitializer activities;
	protected NodeConf nc;
	private NodeLog log;
	
	public RouterWrapper(Router area, EntityProtocols entityCommunication, NodeConf nc){
		this.entityProtocols = entityCommunication;
		this.nc = nc;
		this.area = area;	
		log = new NodeLog(area.getID(), area.getNamer(), nc.isDebug());
		area.setLog(log);
		area.setCore(this);
	}
	
	public void setUp(){
		activities = new ProcessInitializer(area.getActivities(), log);
		protocols = new AreaProtocols(area.getID(), this, nc, log);	
	}
	
	public void setupActivities(){
		activities.setUp(protocols.getNodeAddress(), nc);
	}
	
	public void init(){
		//The thread is to avoid blocking behavior, but it could produce Asynchrony
		//new Thread(){
			//public void run(){
				//sendtoEntity(protocols.getNodeAddress() , new SingInAreaNotificationMessage(protocols.getNodeAddress()).toByteArray()  );
				if ( !this.nc.isLocal())
					entityProtocols.SendSingInAreaNotification(protocols.getNodeAddress());
				area.init();
				activities.initAll();
			//}
		//}.start();
	}
	
	@Override
	public Protocol getProtocol() {
		return protocols;
	}

	public void route(long sendToID, long senderID, MessageMetadata meta, byte[] data){
		area.route(sendToID, senderID,meta,data);
	}
	
	
	@Override
	public void receive(long id, MessageMetadata m, byte[] data) {
		//area.receive(id, m, data);
		System.out.println("How this was called?");
	}
	/*
	public byte[] process(int nodeID, byte[] data){
		return area.process(nodeID, data);
	}
	*/
	
	
	// Methods Related to send ---------------------------------------------------------
	/*public void send(int nodeID, byte[] data){
		protocols.sendData(nodeID, new MessageMetadata(0), data);
	}*/
	
	
	public boolean send(long nodeID, MessageMetadata m, byte[] data){
		System.out.println("Sending to: " + nodeID);
		return protocols.sendData(nodeID, m, data);
	}
	
	public boolean send(long nodeID, long fromID, MessageMetadata m, byte[] data){
		return protocols.sendData(nodeID, fromID, m, data);
	}
	
	
	
	
	
	
	public void setFather(NodeAddress father){
		this.father = father;
	}
	
	public void sendtoEntity(NodeAddress senderAddress, byte[] message){
		entityProtocols.receive(senderAddress.getAddress(), message);
	}
	
	public NodeConf getConfiguration(){
		return this.nc;
	}
}
