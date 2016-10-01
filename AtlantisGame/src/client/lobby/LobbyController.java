package client.lobby;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class LobbyController {
	private LobbyView view;
	private LobbyModel model;
	public LobbyController(LobbyView view, LobbyModel model) {
		this.view = view;
		this.model = model;
		
		
		view.createButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				
				model.createGame();
			}
			
		});
		
		view.joinButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				
				model.joinGame();
			}
			
		});
	}
	
	

}
