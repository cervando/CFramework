package kmiddle2.communications.p2p.sockets;

import kmiddle2.communications.fiels.Address;

public interface Port extends Runnable{
	
	public Address setUp(int port);
	public void startListening();
	public void stopListening();
	public void send(Address address, byte[] message);
}

