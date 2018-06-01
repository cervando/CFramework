package kmiddle2.communications.messages;

import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.nodes.NodeConf;
import kmiddle2.util.BinaryHelper;

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
