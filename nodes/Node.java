package cFramework.nodes;

import cFramework.communications.MessageMetadata;
import cFramework.communications.Protocol;

public abstract class Node {

	public abstract Protocol getProtocol();
	public abstract void receive(long id, MessageMetadata m, byte[] data);
}
