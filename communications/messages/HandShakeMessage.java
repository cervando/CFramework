package cFramework.communications.messages;

import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

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
