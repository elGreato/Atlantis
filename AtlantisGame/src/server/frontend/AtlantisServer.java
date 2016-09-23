package server.frontend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import client.serverconnect.ServerconnectController;
import client.serverconnect.ServerconnectModel;
import client.serverconnect.ServerconnectView;
import javafx.application.Application;
import javafx.stage.Stage;
import server.backend.Lobby;
import server.backend.User;

public class AtlantisServer extends Application {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		launch(args);
		
		/*
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
		*/
	}
	
	 @Override
	 public void start(Stage primaryStage) {
	
		 ServerView view = new ServerView(primaryStage);
		 ServerModel model = new ServerModel(view);
		 ServerController controller = new ServerController(view, model);
		 view.start();
		 
		 
	}

}
