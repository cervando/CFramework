package kmiddle2.communications.messages;

import kmiddle2.communications.messages.base.Message;

public interface MessageReceiverable {
	
	public void receive(Message m);
}
