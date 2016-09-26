package client.login;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import messageObjects.CreateUserMessage;
import messageObjects.ErrorMessage;
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
	
	protected void processLogin()
	{
		try {
			oos.writeObject(new LoginMessage(view.loginusernametxt.getText(), view.loginpasswordtxt.getText()));
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected error");
			alert.setContentText("An unexpected error ocurred. Please try to restart the program");
			alert.showAndWait();
		}
	}
	
	protected void processNewUser()
	{
		try {
			oos.writeObject(new CreateUserMessage(view.createusernametxt.getText(), view.createpasswordtxt.getText()));
			Object reply = ois.readObject();
			if(reply instanceof ErrorMessage)
			{
				ErrorMessage em = (ErrorMessage)reply;
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
