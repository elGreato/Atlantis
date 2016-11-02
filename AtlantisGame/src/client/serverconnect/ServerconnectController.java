package client.serverconnect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class ServerconnectController {
	
	private ServerconnectView view;
	private ServerconnectModel model;
	
	public ServerconnectController(ServerconnectModel model, ServerconnectView view)
	{
		this.view = view;
		this.model = model;
		
		view.manualConnectButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e)
			{
				model.setUpConnection(view.iptxt.getText());
			}
		});
		view.autoConnectButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e)
			{
				model.scanLAN();
				view.autoConnectButton.setDisable(true);
			}
		});
		view.manualConnect.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				view.manualConnectButton.fire();
				
			}
			
		});
		view.autoConnect.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent event) {
				view.autoConnectButton.fire();
				
			}
			
		});
		
		
	}
	
}
