package client.lobby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameListItem;
import messageObjects.GameListItemList;
import messageObjects.InGameMessage;
import messageObjects.ServerInfoMessage;

public class LobbyModel implements Runnable{

	Thread listener;
	private LobbyView view;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	
	public LobbyModel(LobbyView view, ObjectOutputStream oos, ObjectInputStream ois) {
		this.view = view;
		this.oos = oos;
		this.ois = ois;

	}
	
	public void startListener()
	{
		listener = new Thread(this);
		listener.start();
	}
	
	@Override
	public void run() {
		
		
		
		boolean connected = true;
		while(connected)
		{
			try {
				System.out.println("Ready to read");
				Object obj = ois.readObject();
				System.out.println("Read an object");
				if(obj instanceof InGameMessage)
				{
					
				}
				else if(obj instanceof ServerInfoMessage)
				{
					System.out.println("Recieved info from server");
					ServerInfoMessage serverInfoMessage = (ServerInfoMessage)obj;
					Platform.runLater(new Runnable()
					{
						
						@Override
						public void run() 
						{
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Server information");
							alert.setContentText(serverInfoMessage.getMessage());
							alert.showAndWait();
							
						}
						
					});
				}
				else if(obj instanceof GameListItem)
				{
					System.out.println("Recieved update from games from server");
					GameListItemDataModel updatedGame = new GameListItemDataModel((GameListItem)obj);
					System.out.println(updatedGame.getRegisteredPlayers());
					
					Iterator<GameListItemDataModel> gameListIt = view.gameData.iterator();
					while(gameListIt.hasNext())
					{
						GameListItemDataModel g = gameListIt.next();
						if(g.getGameName().equals(updatedGame.getGameName()))
						{
							gameListIt.remove();
						}
					}
					if(updatedGame.getRegisteredPlayers()<updatedGame.getMaxPlayers())
					{
						view.gameData.add(updatedGame);
					}
					
				}
				else if(obj instanceof GameListItemList)
				{
					System.out.println("I'm receiving a gameList");
					GameListItemList glil = (GameListItemList)obj;
					view.gameData.clear();
					for(GameListItem g : glil.getGames())
					{
						view.gameData.add(new GameListItemDataModel(g));
					}
					
				}
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				connected = false;
				e.printStackTrace();
				Platform.runLater(new Runnable(){
					public void run()
					{
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Connection error");
						alert.setContentText("Connection to server lost. Please try to restart the program later.");
						alert.showAndWait();
					}
				});
			}
		}
		
	}

	public void createGame() {
		CreateGameMessage createGameMsg = new CreateGameMessage(view.createGameNametxt.getText(), view.createGamePasswordtxt.getText(), view.createNumPlayerscbx.getValue());
		try {
			oos.writeObject(createGameMsg);
			System.out.println("new game sent to server");
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Connection error");
			alert.setContentText("Connection to server lost. Please try to restart the program later.");
			alert.showAndWait();
		}
	}

	public void joinGame() {
	
		GameListItemDataModel gameToJoin = view.gameList.getSelectionModel().getSelectedItem();
		
		if(!gameToJoin.equals(null))
		{
			System.out.println(gameToJoin.getGameName());
			GameJoinMessage joinGamemsg = new GameJoinMessage(gameToJoin.getGameName(), view.joinPassword.getText());
			try {
				oos.writeObject(joinGamemsg);
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Connection error");
				alert.setContentText("Connection to server lost. Please try to restart the program later.");
				alert.showAndWait();
				e.printStackTrace();
			}
		}
		
	}

	
}
