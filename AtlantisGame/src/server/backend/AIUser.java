package server.backend;

import java.util.ArrayList;

import gameObjects.GameInterface;
import messageObjects.InGameMessage;
import messageObjects.Message;
import messageObjects.WaterMessage;

public class AIUser extends User implements Runnable{
	public static String[] aiNames = {"AI_BernGuy", "AI_Bolt","AI_SGASquad","AI_GovOfficials", "AI_TheStandard"};
	private double aiSpeed;
	private double aiPawnSpread;
	private ArrayList<Message> incomingMessages;
	private ArrayList<GameAI> activeGames;
	//Constructor
	public AIUser(UserInfo ui)
	{
		super(ui);
		incomingMessages = new ArrayList<Message>();
		activeGames = new ArrayList<GameAI>();
		switch(ui.getUsername()){
		case "AI_BernGUY" : aiSpeed = -1d; aiPawnSpread = 4d; break; 
		case "AI_Bolt" : aiSpeed = 10d; aiPawnSpread = -5d; break;
		case "AI_SGASquad" : aiSpeed = 4d; aiPawnSpread = 15d; break;
		case "AI_GovOfficials" : aiSpeed = 0.02d; aiPawnSpread = -2d; break;
		default : aiSpeed = 1; aiPawnSpread = 0d;
		}
		Thread t = new Thread(this);
		t.start();
	}
	@Override
	public synchronized void initiateGameStart(GameInterface game)
	{
		activeGames.add(new GameAI(game,aiSpeed,aiPawnSpread));
		super.initiateGameStart(game);
	}
	//Process messages
	private void processMessages()
	{
		while(incomingMessages.size() == 0)
		{
			synchronized(incomingMessages)
			{
				try {
					incomingMessages.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
		synchronized(incomingMessages)
		{
			incomingMessages.add(msg);
			incomingMessages.notify();
		}
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
