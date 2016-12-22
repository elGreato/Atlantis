package client.login;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
* <h1>Handles user input from login screen</h1>
* Controller for login screen
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class LoginController {
	private LoginView view;
	private LoginModel model;
	private int maxLength;
	
	public LoginController(LoginView view, LoginModel model)
	{
		maxLength = 15;
		this.view = view;
		this.model = model;
		
		//Some cosmetic stuff
		view.createPane.expandedProperty().addListener((observable, oldValue, newValue) ->
		{
			if(oldValue)
			{
				view.loginPane.setExpanded(true);
				
			}
			else
			{
				view.loginPane.setExpanded(false);
			}
		});
		
		//More cosmetic stuff
		view.loginPane.expandedProperty().addListener((observable, oldValue, newValue)->
		{
			if(oldValue)
			{
				view.createPane.setExpanded(true);
			}
			else
			{
				view.createPane.setExpanded(false);
			}
		});
		
		//Login to existing account
		view.loginButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				view.loginButton.setDisable(true);
				model.processLogin();
			}
		});
		
		//Create new account
		view.createButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				if(view.createusernametxt.getText().equals(""))
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("No username entered");
					alert.setContentText("Please enter your desired username");
					alert.showAndWait();
				}
				else if(view.createusernametxt.getText().contains(" "))
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Illegal username");
					alert.setContentText("Please enter a username that does not contain any spaces");
					alert.showAndWait();
				}
				else if(view.createusernametxt.getText().substring(0,3).equals("AI_"))
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Illegal username");
					alert.setContentText("It is illegal to start the username with AI_, because other users might think that you are, in fact, an AI.");
					alert.showAndWait();
				}
				else if(view.createpasswordtxt.getText().length() < 5)
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Illegal information");
					alert.setContentText("Please enter a password that has between 5 and 15 letters.");
					alert.showAndWait();
				}
				else
				{
					view.createButton.setDisable(true);
					model.processNewUser();
				}
			}
		});
		
		//consume entries who would be too long for database
		
		view.createusernametxt.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue.length() > 15)
		    {
		    	String s = newValue.substring(0,15);
		    	view.createusernametxt.setText(s);
		    	
		    }
		});
		view.createpasswordtxt.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue.length() > 15)
		    {
		    	String s = newValue.substring(0,15);
		    	view.createpasswordtxt.setText(s);
		    	
		    	Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Password information");
				alert.setContentText("Please enter a password that has between 5 and 15 letters.");
				alert.showAndWait();
		    	
		    }
		});
		view.loginusernametxt.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue.length() > 15)
		    {
		    	String s = newValue.substring(0,15);
		    	view.loginusernametxt.setText(s);
		    	
		    }
		});
		view.loginpasswordtxt.textProperty().addListener((observable, oldValue, newValue) -> {
		    if(newValue.length() > 15)
		    {
		    	String s = newValue.substring(0,15);
		    	view.loginpasswordtxt.setText(s);
		    	
		    }
		});
		view.createPane.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent k) {
				if(k.getCode().equals(KeyCode.ENTER))
				{
					view.createButton.fire();
				}
				
			}
			
		});
		view.loginPane.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent k) {
				if(k.getCode().equals(KeyCode.ENTER))
				{
					view.loginButton.fire();
				}
				
			}
			
		});
		
	}
	
	
	
}
