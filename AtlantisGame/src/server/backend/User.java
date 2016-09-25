package server.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messageObjects.CreateUserMessage;
import messageObjects.LoginMessage;

public class User implements Runnable{
	
	private String username;
	private boolean loggedIn;
	
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
		
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			
			loginUser();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	
	
	//get login or create messages from user till user is logged in and will therefore be redirected to lobby.
	private void loginUser()
	{
		loggedIn = false;
		
		while(!loggedIn)
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
				
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}
