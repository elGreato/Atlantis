package client.serverconnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ServerconnectModel {
	
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	
	//sets up connection to server
	protected void setUpConnection(String text)
	{
		
		try{
			Socket socket = new Socket(text, 61452);
			oos = new ObjectOutputStream(socket.getOutputStream());
		}
		catch(IOException e)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Connection error");
			alert.setContentText("IP adress wrong. Could not connect to server.");
			alert.showAndWait();
		}
	}
}
