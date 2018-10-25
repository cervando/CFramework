package kmiddle2.nodes.areas;

import kmiddle2.communications.MessageMetadata;
import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.Protocol;
import kmiddle2.communications.p2p.AreaProtocols;
import kmiddle2.communications.p2p.EntityProtocols;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.Node;
import kmiddle2.nodes.NodeConf;

public class AreaWrapper extends Node{

	NodeAddress father;
	EntityProtocols fatherProtocols;
	AreaProtocols protocols;
	private Area area;
	private ActivityInitializer activities;
	protected NodeConf nc;
	private NodeLog log;
	
	public AreaWrapper(Area area, EntityProtocols entityCommunication, NodeConf nc){
		this.fatherProtocols = entityCommunication;
		this.nc = nc;
		this.area = area;	
		log = new NodeLog(area.getID(), area.getNamer(), nc.isDebug());
		area.setLog(log);
		area.setCore(this);
	}
	
	public void setUp(){
		activities = new ActivityInitializer(area.getActivities(), log);
		protocols = new AreaProtocols(area.getID(), this, nc, log);	
	}
	
	public void setupActivities(){
		activities.setUp(protocols.getNodeAddress(), nc);
	}
	
	public void init(){
		//The thread is to avoid blocking behavior, but it could produce Asynchrony
		//new Thread(){
			//public void run(){
				area.init();
				activities.initAll();
			//}
		//}.start();
	}
	
	@Override
	public Protocol getProtocol() {
		return protocols;
	}

	public void route(int sendToID, int senderID, MessageMetadata meta, byte[] data){
		area.route(sendToID, senderID,meta,data);
	}
	
	
	@Override
	public void receive(int id, MessageMetadata m, byte[] data) {
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
	
	
	public void send(int nodeID, MessageMetadata m, byte[] data){
		protocols.sendData(nodeID, m, data);
	}
	
	public void send(int nodeID, int fromID, MessageMetadata m, byte[] data){
		protocols.sendData(nodeID, fromID, m, data);
	}
	
	
	
	
	
	
	public void setFather(NodeAddress father){
		this.father = father;
	}
	
	public void sendtoFather(NodeAddress senderAddress, byte[] message){
		fatherProtocols.receive(senderAddress.getAddress(), message);
	}
	
	public NodeConf getConfiguration(){
		return this.nc;
	}
}
