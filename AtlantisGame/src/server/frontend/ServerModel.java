package server.frontend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import server.backend.Lobby;
import server.backend.User;

public class ServerModel implements Runnable{
	
	private ServerSocket serverSocket;
	private Lobby lobby = new Lobby();
	private ServerView view;


	//Constructor
	public ServerModel(ServerView view)
	{
		this.view = view;
	}

	
	
	//Creates new thread for listener (JavaFX crashes when doing this without a thread)
	protected void startServer()
	{
		view.serverstartbtn.setDisable(true);
		view.serverstartbtn.setText("Running...");
		Thread listener = new Thread(this);
		listener.start();
	}
	

	
	//Server loop
	@Override
	public void run() {
		
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
			Platform.runLater(new Runnable(){
				public void run()
				{
					view.serverstartbtn.setDisable(false);
					view.serverstartbtn.setText("Start");
					
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Socket error");
					alert.setContentText("An error with the socket occured. Server stopped.");
					alert.showAndWait();
				}
			});
		}

		

	}
}
