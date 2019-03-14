package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class FindNodeMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public FindNodeMessage(byte[] data){
		super(data);
	}
	
	public FindNodeMessage(long nodeID,String ip, int port){
		this.type = OperationCodeConstants.FIND_NODE;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						new NodeAddress(nodeID, ip, port).toByteArray()
				);
	}
		
	public long getNodeID(){
		return BinaryHelper.byteToLong(msg, 2);
	}
	
	public String getIP(){
		return BinaryHelper.byteToIP(msg, 6+4);
	}
	
	public int getPort(){
		return BinaryHelper.byteToUnsignedShort(msg, 10+4);
	}
}