package cFramework.communications.messages;

import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class HelloMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */	
	public HelloMessage(){
		this.type = OperationCodeConstants.HELLO;
		this.msg = BinaryHelper.shortToByte(type);
	}
}
