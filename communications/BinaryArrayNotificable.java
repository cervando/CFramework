package cFramework.communications;

import cFramework.communications.fiels.Address;

public interface BinaryArrayNotificable {
	public void receive(Address address, byte[] message);
}
