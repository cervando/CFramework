package cFramework.communications.messages;

import cFramework.communications.NodeAddress;
import cFramework.communications.messages.base.Message;
import cFramework.communications.messages.base.OperationCodeConstants;
import cFramework.nodes.NodeConf;
import cFramework.util.BinaryHelper;

public class CreateAreaHelperRequestMessage extends Message {

	
	
	public CreateAreaHelperRequestMessage(byte[] data){
		super(data);
	}
	
	public CreateAreaHelperRequestMessage(long myNodeID,String myIP, int myPort, long idToCreate, NodeConf nodeConf){
		this.type = OperationCodeConstants.CREATE_AREA_HELPER_REQUEST;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						new NodeAddress(myNodeID, myIP, myPort).toByteArray(),
						BinaryHelper.longToByte( idToCreate),
						BinaryHelper.intToByte(	nodeConf.toInt() )
				);
	}
	
	public long getNodeID(){
		return BinaryHelper.byteToLong(msg, 2);
	}
	
	public String getIP(){
		return BinaryHelper.byteToIP(msg, 6 +4);
	}
	
	public int getPort(){
		return BinaryHelper.byteToUnsignedShort(msg, 10 +4);
	}
	
	public long getIDNodetoCreate(){
		return BinaryHelper.byteToLong(msg, 12 +4);
	}
	
	public NodeConf getNodeConfiguration(){
		return new NodeConf(BinaryHelper.byteToInt(msg, 16 + 8));
	}
}
