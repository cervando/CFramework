package kmiddle2.communications.p2p.sockets;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import kmiddle2.communications.BinaryArrayNotificable;
import kmiddle2.communications.fiels.Address;

public class TCPSocketListener extends Thread{

	Socket socket;
	BinaryArrayNotificable listener;
	
	public TCPSocketListener( Socket sock, BinaryArrayNotificable listener){
		this.socket = sock;
		this.listener = listener;
	}
	
	public void run(){
		try {
			DataInputStream in =
			        new DataInputStream(socket.getInputStream());
			
			int incomingPort = in.readUnsignedShort();
			int fragmentSize = 64000;
			byte[] messageFragment = new byte[fragmentSize];
			int readed = 0;
			int messageLengt = 0;
			ArrayList<byte[]> messContent = new ArrayList<byte[]>();
			
			while((readed = in.read(messageFragment)) > 0){
				messageLengt += readed;
				messContent.add(messageFragment.clone());
			}
			
			int start = 0;
			byte[] message = new byte[messageLengt];
			for( byte[] fragment: messContent){
				if ( start + fragmentSize > messageLengt )
					System.arraycopy(fragment, 0, message, start, messageLengt % fragmentSize);
				else	
					System.arraycopy(fragment, 0, message, start, fragmentSize);
				start += fragmentSize;
			}
			
			Address addr = new Address(
					socket.getInetAddress().toString().substring(1), 
					incomingPort);
			in.close();
			socket.close();
			listener.receive(addr, message);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
