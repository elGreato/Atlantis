package server.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messageObjects.CreateUserMessage;
import messageObjects.ErrorMessage;
import messageObjects.LoginMessage;
import messageObjects.UserInfoMessage;

public class User implements Runnable{
	
	private UserInfo userInfo;
	
	private boolean loggedIn;
	private boolean connected;
	
	private Lobby lobby;
	
	private Socket client;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
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
			
			loginUser();
			
			
		} catch (IOException e) {
			connected = false;
			e.printStackTrace();
		} 
	}
	
	
	
	//get login or create messages from user till user is logged in and will therefore be redirected to lobby.
	private void loginUser()
	{
		loggedIn = false;
		
		while(!loggedIn && connected)
		{
			try{
				Object loginOrCreate = ois.readObject();
		
				if(loginOrCreate instanceof LoginMessage)
				{
					LoginMessage loginMessage = (LoginMessage)loginOrCreate;
					System.out.println(loginMessage.getUsername() + loginMessage.getPassword());
				}
				else if(loginOrCreate instanceof CreateUserMessage)
				{
					CreateUserMessage createMessage = (CreateUserMessage)loginOrCreate;
					userInfo = lobby.createNewUser(createMessage.getUsername(), createMessage.getPassword());
					if(userInfo != null)
					{
						System.out.println("UserCreated");
						//inform user about successful create and login, send user information at the same time
						UserInfoMessage nowLoggedInAs = new UserInfoMessage(userInfo.getUsername(), userInfo.getGamesPlayed(), userInfo.getGamesWon(), userInfo.getGamesLost());
						oos.writeObject(nowLoggedInAs);
						loggedIn = true;
					}
					else
					{
						//inform user about username already taken
						ErrorMessage usernameTaken = new ErrorMessage("This username is already taken. Please try another one.");
						oos.writeObject(usernameTaken);
					}
					System.out.println(createMessage.getUsername() + createMessage.getPassword());
					
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
}
