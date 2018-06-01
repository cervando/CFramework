package kmiddle2.communications.messages;

import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.util.BinaryHelper;

public class SingInAreaNotificationMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public SingInAreaNotificationMessage(byte[] data){
		super(data);
	}
	
	public SingInAreaNotificationMessage(int nodeID,String ip, int port){
		this.type = OperationCodeConstants.SINGIN_AREA_NOTIFICATION;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						new NodeAddress(nodeID, ip, port).toByteArray()
				);
	}
	
	public int getNodeID(){
		return BinaryHelper.byteToInt(msg, 2);
	}
	
	public String getIP(){
		return BinaryHelper.byteToIP(msg, 6);
	}
	
	public int getPort(){
		return BinaryHelper.byteToUnsignedShort(msg, 10);
	}	
}