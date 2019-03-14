package cFramework.communications.messages;

import cFramework.communications.MessageMetadata;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.util.BinaryHelper;

public class DataMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public DataMessage(byte[] data){
		super(data);
	}
	
	public DataMessage(long senderID, long receiverID, MessageMetadata metadata, byte[] msg){
		this.type = OperationCodeConstants.DATA;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type),
						BinaryHelper.longToByte(senderID),
						BinaryHelper.longToByte(receiverID),
						BinaryHelper.MessageMetadataToByte(metadata),
						msg
				);
	}
	
	public long getSenderID(){
		return BinaryHelper.byteToLong(msg, 2);
	}
	
	public long getReceiverID(){
		return BinaryHelper.byteToLong(msg, 6 + 4);
	}
	
	public MessageMetadata getMetaData(){
		return BinaryHelper.byteToMessageMetaData(msg, 10 + 8);
	}
	
	
	public byte[] getData() {
		return BinaryHelper.subByteArray(msg, 18 +BinaryHelper.MessageMetadataBytesLengh, msg.length - (18+BinaryHelper.MessageMetadataBytesLengh));
	}
}
