package kmiddle2.nodes;

import kmiddle2.communications.MessageMetadata;
import kmiddle2.communications.Protocol;

public abstract class Node {

	public abstract Protocol getProtocol();
	public abstract void receive(int id, MessageMetadata m, byte[] data);
}
