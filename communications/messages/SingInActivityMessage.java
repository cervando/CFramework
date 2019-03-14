package cFramework.communications.messages;

import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class SingInActivityMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public SingInActivityMessage(byte[] data){
		super(data);
	}
	
	public SingInActivityMessage(long nodeID){
		this.type = OperationCodeConstants.SINGIN_ACTIVITY;
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