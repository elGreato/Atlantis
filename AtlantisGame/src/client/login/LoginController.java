package client.login;

import com.sun.glass.ui.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
	}
	
	
	
}
