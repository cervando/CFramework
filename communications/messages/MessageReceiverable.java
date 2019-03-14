package cFramework.communications.messages;

import cFramework.communications.messages.base.Message;

public interface MessageReceiverable {
	
	public void receive(Message m);
}
