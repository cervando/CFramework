package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class SearchMulticastMessage extends Message{

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public SearchMulticastMessage(byte[] data){
		super(data);
	}
	
	public SearchMulticastMessage(long nodeID,String ip, int port, long lookedID){
		this.type = OperationCodeConstants.SEARCH_MULTICAST;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						new NodeAddress(nodeID, ip, port).toByteArray(),
						BinaryHelper.longToByte(lookedID)
				);
	}
	
	public long getNodeID(){
		return BinaryHelper.byteToLong(msg, 2);
	}
	
	public String getIP(){
		return BinaryHelper.byteToIP(msg, 6 + 4);
	}
	
	public int getPort(){
		return BinaryHelper.byteToUnsignedShort(msg, 10 +4);
	}
	
	public long getLookedName(){
		return BinaryHelper.byteToLong(msg, 12 + 4);
	}
	
}
