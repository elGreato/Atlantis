package server.frontend;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;

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
		
		//Closes java application and stops all threads.
		view.stage.setOnCloseRequest(new EventHandler<WindowEvent>(){
			public void handle(WindowEvent e)
			{
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Close Request");
				alert.setContentText("Do you really want to close the server application? Players who are currently playing will not be able to continue.");
				Optional<ButtonType> selection = alert.showAndWait();
				
				if(selection.get() == ButtonType.OK )
				{
					System.exit(0);
				}
				else
				{
					e.consume();
				}
			}
		});
		
	}
}
