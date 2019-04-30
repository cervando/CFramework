package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class SingInAreaNotificationMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public SingInAreaNotificationMessage(byte[] data){
		super(data);
	}
	
	public SingInAreaNotificationMessage(long nodeID,String ip, int port){
		this.type = OperationCodeConstants.SINGIN_AREA_NOTIFICATION;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						new NodeAddress(nodeID, ip, port).toByteArray()
				);
	}
	
	
	public SingInAreaNotificationMessage( NodeAddress address){
		this.type = OperationCodeConstants.SINGIN_AREA_NOTIFICATION;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						address.toByteArray()
				);
	}
	
	
	
	public long getNodeID(){
		return BinaryHelper.byteToLong(msg, 2);
	}
	
	
	public NodeAddress getAddress() {
		return new NodeAddress(getNodeID(), getIP(), getPort());
	}
	
	
	public String getIP(){
		return BinaryHelper.byteToIP(msg, 6 + 4);
	}
	
	public int getPort(){
		return BinaryHelper.byteToUnsignedShort(msg, 10 +4 );
	}	
}