package server.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import gameObjects.Game;
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

public class User implements Runnable{
	
	private UserInfo userInfo;

	private boolean loggedIn;
	private boolean connected;
	
	private Lobby lobby;
	
	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	private ArrayList<Game> runningGames;
	
	//Required Getters/Setters
	public UserInfo getUserInfo() {
		return userInfo;
	}
	
	//Get information about objects from server
	public User(Socket client, Lobby lobby)
	{
		this.client = client;
		this.lobby = lobby;
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
		userInfo = lobby.createNewUser(createMessage.getUsername(), createMessage.getPassword(), this);
		if(userInfo != null)
		{
			//inform user about successful create and login, send user information at the same time
			UserInfoMessage nowLoggedInAs = new UserInfoMessage(userInfo, lobby.getPosition(userInfo));
			oos.writeObject(nowLoggedInAs);
			lobby.sendWholeLobby(this);
			loggedIn = true;
		}
		else
		{
			//inform user about username already taken
			ServerInfoMessage usernameTaken = new ServerInfoMessage("This username is already taken. Please try another one.");
			oos.writeObject(usernameTaken);
		}
	}

	
	//Send data to lobby to check password and inform user about result
	private void loginUser(Object loginOrCreate) throws IOException {
		
		LoginMessage loginMessage = (LoginMessage)loginOrCreate;
		userInfo = lobby.loginUser(loginMessage.getUsername(), loginMessage.getPassword(), this);
		
		if(userInfo != null)
		{
			//inform user about successful login, send user information at the same time
			UserInfoMessage nowLoggedInAs = new UserInfoMessage(userInfo, lobby.getPosition(userInfo));
			oos.writeObject(nowLoggedInAs);
			lobby.sendWholeLobby(this);
			loggedIn = true;
		}
		else
		{
			//inform user about wrong credentials entered
			ServerInfoMessage wrongEntry = new ServerInfoMessage("The credentials you entered are not correct. Try again!");
			oos.writeObject(wrongEntry);
		}
		
	}
	
	//Loop for receiving messages from the lobby and from the games that the user currently plays
	private void processLobbyAndGameActivity() {

		runningGames = new ArrayList<Game>();
		
		while(loggedIn && connected)
		{
			try {
				Object receivedMessage = ois.readObject();
				if(receivedMessage instanceof InGameMessage)
				{
					InGameMessage igm = (InGameMessage)receivedMessage;
					for(Game g : runningGames)
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
					String answer = lobby.joinGame(this, gjm);
					oos.writeObject(new ServerInfoMessage(answer));
				}
				else if(receivedMessage instanceof CreateGameMessage)
				{
					System.out.println("create game message received");
					CreateGameMessage cgm = (CreateGameMessage)receivedMessage;
					String answer = lobby.createGame(this, cgm);
					System.out.println("Create game message processed");
					oos.writeObject(new ServerInfoMessage(answer));
					System.out.println("Answer sent");
				
				}
				else if(receivedMessage instanceof LobbyChatMessage)
				{
					LobbyChatMessage chatMessage = (LobbyChatMessage)receivedMessage;
					lobby.processChatMessage(chatMessage);
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
			oos.writeObject(m);
		} catch (IOException e) {
			logoutUser();
		}
		
	}
	
	public synchronized void initiateGameStart(Game game)
	{
		runningGames.add(game);
		try {
			oos.writeObject(new GameStartMessage(game.getName()));
		} catch (IOException e) {
			e.printStackTrace();
			logoutUser();
		}
	}
	private synchronized void logoutUser()
	{
		loggedIn = false;
		connected = false;
		lobby.logoutFromOnlineUsers(this);
	}

	protected void endGame(Game game) {
		runningGames.remove(game);
		
	}
}
