package client.login;

import com.sun.glass.ui.View;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class LoginController {
	private LoginView view;
	private LoginModel model;
	
	public LoginController(LoginView view, LoginModel model)
	{
		this.view = view;
		this.model = model;
		
		//Some cosmetic stuff
		view.createPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e)
			{
				if(view.createPane.isExpanded())
				{
					view.loginPane.setExpanded(false);
				}
				else
				{
					view.createPane.setExpanded(true);
				}
			}
		});
		
		//More cosmetic stuff
		view.loginPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e)
			{
				if(view.loginPane.isExpanded())
				{
					view.createPane.setExpanded(false);
				}
				else
				{
					view.loginPane.setExpanded(true);
				}
			}
		});
		
		//Login to existing account
		view.loginButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				model.processLogin();
			}
		});
		
		//Create new account
		view.createButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				
			}
		});
		
		
		
	}
	
	
	
}
