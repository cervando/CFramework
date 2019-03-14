package cFramework.communications.p2p.sockets;

import cFramework.communications.fiels.Address;

public interface Port extends Runnable{
	
	public Address setUp(int port);
	public void startListening();
	public void stopListening();
	public boolean send(Address address, byte[] message);
}

