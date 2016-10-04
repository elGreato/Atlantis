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
		
		//listener from http://stackoverflow.com/questions/26424769/javafx8-how-to-create-listener-for-selection-of-row-in-tableview
		view.gameList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			
		    if(newSelection != null && newSelection.getHasPassword())
		    {
		    	view.joinPassword.setDisable(false);
		    }
		    else
		    {
		    	view.joinPassword.setDisable(true);
		    	view.joinPassword.setText("");
		    }
		});
		
		view.chatButton.setOnAction(new EventHandler<ActionEvent>()
			{
				public void handle(ActionEvent e)
				{
					model.sendChatMessage();
				}
			});
		
	
	}
	
	

}
