package cFramework.communications.messages.base;

import cFramework.communications.fiels.NullValueConstants;
import cFramework.communications.messages.CreateAreaHelperRequestMessage;
import cFramework.communications.messages.CreateAreaRequestMessage;
import cFramework.communications.messages.DataMessage;
import cFramework.communications.messages.FindNodeMessage;
import cFramework.communications.messages.HandShakeMessage;
import cFramework.communications.messages.HelloMessage;
import cFramework.communications.messages.IgniteEntityListMessage;
import cFramework.communications.messages.SearchMulticastMessage;
import cFramework.communications.messages.SearchNodeRequestMessage;
import cFramework.communications.messages.SingInActivityMessage;
import cFramework.communications.messages.SingInAreaNotificationMessage;
import cFramework.communications.messages.UpdateMessage;
import cFramework.util.BinaryHelper;
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

//public class Message implements IndexConstants, NullValueConstants, OperationCodeConstants{
public class Message implements NullValueConstants, OperationCodeConstants{
	
	
	protected short type;
	protected byte msg[];
	
	public Message(){}
	
	public Message(byte[] msg){
		this.type = BinaryHelper.byteToShort(msg, 0);
		this.msg = msg;
	}
	
	public short getOperationCode(){
		return this.type;
	}
	
	public byte[] toByteArray(){
		return this.msg;
	}
	
	
	public static Message getMessage(byte[] msg){
		
		short type = BinaryHelper.byteToShort(msg, 0);
		
		if ( type == OperationCodeConstants.SINGIN_AREA_NOTIFICATION ){
			return new SingInAreaNotificationMessage(msg);
		
		}else if ( type == OperationCodeConstants.CREATE_AREA_HELPER_REQUEST){
			return new CreateAreaHelperRequestMessage(msg);
		
		}else if ( type == OperationCodeConstants.CREATE_AREA_REQUEST){
			return new CreateAreaRequestMessage(msg);
		
		}else if ( type == OperationCodeConstants.DATA ) {
			return new DataMessage(msg);

		}else if ( type == OperationCodeConstants.FIND_NODE){
			return new FindNodeMessage(msg);
		
		}else if ( type == OperationCodeConstants.HANDSHAKE){
			return new HandShakeMessage();
		
		}else if ( type == OperationCodeConstants.HELLO){
			return new HelloMessage();
		
		}else if ( type == OperationCodeConstants.IGNITE_ENTITY_LIST){
			return new IgniteEntityListMessage(msg);
		
		}else if ( type == OperationCodeConstants.FIND_NODE){
			return new FindNodeMessage(msg);
		
		}else if ( type == OperationCodeConstants.SEARCH_MULTICAST ) {
			return new SearchMulticastMessage(msg);
		
		}else if ( type == OperationCodeConstants.SEARCH_NODE_REQUEST ) {
			return new SearchNodeRequestMessage(msg);
		
		}else if ( type == OperationCodeConstants.SINGIN_ACTIVITY ) { 
			return new SingInActivityMessage(msg);
		
		}else if ( type == OperationCodeConstants.SINGIN_AREA_NOTIFICATION ){
			return new SingInAreaNotificationMessage(msg);
		
		}else if ( type == OperationCodeConstants.UPDATE ){
			return new UpdateMessage(msg);
		}
		
		return new Message(msg);
	}
	
	
	
	
//	Erased from version 1, This code was distributed on communications.messages package
//	/**
//	 * Constructor que crea un mensaje a partir de una cadena de bytes
//	 * @param msg Arreglo de bytes que constituye el mensaje
//	 */
//	public Message(byte[] msg){
//		type = BinaryHelper.byteToShort(msg, 0);
//		this.msg = msg;
//	}
//	
//	/**
//	 * Esta funcion regresa el codigo de operacion del mensaje
//	 * @return Codigo de operacion
//	 */
//	public short getOperationCode(){
//		return type;
//	}	
//
//	/**
//	 * Devuelbe en nombre que se incluye en el mensaje, esta fucion solo
//	 * es valida para los mensajes de tipo SingInArea, SingInSmallNode y Handshake
//	 * @return Nombre del area incluido en el mensaje
//	 */
//	public int getName(){
//		if ( type == SINGIN_AREA || type == SINGIN_SIBLING || type == SINGIN_CHILD || type == HANDSHAKE || type == SEARCH_NODE_REQUEST || type == SEARCH_MULTICAST || type == FIND_NODE || type == DATA || type == FREE_NODE)
//			return BinaryHelper.byteToInt(msg, 2);
//		return -1;
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	private String getIp(){
//		if ( type == SINGIN_AREA || type == SEARCH_MULTICAST || type == FIND_NODE){
//			return new Address(msg, 6).getIp(); 
//		}
//		return "";
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */
//	private int getPort(){
//		if ( type == SINGIN_AREA || type == SEARCH_MULTICAST || type == FIND_NODE)
//			return new Address(msg, 6).getPort(); 
//		return 0;
//	}
//	
//	/**
//	 * 
//	 * @return
//	 */int
//	public Node getNode(){
//		return new Node(getName(), getIp(), getPort());
//	}
//	
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public int getLookedName(){
//		if ( type == SEARCH_MULTICAST )
//			return BinaryHelper.byteToInt(this.msg, 10);
//		return -1;
//	}
//		
//	
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public short getDataType(){
//		if( type == DATA )
//			return BinaryHelper.byteToShort(msg, 6);
//		return 0;
//	} 
//	
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public byte[] getData() {
//		if( type == DATA )
//			return BinaryHelper.subByteArray(msg, 6, msg.length - 6);
//		return null;
//    }
//	
//	//----------------------------------------------------------------
//	//-------- MESSAGE
//	//----------------------------------------------------------------
//	
//	
//	/**
//	 * 
//	 * @return
//	 */
//	public byte[] getMessage(){
//		return BinaryHelper.mergeByteArrays(BinaryHelper.shortToByte(type), this.msg);
//	}
//	
//	private static byte[] getMessage(short opCode, int areaName ){
//		return  BinaryHelper.mergeByteArrays(
//				BinaryHelper.shortToByte(opCode), 
//				BinaryHelper.intToByte(areaName) 
//	);
//	}
//	
//	
//	/**
//	 * 
//	 * @param opCode
//	 * @param areaName
//	 * @param IP
//	 * @param PORT
//	 * @return
//	 */
//	private static byte[] getMessage(short opCode, int areaName, String IP, int PORT ){
//		return  BinaryHelper.mergeByteArrays(
//					BinaryHelper.shortToByte(opCode), 
//					Node.getNode( new Node(areaName, IP, PORT)) 
//		);
//	}
//	
//	
//	/**
//	 * 
//	 * @param areaName
//	 * @return
//	 */
//	public static byte[] getSearchNodeRequest( int areaName ){
//		return getMessage(SEARCH_NODE_REQUEST, areaName);
//	}
//	
//	public static byte[] getFreeNodeRequest( int areaName ){
//		return getMessage(FREE_NODE, areaName);
//	}
//	int
//	
//	/**
//	 * 
//	 * @param areaName
//	 * @return
//	 */
//	public static byte[] getSearchMulticast( int areaName, Address addr, int lookedNode ){
//		
//		return  BinaryHelper.mergeByteArrays(
//					BinaryHelper.mergeByteArrays(
//						BinaryHelper.shortToByte(SEARCH_MULTICAST), 
//						Node.getNode( new Node(areaName, addr.getIp(), addr.getPort()))
//					),
//					BinaryHelper.intToByte(lookedNode)
//		);
//		
//	}
//	
//	
//	
//	public static byte[] getFindMessage( Node n ){
//		return getMessage(FIND_NODE, n.getName(), n.getAddress().getIp(), n.getAddress().getPort());
//	}
//	
//	/**
//	 * 
//	 * @param areaName
//	 * @param IP
//	 * @param PORT
//	 * @return
//	 */
//	public static byte[] getSingInAreaMessage( int areaName, String IP, int PORT ){
//		return getMessage( SINGIN_AREA , areaName, IP, PORT);
//	}
//	
//	/**
//	 * 
//	 * @param areaName
//	 * @return
//	 */
//	public static byte[] getSingInChildMessage( int areaName ){
//		return getMessage( SINGIN_CHILD , areaName );
//	}
//
//	/**
//	 * 
//	 * @param areaName
//	 * @param IP
//	 * @param PORT
//	 * @return
//	 */
//	public static byte[] getHandShakeMessage( int areaName){
//		return getMessage( HANDSHAKE, areaName);
//	}
//	
//	/**
//	 * @param senderID
//	 * @param dataType
//	 * @param data
//	 * @return
//	 */
//	
//	public static byte[] getDataMessage( int senderID,  short dataType, byte[] data){
//		return BinaryHelper.mergeByteArrays(
//					BinaryHelper.shortToByte( DATA ),
//					BinaryHelper.mergeByteArrays(
//						BinaryHelper.intToByte(senderID),
//						BinaryHelper.mergeByteArrays(
//								BinaryHelper.shortToByte(dataType), 
//								data
//						)
//					)
//		);
//	}
//	
//	
//	
	
	/**
	 ******** Deprecado en la actualizacion 1.1 *********************
	
	public byte[] getSecondMessage(int nodeName){
		byte[] a = copyByteArrays(getShortArray(SECOND_DATA), getIntArray(nodeName));
		byte[] b = copyByteArrays(a, getShortArray(getDataType()));
		return copyByteArrays(b, getData());
	}
		
	public static byte[] getMessage(short opCod){
		return getShortArray(opCod);
	}
	
	public static byte[] getMessage(short opCod,Node node){
		return copyByteArrays(getShortArray(opCod), Node.getNode(node));
	}
	
	public static byte[] getMessage(short opCod,short dataType, byte[] data){
		byte[] a = copyByteArrays(getShortArray(opCod), getShortArray(dataType));
		return copyByteArrays(a, data);
	}
	
	public static byte[] getMessage(short opCod,byte[] data){
		return copyByteArrays(getShortArray(opCod), data);
	}
	
	public static byte[] getMessage(short opCod,int areName,short dataType, byte[] data){
		byte[] a = copyByteArrays(getShortArray(opCod), getIntArray(areName));
		byte[] b = copyByteArrays(a, getShortArray(dataType));
		return copyByteArrays(b, data);
	}
	
	public byte[] getSmallNodeMessage(){ 
		byte[] r = new byte[msg.length-2];
		System.arraycopy(msg, 2, r, 0,msg.length-2) ;
		return r;
	}	
	
	//----------------------------------------------------------------
	//-------- HELP
	//----------------------------------------------------------------
	
	public static byte[] getIntArray(int areaName){
		ByteBuffer buffer = ByteBuffer.allocate(4);
		buffer.putInt(areaName);
		return buffer.array();
	}
	
	public static byte[] getShortArray(short opeCod){
		ByteBuffer buffer = ByteBuffer.allocate(2);
		buffer.putShort(opeCod);
		return buffer.array();  
	}
	
	public int getMissingNodeName(){
		return AreaName.getMissingNode(msg);
	}

	public int getSecondName(){
		return AreaName.getSecondAreaName(msg);
	}
int
	public byte[] getSecondData(){
		return Data.getSecondData(msg);
	}
	
	
	
	public static byte[] copyByteArrays(byte[] a,byte[] b){
		byte[] c = new byte[a.length+b.length];
		
		System.arraycopy(a, 0, c, 0, a.length);
		System.arraycopy(b, 0, c, a.length, b.length);
		return c;
	}
	
	public short getSecondDataType(){
		return DataType.getSecondDataType(msg);
	}
		
	*/

}
