package kmiddle2.communications;

import kmiddle2.communications.fiels.Address;

public interface BinaryArrayNotificable {
	public void receive(Address address, byte[] message);
}
