package server.frontend;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ServerController {
	
	private ServerView view;
	private ServerModel model;
	
	public ServerController(ServerView view, ServerModel model)
	{
		this.view = view;
		this.model = model;
		
		//Start server
		view.serverstartbtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				model.startServer();

			}
		});
		

		
		//Connect to database
		view.dbconnectbtn.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				
			}
		});
		
		
		
	}
}
