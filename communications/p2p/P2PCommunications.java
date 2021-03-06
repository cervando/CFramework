package cFramework.communications.p2p;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.BindException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import cFramework.communications.AddressAndBytes;
import cFramework.communications.BinaryArrayNotificable;
import cFramework.communications.LocalJVMNodeAddress;
import cFramework.communications.NodeAddress;
import cFramework.communications.Protocol;
import cFramework.communications.fiels.Address;
import cFramework.communications.p2p.sockets.Port;
import cFramework.communications.p2p.sockets.TCPPort;
import cFramework.communications.p2p.sockets.UDPPort;
import cFramework.log.NodeLog;
import cFramework.nodes.NodeConf;

public class P2PCommunications implements BinaryArrayNotificable {

	private Protocol protocolsLayer;
	private Port socketPort;
	private Address myAddress;
	private BlockingQueue<AddressAndBytes> messageQueue;
	private Thread messageQueueThread;
	private NodeLog log;
	
	
	public P2PCommunications(Protocol protocols, int port, NodeLog log) throws BindException{
		this(protocols, new NodeConf(), port, log );
	}
	
	public P2PCommunications(Protocol protocols, NodeConf nc, NodeLog log) throws BindException{
		this(protocols, nc, 0, log);
	}

	public P2PCommunications(Protocol protocols, NodeConf nc, int port, final NodeLog log) throws BindException  {
		this.protocolsLayer = protocols;
		this.log = log;
		
		if ( nc.isUDP() )
			socketPort = new UDPPort(this, log);
		else
			socketPort = new TCPPort(this, log);
		
		myAddress = socketPort.setUp(port);
		if ( myAddress == null )
			throw new BindException();
		
		socketPort.startListening();
		messageQueue = new ArrayBlockingQueue<AddressAndBytes>(1024);
		messageQueueThread = new Thread(){
			public void run(){
				boolean run = true;
				while(run){
					AddressAndBytes m;
					try {
						m = messageQueue.take();
						
						//Is this is commented the packages will be proceced in order, 
						//HOWEVER, if the Area executes a SLEEP, the WHOLE Processing will stop
						//new Thread(){
							//public void run(){
								protocolsLayer.receive(m.getAddress(), m.getBytes());
							//}
						//}.start();
					} catch (InterruptedException e) {
						log.developer("MessageQueue closed");
						run = false;
					}
					
				}
			}
		};
		messageQueueThread.start();
	}
	
	/**
	 * This methos choose which is the best communication method to send the message and send it.
	 * @param address
	 * @param message
	 */
	public boolean send(NodeAddress address, byte[] message){
		if ( address instanceof LocalJVMNodeAddress ) {
			((LocalJVMNodeAddress) address).getNodeReference().receive(myAddress, message);
			return true;
		}else{
			return socketPort.send(address.getAddress(), message);
		}
	}
	
	
	@Override
	public void receive(Address address, byte[] message) {
		try {
			messageQueue.put(new AddressAndBytes(address, message));
		} catch (InterruptedException e) {
			log.debug("Error enqueueing message");
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			log.developer( errors.toString() );
		}
	}
	
	public Address getAddress(){
		return myAddress;
	}
	
	public void stop(){
		socketPort.stopListening();
		messageQueueThread.interrupt();
	}

}
