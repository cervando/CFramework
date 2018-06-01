package kmiddle2.communications.messages;

import kmiddle2.communications.NodeAddress;
import kmiddle2.communications.messages.base.Message;
import kmiddle2.communications.messages.base.OperationCodeConstants;
import kmiddle2.nodes.NodeConf;
import kmiddle2.util.BinaryHelper;

public class CreateAreaRequestMessage extends Message {

	
	
	public CreateAreaRequestMessage(byte[] data){
		super(data);
	}
	
	public CreateAreaRequestMessage(int myNodeID,String myIP, int myPort, int idToCreate, NodeConf nodeConf){
		this.type = OperationCodeConstants.CREATE_AREA_REQUEST;
		this.msg = 
				BinaryHelper.mergeByteArrays(
						BinaryHelper.shortToByte(type), 
						new NodeAddress(myNodeID, myIP, myPort).toByteArray(),
						BinaryHelper.intToByte( idToCreate),
						BinaryHelper.intToByte(	nodeConf.toInt() )
				);
	}
	
	public int getNodeID(){
		return BinaryHelper.byteToInt(msg, 2);
	}
	
	public String getIP(){
		return BinaryHelper.byteToIP(msg, 6);
	}
	
	public int getPort(){
		return BinaryHelper.byteToUnsignedShort(msg, 10);
	}
	
	public int getIDNodetoCreate(){
		return BinaryHelper.byteToInt(msg, 12);
	}
	
	public NodeConf getNodeConfiguration(){
		return new NodeConf(BinaryHelper.byteToInt(msg, 16));
	}
}
