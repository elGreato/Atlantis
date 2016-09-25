package client.serverconnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ServerconnectModel {
	
	private ServerconnectView view;
	private Socket socket;
	
	public ServerconnectModel(ServerconnectView view)
	{
		this.view = view;
		
	}
	
	//sets up connection to server
	protected void setUpConnection(String text)
	{
		
		try{
			socket = new Socket(text, 61452);

			
			LoginView loginView = new LoginView();
			LoginModel loginModel = new LoginModel(socket, loginView);
			LoginController loginController = new LoginController(loginView, loginModel);
			loginView.start();
			view.close();
			
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
