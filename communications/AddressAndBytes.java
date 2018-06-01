package kmiddle2.communications;

import kmiddle2.communications.fiels.Address;

public class AddressAndBytes {

	private Address address;
	private byte[] bytes;
	
	public Address getAddress() {
		return address;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public AddressAndBytes(Address a, byte[] m){
		this.address = a;
		this.bytes = m;
	}
}
