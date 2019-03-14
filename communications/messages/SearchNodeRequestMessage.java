package cFramework.communications.messages;

import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class SearchNodeRequestMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public SearchNodeRequestMessage(byte[] data){
		super(data);
	}
	
	public SearchNodeRequestMessage(long nodeID){
		this.type = OperationCodeConstants.SEARCH_NODE_REQUEST;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						BinaryHelper.longToByte(nodeID)
				);
	}
		
	public long getNodeID(){
		return BinaryHelper.byteToLong(msg, 2);
	}
}