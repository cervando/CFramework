package kmiddle2.communications.messages;

import kmiddle2.communications.MessageMetadata;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.util.BinaryHelper;

public class DataMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public DataMessage(byte[] data){
		super(data);
	}
	
	public DataMessage(int senderID, int receiverID, MessageMetadata metadata, byte[] msg){
		this.type = OperationCodeConstants.DATA;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type),
						BinaryHelper.intToByte(senderID),
						BinaryHelper.intToByte(receiverID),
						BinaryHelper.MessageMetadataToByte(metadata),
						msg
				);
	}
	
	public int getSenderID(){
		return BinaryHelper.byteToInt(msg, 2);
	}
	
	public int getReceiverID(){
		return BinaryHelper.byteToInt(msg, 6);
	}
	
	public MessageMetadata getMetaData(){
		return BinaryHelper.byteToMessageMetaData(msg, 10);
	}
	
	
	public byte[] getData() {
		return BinaryHelper.subByteArray(msg, 10+BinaryHelper.MessageMetadataBytesLengh, msg.length - (10+BinaryHelper.MessageMetadataBytesLengh));
	}
}
