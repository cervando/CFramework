package cFramework.communications.p2p.sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import cFramework.communications.fiels.Address;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.messages.base.Message;
import cFramework.log.NodeLog;

public class TCPSocketSender extends Thread{

	Address address;
	byte[] message;
	int myPort;
	NodeLog log;
	
	public TCPSocketSender( Address address, byte[] message, int myPort, NodeLog log){
		this.address = address;
		this.message = message;
		this.myPort = myPort;
		this.log = log;
	}
	
	public void run(){
		this.send();
	}
	
	
	public boolean send() {
		try {
			Socket s = new Socket(address.getIp(), address.getPort());
			DataOutputStream out = new DataOutputStream(s.getOutputStream());
			out.writeShort((short)myPort);
			out.write(this.message);
			out.close();
			s.close();
			
		} catch (IOException e) {
			//e.printStackTrace();
			if ( !(Message.getMessage(this.message) instanceof HelloMessage))
				log.debug(e.getMessage());
			return false;
		}
		return true;
	}
}
