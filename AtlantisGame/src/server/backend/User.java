package server.backend;

import java.util.ArrayList;

import gameObjects.Game;
import gameObjects.GameInterface;
import messageObjects.Message;

public abstract class User {
	
	UserInfo userInfo;
	ArrayList<GameInterface> runningGames;
	public User()
	{
		runningGames = new ArrayList<GameInterface>();
	}
	public User(UserInfo userInfo) {
		super();
		this.userInfo = userInfo;
		runningGames = new ArrayList<GameInterface>();
	}

	public synchronized void initiateGameStart(GameInterface game)
	{
		runningGames.add(game);
	}
	public synchronized void endGame(Game game)
	{
		runningGames.remove(game);
	}
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo){
		this.userInfo = userInfo;
	}
	public abstract void sendMessage(Message m);
}

