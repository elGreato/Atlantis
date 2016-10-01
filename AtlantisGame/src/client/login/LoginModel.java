package client.login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import client.lobby.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import messageObjects.CreateUserMessage;
import messageObjects.ServerInfoMessage;
import messageObjects.LoginMessage;
import messageObjects.UserInfoMessage;

public class LoginModel {
	private Socket socket;
	private LoginView view;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public LoginModel(Socket socket, LoginView view)
	{
		this.socket = socket;
		this.view = view;
		
		try{
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			

		} catch(IOException e){
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected error");
			alert.setContentText("An unexpected error ocurred. Please try to restart the program");
			alert.showAndWait();
				
		}
		
		
	}
	
	//Sends entered login data to server and processes the answer it gets about it
	protected void processLogin()
	{
		try {
			oos.writeObject(new LoginMessage(view.loginusernametxt.getText(), view.loginpasswordtxt.getText()));
			Object reply = ois.readObject();
			if(reply instanceof ServerInfoMessage)
			{
				ServerInfoMessage em = (ServerInfoMessage)reply;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Atlantis server notification");
				alert.setContentText(em.getMessage());
				alert.showAndWait();
				view.loginButton.setDisable(false);
			}
			else if(reply instanceof UserInfoMessage)
			{
				
				UserInfoMessage nowLoggedInAs = (UserInfoMessage)reply;
				LobbyView lobbyView = new LobbyView();
				LobbyModel lobbyModel = new LobbyModel(lobbyView, oos, ois);
				LobbyController lobbyController = new LobbyController(lobbyView, lobbyModel);
				lobbyView.start();
				view.close();
			}
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected error");
			alert.setContentText("An unexpected error ocurred. Please try to restart the program");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//Sends 'create new user' data to server and processes the answer about it
	protected void processNewUser()
	{
		try {
			oos.writeObject(new CreateUserMessage(view.createusernametxt.getText(), view.createpasswordtxt.getText()));
			
			Object reply = ois.readObject();
			
			if(reply instanceof ServerInfoMessage)
			{
				ServerInfoMessage em = (ServerInfoMessage)reply;
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Atlantis server notification");
				alert.setContentText(em.getMessage());
				alert.showAndWait();
				view.createButton.setDisable(false);
			}
			else if(reply instanceof UserInfoMessage)
			{
				UserInfoMessage nowLoggedInAs = (UserInfoMessage)reply;
				
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Atlantis server notification");
				alert.setContentText("Account '" + nowLoggedInAs.getUsername() + "' successfully created.");
				alert.showAndWait();
				
				LobbyView lobbyView = new LobbyView();
				LobbyModel lobbyModel = new LobbyModel(lobbyView, oos, ois);
				LobbyController lobbyController = new LobbyController(lobbyView, lobbyModel);
				lobbyView.start();
				view.close();
				
			}
			
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected error");
			alert.setContentText("An unexpected error ocurred. Please try to restart the program");
			alert.showAndWait();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
