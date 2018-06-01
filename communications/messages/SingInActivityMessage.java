package kmiddle2.communications.messages;

import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.util.BinaryHelper;

public class SingInActivityMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public SingInActivityMessage(byte[] data){
		super(data);
	}
	
	public SingInActivityMessage(int nodeID){
		this.type = OperationCodeConstants.SINGIN_ACTIVITY;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						BinaryHelper.intToByte(nodeID)
				);
	}
		
	public int getNodeID(){
		return BinaryHelper.byteToInt(msg, 2);
	}
}