package server.backend;

import java.util.ArrayList;

import gameObjects.Game;
import gameObjects.GameInterface;
import messageObjects.InGameMessage;
import messageObjects.Message;
import messageObjects.WaterMessage;
/**
* <h1>Representation of an AI user</h1>
* Represents AI in Lobby. Sends InGameMessages to specific AIGame instance.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class AIUser extends User implements Runnable{
	public static String[] aiNames = {"AI_BernCityGuy", "AI_Bolt","AI_SGASquad","AI_GovOfficials", "AI_TheStandard", "AI_EvilGenius"};
	private double aiGreediness;
	private double aiSpeed;
	private double aiTeamSpirit;
	private double aiEvilness;
	private ArrayList<Message> incomingMessages;
	private ArrayList<GameAI> activeGames;
	//Constructor
	public AIUser(UserInfo ui)
	{
		super(ui);
		incomingMessages = new ArrayList<Message>();
		activeGames = new ArrayList<GameAI>();
		switch(ui.getUsername()){
		case "AI_BernCityGuy" :aiGreediness = 5d; aiSpeed = -1d; aiTeamSpirit = 4d; aiEvilness = 0d; break; 
		case "AI_Bolt" : aiGreediness = 1d; aiSpeed = 10d; aiTeamSpirit = -5d; aiEvilness = 0d; break;
		case "AI_SGASquad" : aiGreediness = 3d; aiSpeed = 4d; aiTeamSpirit = 15d; aiEvilness = 0d; break;
		case "AI_GovOfficials" : aiGreediness = 8d; aiSpeed = 0.02d; aiTeamSpirit = -2d; aiEvilness = 5d; break;
		case "AI_EvilGenius" : aiGreediness = 1d; aiSpeed = 8d; aiTeamSpirit = 8d; aiEvilness = 100d; break;
		default : aiGreediness = 1d; aiSpeed = 1d; aiTeamSpirit = 1d; aiEvilness = 1d;
		}
		Thread t = new Thread(this);
		t.start();
	}
	//Initiates a game start this AI is involved in
	@Override
	public synchronized void initiateGameStart(GameInterface game)
	{
		activeGames.add(new GameAI(game,aiGreediness, aiSpeed,aiTeamSpirit,aiEvilness));
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
	//Invoked when a game this ai is involved in ends
	@Override
	public synchronized void endGame(Game game) {
		super.endGame(game);
		for(GameAI g:activeGames)
		{
			if (g.getGameName().equals(game.getName()))
			{
				activeGames.remove(g);
				break;
			}
		}
	}
	
}
