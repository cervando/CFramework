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


package kmiddle2.communications.multicast;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import kmiddle2.communications.Protocol;
import kmiddle2.communications.fiels.Address;
import kmiddle2.log.NodeLog;
import kmiddle2.nodes.NodeConf;

public class Multicast extends Thread{
	
	private MulticastSocket socket;					
	
	private Protocol receiver;						/* Object to send the received messages */
	
	private InetAddress group;						/* Multicast group Address */
	
	private NodeLog logger;				
	
	private Address address;
	
	private NodeConf nc;
	
	/**
	 * Contructor de la clase		
	 * @param receiver 	Objeto que se encargara de procesar los mensajes
	 * @param temporal	Log
	 */
	public Multicast(Protocol receiver, NodeConf nc, NodeLog temporal){
		this.receiver = receiver; 
		this.logger = temporal;
		this.nc = nc;
	}
	
	public void setUp(){
		try{
			address = new Address("228.5.6.7", 6789 + nc.getEntityID());
			group = InetAddress.getByName(address.getIp()); 
			socket = new MulticastSocket(address.getPort());
			start();
			socket.joinGroup(group); 
		} catch (IOException ex) {
		   logger.error(ex.getMessage());
		}
	}
	
	
	/**
	 * Deja el grupo multicast
	 */
	public void leaveGroup(){ 
		try {
			socket.leaveGroup(group);
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
	
	
	public void run(){
		DatagramPacket input = null;
		while (true) {
			byte[] buffer = null;
			try {
				buffer = new byte[1024];
				input = new DatagramPacket(buffer, buffer.length);
				socket.receive(input); 									
			} catch (IOException ex) {
				logger.error(ex.getMessage());
			}
			receiver.receive(null, buffer);
		}
	}
		
	/**
	 * Envia informacion al grupo multicast
	 * @param m Cadena binaria a ser enviada
	 */
	public void send(byte[] m){
		try {
			logger.developer("Send data to multicast group");
			DatagramPacket output = new DatagramPacket(m, m.length,group,address.getPort());
			socket.send(output);
		} catch (IOException ex) {
			logger.error(ex.getMessage());
		}
	}
}
