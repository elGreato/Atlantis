package client.lobby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import messageObjects.InGameMessage;
import messageObjects.ServerInfoMessage;

public class LobbyModel implements Runnable{

	Thread listener;
	private LobbyView view;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	
	public LobbyModel(LobbyView lobbyView, ObjectOutputStream oos, ObjectInputStream ois) {
		this.view = view;
		
		listener = new Thread(this);
		listener.start();
	}
	
	
	@Override
	public void run() {
		boolean connected = true;
		while(connected)
		{
			try {
				Object obj = ois.readObject();
				
				if(obj instanceof InGameMessage)
				{
					
				}
				else if(obj instanceof ServerInfoMessage)
				{
					
				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				connected = false;
				
				Platform.runLater(new Runnable(){
					public void run()
					{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection error");
						alert.setContentText("Connection to server lost. Please try to restart the program later.");
						alert.showAndWait();
					}
				});
			}
		}
		
	}

	
}
