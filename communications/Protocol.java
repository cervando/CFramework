package cFramework.communications;

import cFramework.communications.p2p.P2PCommunications;

public interface Protocol extends BinaryArrayNotificable{

	public P2PCommunications getCommunications();
	public NodeAddress getNodeAddress();
}
