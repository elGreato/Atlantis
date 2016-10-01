package client.lobby;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LobbyController {
	private LobbyView view;
	private LobbyModel model;
	public LobbyController(LobbyView lobbyView, LobbyModel lobbyModel) {
		this.view = view;
		this.model = model;
		
		
		view.createButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				
				
			}
			
		});
		
		view.joinButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				
				
			}
			
		});
	}
	
	

}
