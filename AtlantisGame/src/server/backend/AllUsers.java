package server.backend;

import java.util.ArrayList;

import gameObjects.Game;
import gameObjects.GameInterface;
import messageObjects.Message;

public abstract class AllUsers {
	
	UserInfo ui;
	ArrayList<GameInterface> runningGames;
	public AllUsers(UserInfo ui) {
		super();
		this.ui = ui;
		runningGames = new ArrayList<GameInterface>();
	}

	public synchronized void initiateGameStart(GameInterface game)
	{
		runningGames.add(game);
	}

	public UserInfo getUi() {
		return ui;
	}
	public abstract void sendMessage(Message m);
}
