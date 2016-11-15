package server.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import gameObjects.GameInterface;
import messageObjects.CreateGameMessage;
import messageObjects.CreateUserMessage;
import messageObjects.ServerInfoMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameStartMessage;
import messageObjects.InGameMessage;
import messageObjects.LobbyChatMessage;
import messageObjects.LoginMessage;
import messageObjects.Message;
import messageObjects.UserInfoMessage;

public class HumanUser extends User implements Runnable{
	
	private boolean loggedIn;
	private boolean connected;
	
	private LobbyInterface lobbyInterface;
	
	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	//Required Getters/Setters
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	//Get information about objects from server
	public HumanUser(Socket client, LobbyInterface lobbyInterface)
	{
		super();
		this.client = client;
		this.lobbyInterface = lobbyInterface;
	}
	
	
	//Executed after user specific thread has been created by server loop
	public void run()
	{
		connected = true;
		
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			
			processLoginOrCreateMessages();
			processLobbyAndGameActivity();
			
		} catch (IOException e) {
			connected = false;
			e.printStackTrace();
		} 
	}
	
	



	//get login or create messages from user till user is logged in and will therefore be redirected to lobby.
	private void processLoginOrCreateMessages()
	{
		loggedIn = false;
		
		while(!loggedIn && connected)
		{
			try{
				
				Object loginOrCreate = ois.readObject();
		
				if(loginOrCreate instanceof LoginMessage)
				{
					loginUser(loginOrCreate);
				}
				else if(loginOrCreate instanceof CreateUserMessage)
				{
					createUser(loginOrCreate);
				}
				
			} catch (IOException e) {
				connected = false;
				e.printStackTrace();
				
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	
	//Send data to lobby to check availability of username and inform user about result
	private void createUser(Object loginOrCreate) throws IOException{
		
		CreateUserMessage createMessage = (CreateUserMessage)loginOrCreate;
		userInfo = lobbyInterface.createNewUser(createMessage.getUsername(), createMessage.getPassword(), this);
		if(userInfo != null)
		{
			//inform user about successful create and login, send user information at the same time
			UserInfoMessage nowLoggedInAs = new UserInfoMessage(userInfo, lobbyInterface.getPosition(userInfo));
			oos.writeObject(nowLoggedInAs);
			oos.flush();
			lobbyInterface.sendWholeLobby(this);
			loggedIn = true;
		}
		else
		{
			//inform user about username already taken
			ServerInfoMessage usernameTaken = new ServerInfoMessage("This username is already taken. Please try another one.");
			oos.writeObject(usernameTaken);
			oos.flush();
		}
	}

	
	//Send data to lobby to check password and inform user about result
	private void loginUser(Object loginOrCreate) throws IOException {
		
		LoginMessage loginMessage = (LoginMessage)loginOrCreate;
		userInfo = lobbyInterface.loginUser(loginMessage.getUsername(), loginMessage.getPassword(), this);
		
		if(userInfo != null)
		{
			//inform user about successful login, send user information at the same time
			UserInfoMessage nowLoggedInAs = new UserInfoMessage(userInfo, lobbyInterface.getPosition(userInfo));
			oos.writeObject(nowLoggedInAs);
			oos.flush();
			lobbyInterface.sendWholeLobby(this);
			loggedIn = true;
		}
		else
		{
			//inform user about wrong credentials entered
			ServerInfoMessage wrongEntry = new ServerInfoMessage("The credentials you entered are not correct. Try again!");
			oos.writeObject(wrongEntry);
			oos.flush();
		}
		
	}
	
	//Loop for receiving messages from the lobby and from the games that the user currently plays
	private void processLobbyAndGameActivity() {

		runningGames = new ArrayList<GameInterface>();
		
		while(loggedIn && connected)
		{
			try {
				Object receivedMessage = ois.readUnshared();
				if(receivedMessage instanceof InGameMessage)
				{
					InGameMessage igm = (InGameMessage)receivedMessage;
					for(GameInterface g : runningGames)
					{
						if(igm.getGameName().equals(g.getName()))
						{
							g.processMessage(igm);
							break;
						}
					}
				}
				else if(receivedMessage instanceof GameJoinMessage)
				{
					GameJoinMessage gjm = (GameJoinMessage)receivedMessage;
					String answer = lobbyInterface.joinGame(this, gjm);
					oos.writeObject(new ServerInfoMessage(answer));
					oos.flush();
				}
				else if(receivedMessage instanceof CreateGameMessage)
				{
					CreateGameMessage cgm = (CreateGameMessage)receivedMessage;
					String answer = lobbyInterface.createGame(this, cgm);
					oos.writeObject(new ServerInfoMessage(answer));
					oos.flush();
				
				}
				else if(receivedMessage instanceof LobbyChatMessage)
				{
					LobbyChatMessage chatMessage = (LobbyChatMessage)receivedMessage;
					lobbyInterface.processChatMessage(chatMessage);
				}
				
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				logoutUser();
				e.printStackTrace();
			}
		}
		
		//ADD: logout user
	}

	public synchronized void sendMessage(Message m) {
		try {
			oos.writeUnshared(m);
			oos.flush();
		} catch (IOException e) {
			logoutUser();
		}
		
	}
	
	public synchronized void initiateGameStart(GameInterface game)
	{
		runningGames.add(game);
		try {
			oos.writeObject(new GameStartMessage(game.getName()));
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
			logoutUser();
		}
	}
	private synchronized void logoutUser()
	{
		loggedIn = false;
		connected = false;
		lobbyInterface.logoutFromOnlineUsers(this);
	}
}