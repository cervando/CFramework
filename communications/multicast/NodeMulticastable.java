package cFramework.communications.multicast;

import cFramework.communications.fiels.Address;

public interface NodeMulticastable {
	public int getName();
	public void sendMyInfoToNeighbor(Address neighbor);
	public void singInMulticast(Address adr, int nodeName);
}
