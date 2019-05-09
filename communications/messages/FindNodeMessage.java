package cFramework.communications.messages;

import java.util.ArrayList;

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
	
	public FindNodeMessage(ArrayList<NodeAddress> a){
		this.type = OperationCodeConstants.FIND_NODE;
		this.msg = 
			BinaryHelper.mergeByteArrays(
				BinaryHelper.shortToByte(type), 
				BinaryHelper.intToByte(a.size())
			);
		for ( int i = 0; i< a.size(); i++) {
			this.msg = 
				BinaryHelper.mergeByteArrays(
					this.msg, 
					a.get(i).toByteArray()
				);
		}
	}
	
	public ArrayList<NodeAddress> getNodes(){
		ArrayList<NodeAddress> r = new ArrayList<NodeAddress>();
		int size = BinaryHelper.byteToInt(msg, 2);
		for ( int i =0 ; i< size; i++) {
			r.add(new NodeAddress(
					BinaryHelper.byteToLong(msg, 6 + (14 * i) ), 
					BinaryHelper.byteToIP(msg, 6 + 8 + (14 * i) ),
					BinaryHelper.byteToUnsignedShort(msg, 14+4 + (14 * i))
				)
			);
		}
		return r;
	}
	
}