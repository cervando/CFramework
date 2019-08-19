/**
 * 
 * TCPPort
 * 
 * V2
 * 
 * 13/03/2017
 * 
 * Kuayolotl Middleware System
 * @author Karina Jaime <ajaime@gdl.cinvestav.mx>
 *
 * This file is part of Kuayolotl Middleware
 *
 * Kuayolotl Middleware is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *	
 * Kuayolotl Middleware is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *	
 * You should have received a copy of the GNU General Public License
 * along with Kuayolotl Middleware.  If not, see <http://www.gnu.org/licenses/>.
 * 
*/


package cFramework.communications.p2p.sockets;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import cFramework.communications.BinaryArrayNotificable;
import cFramework.communications.fiels.Address;
import cFramework.log.NodeLog;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class TCPPort implements Port {
	
	private BinaryArrayNotificable messageReceiver;					/* Objeto que implementa la interface CommunicationBehavior al cual se le enviara el mensaje */

	private ServerSocket serverSocket;								/* Se encarga de escuchar por mensajes */

	private Address myAddress;
	
	private boolean run;
	
	private NodeLog log;
	
	
	/**
	 * @param messageManager 
	 * Object that listen throug this port
	 */
	public TCPPort( BinaryArrayNotificable messageManager, NodeLog log){
		this.messageReceiver = messageManager;
		this.log = log;
	}
	
	public Address setUp(int port){
		myAddress = new Address();
		try {
			serverSocket = new ServerSocket(port);									//Starts the socket and bind it to any free port
			myAddress.setPort( serverSocket.getLocalPort() );
                        String ip = "127.0.0.1";
                        Enumeration en = NetworkInterface.getNetworkInterfaces();
                        while(en.hasMoreElements()){
                            NetworkInterface ni=(NetworkInterface) en.nextElement();
                            Enumeration ee = ni.getInetAddresses();
                            while(ee.hasMoreElements()) {
                                InetAddress ia= (InetAddress) ee.nextElement();
                                if ( !ia.equals("127.0.0.1") && !ia.equals("127.0.1.1") && !ia.equals("0.0.0.0"))
                                    ip = ia.getHostAddress();
                            }
                        }

                        myAddress.setIp(ip);
                        run = true;
        }
        catch (IOException socketException) {
            log.debug(socketException.toString());
        	//socketException.printStackTrace();
        	return null;
        }
		//temporal.header(myName, myAddress);
		return myAddress;
	}
	
	public void startListening(){
		(new Thread(this)).start();
	}
	
	/**
	 * Thread for the listener
	 */
	public void run(){
		while(run){
			 try {
				Socket connectionSocket = serverSocket.accept();
				new TCPSocketListener(connectionSocket, messageReceiver).start();							//Start a new thread to receive a message
			 } catch (SocketException e) {
				 if ( run || !e.getMessage().toLowerCase().equals("socket closed") )
					 e.printStackTrace(); 
			 } catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if ( !serverSocket.isClosed())
				serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stopListening(){
		run = false;
		try {
			serverSocket.close();
			log.developer("Socket Sussefully Closed");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean send(Address address, byte[] message) {
		return new TCPSocketSender(address, message,myAddress.getPort(), log).send();
	}
}
