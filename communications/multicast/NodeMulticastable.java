package kmiddle2.communications.multicast;

import kmiddle2.communications.fiels.Address;

public interface NodeMulticastable {
	public int getName();
	public void sendMyInfoToNeighbor(Address neighbor);
	public void singInMulticast(Address adr, int nodeName);
}
