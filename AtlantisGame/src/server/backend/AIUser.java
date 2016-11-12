package server.backend;

import java.util.ArrayList;

import messageObjects.Message;
import messageObjects.WaterMessage;

public class AIUser extends AllUsers implements Runnable{
	
	private ArrayList<Message> incomingMessages;
	
	//Constructor
	public AIUser(UserInfo ui)
	{
		super(ui);
		incomingMessages = new ArrayList<Message>();
		Thread t = new Thread(this);
		t.start();
	}
	
	//Process messages
	private void processMessages()
	{
		while(incomingMessages.size() == 0)
		{
			try {
				incomingMessages.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		Message msg = incomingMessages.remove(0);
		if (msg instanceof WaterMessage)
		{
			
		}
	}
	//receiving messages
	@Override
	public synchronized void sendMessage(Message msg) {
		incomingMessages.add(msg);
		incomingMessages.notify();
	}

	//AI thread
	@Override
	public void run() {
		while(true)
		{
			processMessages();
		}
		
	}
}
