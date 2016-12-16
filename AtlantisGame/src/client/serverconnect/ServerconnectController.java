package client.serverconnect;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
/**
* <h1>Handles user input of serverconnect window</h1>
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
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
				view.autoConnectButton.setDisable(true);
				view.manualConnectButton.setDisable(true);
				model.setUpManualConnection(view.iptxt.getText());
				
			}
		});
		view.autoConnectButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e)
			{
				view.autoConnectButton.setDisable(true);
				view.manualConnectButton.setDisable(true);
				model.scanLAN();
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
		view.manualConnect.expandedProperty().addListener((observable, oldValue, newValue) ->
		{
			if(oldValue)
			{
				view.autoConnect.setExpanded(true);
				
			}
			else
			{
				view.autoConnect.setExpanded(false);
			}
		});
		
		//More cosmetic stuff
		view.autoConnect.expandedProperty().addListener((observable, oldValue, newValue)->
		{
			if(oldValue)
			{
				view.manualConnect.setExpanded(true);
			}
			else
			{
				view.manualConnect.setExpanded(false);
			}
		});
		
	}
	
}
