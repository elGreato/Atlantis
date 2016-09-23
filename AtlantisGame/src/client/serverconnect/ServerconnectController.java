package client.serverconnect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ServerconnectController {
	
	private ServerconnectView view;
	private ServerconnectModel model;
	
	public ServerconnectController(ServerconnectModel model, ServerconnectView view)
	{
		this.view = view;
		this.model = model;
		
		view.okButton.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent e)
			{
				model.setUpConnection(view.iptxt.getText());
			}
		});
		
	}
	
}
