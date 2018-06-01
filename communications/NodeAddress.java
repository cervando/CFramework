package kmiddle2.communications;

import kmiddle2.communications.fiels.Address;
import kmiddle2.communications.fiels.NullValueConstants;
import kmiddle2.util.BinaryHelper;


public class NodeAddress implements NullValueConstants {
    protected int name = NULL_INT;
	protected Address address;
    protected int bits=0;
	
	public NodeAddress(){
		
	}

    public NodeAddress(int name) {
        this.name = name;
		address = new Address();
        bits+= (this.name==NULL_INT)?0:4;
    }

	public NodeAddress(int name,String host,int port) {
        this.name = name;
        address = new Address(host, port);
        bits+= (this.name==NULL_INT)?0:4;
        bits+= address.isNullIp()?0:2;
		bits+= address.isNullPort()?0:1;
    }
	
	public NodeAddress(int name,Address a) {
        this.name = name;
        address = a;
        bits+= (this.name==NULL_INT)?0:4;
        bits+= address.isNullIp()?0:2;
		bits+= address.isNullPort()?0:1;
    }

    public int getName() {
        return name;
    }

    public String getHost() {
        return address.getIp();
    }

    public int getPort() {
        return address.getPort();
    }

    public int getBits() {
        return bits;
    }

	public Address getAddress(){
		return address;
	}
	
    @Override
    public boolean equals(Object obj) {
		if(obj.getClass()==NodeAddress.class){
			NodeAddress node = (NodeAddress)obj;
			switch (Math.min(getBits(), node.getBits())) {
				case 1: return node.getPort()==getPort();
				case 2: return node.getHost().compareTo(getHost())==0;
				case 3: return node.getHost().compareTo(getHost())==0&&node.getPort()==getPort();
				case 4: return node.getName()==name;
				case 5: return node.getName()==name&& node.getPort()==getPort();
				case 6: return node.getName()==name&& node.getHost().compareTo(getHost())==0;
				case 7: return node.getName()==name&& node.getHost().compareTo(getHost())==0&&
								node.getPort()==getPort(); 
				default: return false;
			}
		}
		return false;
    }

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 47 * hash + this.name;
		hash = 47 * hash + (this.address != null ? this.address.hashCode() : 0);
		hash = 47 * hash + this.bits;
		return hash;
	}

	public byte[] toByteArray(){
		return BinaryHelper.mergeByteArrays(
				BinaryHelper.intToByte(this.getName()),
				this.getAddress().toByteArray()
		);
	}

	public static byte[] getNode(NodeAddress n){
		return BinaryHelper.mergeByteArrays(
				BinaryHelper.intToByte(n.getName()),
				n.getAddress().toByteArray()
		);
	}

    @Override
    public String toString() {
        return ""+name+address;
    }

    public static String toString(String tNodeName,String tHost,int tPort) {
        return tNodeName+tHost+LIMIT+tPort+LIMIT;
    }
	
	public boolean isNull(){
		return bits==0;
	}

	public void setName(int name){
		this.name = name;		
	}
	
}