package server.backend;

import java.util.ArrayList;

import gameObjects.GameInterface;
import messageObjects.InGameMessage;
import messageObjects.Message;
import messageObjects.WaterMessage;

public class AIUser extends AllUsers implements Runnable{
	
	private ArrayList<Message> incomingMessages;
	private ArrayList<GameAI> activeGames;
	//Constructor
	public AIUser(UserInfo ui)
	{
		super(ui);
		incomingMessages = new ArrayList<Message>();
		activeGames = new ArrayList<GameAI>();
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public synchronized void initiateGameStart(GameInterface game)
	{
		activeGames.add(new GameAI(game));
		super.initiateGameStart(game);
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
		
		Message receivedMessage = incomingMessages.remove(0);
		if (receivedMessage instanceof InGameMessage)
		{
			InGameMessage igm = (InGameMessage)receivedMessage;
			for(GameAI aig : activeGames)
			{
				if(igm.getGameName().equals(aig.getGameName()))
				{
					aig.processMessage(igm);
					break;
				}
			}
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
