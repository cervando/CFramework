package cFramework.communications.messages;

import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.nodes.NodeConf;
import cFramework.util.BinaryHelper;

public class IgniteEntityListMessage extends Message {

	/**
	 * This method create the messages based on the incoming data from the connection
	 * @param data
	 */
	public IgniteEntityListMessage(byte[] data){
		super(data);
	}
	
	public IgniteEntityListMessage(NodeConf nodeC,String data){
		this.type = OperationCodeConstants.IGNITE_ENTITY_LIST;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type),
						BinaryHelper.intToByte(nodeC.toInt()),
						data.getBytes()
				);
	}
	
	public NodeConf getNodeConfiguration(){
		return new NodeConf(BinaryHelper.byteToInt(msg, 2));
	}
	
	public String getList() {
		return new String(BinaryHelper.subByteArray(msg, 6, msg.length - 6));
	}
}
