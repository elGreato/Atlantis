package server.backend;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class User implements Runnable{
	
	private Socket client;
	private Lobby lobby;
	
	public User(Socket client, Lobby lobby)
	{
		this.client = client;
		this.lobby = lobby;
	}
	public void run()
	{
		ObjectInputStream ois;
		ObjectOutputStream oos;
		try {
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
