package server.backend;

import java.util.ArrayList;

import gameObjects.Game;
import gameObjects.GameInterface;
import messageObjects.Message;
/**
* <h1>Superclass for Users of game</h1>
* A user can be either a human connected through a client or an AI running directly on a server.
* This class contains functionality both of these types of users need.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
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

