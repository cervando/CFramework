package kmiddle2.communications.messages;

import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.util.BinaryHelper;

public class HandShakeMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	
	public HandShakeMessage(){
		this.type = OperationCodeConstants.HANDSHAKE;
		this.msg = BinaryHelper.shortToByte(type);
	}
}
