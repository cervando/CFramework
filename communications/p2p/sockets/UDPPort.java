/**
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
 */

package cFramework.communications.p2p.sockets;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import cFramework.communications.BinaryArrayNotificable;
import cFramework.communications.fiels.Address;
import cFramework.log.NodeLog;


public class UDPPort implements Port {
	
	private BinaryArrayNotificable messageReceiver;						/** Objeto que implementa la interface CommunicationBehavior al cual se le enviara el mensaje */	
	
	private DatagramSocket datagramSocket;								/** Se encarga de escuchar por mensajes **/
	
	private DatagramPacket receiveMessage;								/** Objeto para guardar el mensaje resivido **/
	
	private boolean run;
	
	private NodeLog log;
	
	
	/**
	 * @param messageManager 
	 * Object that listen throug this port
	 */
	public UDPPort( BinaryArrayNotificable messageManager, NodeLog log){
		this.messageReceiver = messageManager;
		this.log = log;
	}
	
	public Address setUp(int port){
		Address myAddress = new Address();
		try {

			if ( port == 0){															//Starts the socket and bind it to any free port
				datagramSocket = new DatagramSocket();
				port = datagramSocket.getLocalPort();
			}else{
				datagramSocket = new DatagramSocket(port);
			}
			myAddress.setPort( port );
			
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
				String ip = addr.getHostAddress();
				if(ip.equals("0.0.0.0") || ip.equals("127.0.1.1")) {					//Modify Address, this is to prevent bugs on diferent opearating systems
					ip = "127.0.0.1";
				}
				myAddress.setIp(ip);
				run = true;
			} catch (UnknownHostException ex) {
				//temporal.error(ex.getMessage());
				ex.printStackTrace();
				return null;
							
			}
        }
        catch (SocketException socketException) {
            //temporal.error(socketException.toString());
        	socketException.printStackTrace();
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
	@Override
	public void run(){
		while(run){
			//Escucha por el mensaje
			try {
				byte datagrama[] = new byte[ 64000 ];
				receiveMessage = new DatagramPacket(datagrama,datagrama.length);
				datagramSocket.receive(receiveMessage);																//Waiting to receive, received message will be storage in receiveMessage
				
			}catch(InterruptedIOException interruptedIOException){
				log.developer(interruptedIOException.getMessage());
				continue;
			}catch( IOException ioException) {
				log.debug(ioException.getMessage());
			}
		
			byte[] messageData = new byte[receiveMessage.getLength()];												//Copy just the received data to an array 
			System.arraycopy(
					receiveMessage.getData(), 0,
					messageData, 0, 
					messageData.length);
			
			
			
			messageReceiver.receive(
					new Address(
							receiveMessage.getAddress().toString().substring(1), 
							receiveMessage.getPort()), 
					messageData);																					//Enviar mensaje a Manejador
		}
		datagramSocket.close();
	}
	
	public boolean send(Address addr, byte[] data) {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(
            		data, data.length,
					InetAddress.getByName(addr.getIp()),addr.getPort());
            datagramSocket.send(datagramPacket);
            return true;
        }
        catch (IOException e) { 
            //temporal.error(e.getMessage());
        	return false;
        }
    }
	
	public void stopListening(){
		run = false;
	}
}