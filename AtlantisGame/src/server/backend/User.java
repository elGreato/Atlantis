package server.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import messageObjects.CreateUserMessage;
import messageObjects.LoginMessage;

public class User implements Runnable{
	
	private String username;
	
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
			
			//get login or create message from user
			Object loginOrCreate = ois.readObject();
			
			if(loginOrCreate instanceof LoginMessage)
			{
				
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
