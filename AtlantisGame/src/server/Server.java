package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket serverSocket;
		Lobby lobby = new Lobby();
		
		try 
		{
			serverSocket = new ServerSocket(61452,10,null);
			
			while(true)
			{
				Socket client = serverSocket.accept();
				User user = new User(client, lobby);
				Thread thread = new Thread(user);
				thread.start();
			}
		} 
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

}
