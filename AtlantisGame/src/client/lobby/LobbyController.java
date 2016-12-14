package client.lobby;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class LobbyController {
	private LobbyView view;
	private LobbyModel model;
	public LobbyController(LobbyView view, LobbyModel model) {
		this.view = view;
		this.model = model;
		
		view.createNumPlayerscbx.valueProperty().addListener((observable, oldValue, newValue) ->
		{
			if(oldValue<newValue)
			{
				for(int i = oldValue; i< newValue;i++)
				{
					view.createNumAIPlayerscbx.getItems().add(i);
				}
			}
			else if(oldValue > newValue)
			{
				for(int i = oldValue-1 ; i>= newValue;i--)
				{
					view.createNumAIPlayerscbx.getItems().remove((Integer)i);
				}
			}
			view.createNumAIPlayerscbx.getItems();
		});
		view.createButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				if(view.createGameNametxt.getText().equals(""))
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("No game name entered");
					alert.setContentText("Please enter a game name");
					alert.showAndWait();
				}
				else if(view.createGameNametxt.getText().contains(" "))
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Illegal game name");
					alert.setContentText("Please enter a game name that does not contain any spaces");
					alert.showAndWait();
				}
				else
				{
					model.createGame();
					view.createGameNametxt.setText("");
					view.createGamePasswordtxt.setText("");
					view.createNumPlayerscbx.setValue(2);
				}
			}
			
		});
		
		view.joinButton.setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent arg0) {
				if(view.gameList.getSelectionModel().getSelectedItem() !=null)
				{
					model.joinGame();
				}
				else
				{
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("No game selected");
					alert.setContentText("Please select a game first.");
					alert.showAndWait();
				}
				view.joinPassword.setText("");
			}
			
		});
		
		//listener from http://stackoverflow.com/questions/26424769/javafx8-how-to-create-listener-for-selection-of-row-in-tableview
		view.gameList.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
			
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
					view.chatField.setText("");
				}
			});
		view.joinGameSection.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent k) {
				if(k.getCode().equals(KeyCode.ENTER))
				{
					view.joinButton.fire();
				}
				
			}
			
		});
		view.createGameSection.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent k) {
				if(k.getCode().equals(KeyCode.ENTER))
				{
					view.createButton.fire();
				}
				
			}
			
		});
		view.chatSection.setOnKeyPressed(new EventHandler<KeyEvent>(){

			@Override
			public void handle(KeyEvent k) {
				if(k.getCode().equals(KeyCode.ENTER))
				{
					view.chatButton.fire();
				}
				
			}
			
		});
		
		
		view.stage.setOnCloseRequest((we)->
		{
			model.disconnect(we);
		}
		);
	
	}
	
	

}
