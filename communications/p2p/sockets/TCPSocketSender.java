package kmiddle2.communications.p2p.sockets;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import kmiddle2.communications.fiels.Address;
import kmiddle2.communications.messages.HelloMessage;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.log.NodeLog;

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
		}
	}
}
