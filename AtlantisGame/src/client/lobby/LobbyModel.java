package client.lobby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import client.game.GameController;
import client.game.GameModel;
import client.game.GameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameListItem;
import messageObjects.GameListItemList;
import messageObjects.GameStartMessage;
import messageObjects.InGameMessage;
import messageObjects.LobbyChatMessage;
import messageObjects.Message;
import messageObjects.ServerInfoMessage;
import messageObjects.UserInfoMessage;
import server.backend.UserInfo;

public class LobbyModel implements Runnable{

	Thread listener;
	private LobbyView view;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private UserInfoDataModel userInfo;
	
	private ArrayList<GameModel> runningGames;
	
	public LobbyModel(LobbyView view, UserInfoMessage userInfo, ObjectOutputStream oos, ObjectInputStream ois) {
		this.view = view;
		this.oos = oos;
		this.ois = ois;
		this.userInfo = new UserInfoDataModel(userInfo);
		runningGames = new ArrayList<GameModel>();

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
					
					boolean isSelected = false;
					
					Iterator<GameListItemDataModel> gameListIt = view.gameData.iterator();
					while(gameListIt.hasNext())
					{
						GameListItemDataModel g = gameListIt.next();
						if(g.getGameName().equals(updatedGame.getGameName()))
						{
							if(view.gameList.getSelectionModel().getSelectedItem() != null && view.gameList.getSelectionModel().getSelectedItem().equals(g))
							{
								view.gameList.getSelectionModel().clearSelection();
								isSelected = true;
							}
							gameListIt.remove();
							System.out.println("Detected existing instance in List");
						}
					}
					if(updatedGame.getRegisteredPlayers()<updatedGame.getMaxPlayers())
					{
						view.gameData.add(updatedGame);
						if(isSelected)
						{
							view.gameList.getSelectionModel().select(updatedGame);
						}
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
				
				else if (obj instanceof GameStartMessage)
				{
					LobbyModel thisModel = this;
					Platform.runLater(new Runnable(){
						public void run()
						{
							String gameName = ((GameStartMessage)obj).getGameName();
							GameView gameView = new GameView();
							GameModel gameModel = new GameModel(gameName, thisModel, gameView);
							GameController gameController = new GameController(gameView, gameModel);
							runningGames.add(gameModel);
						}
					});

				}
				else if(obj instanceof InGameMessage)
				{
					InGameMessage msg = (InGameMessage)obj;
					String gameName = msg.getGameName();
					for(GameModel gm : runningGames)
					{
						if (gm.getGameName().equals(gameName))
						{
							Platform.runLater(new Runnable(){
								public void run()
								{
									gm.processMessage(msg);
								}
							});
							break;
						}
					}
				}
				else if(obj instanceof LobbyChatMessage)
				{
					LobbyChatMessage chatMessage = (LobbyChatMessage)obj;
					view.chatHistory.appendText(chatMessage.getAuthor() + " : " + chatMessage.getMessage() + "\n");
				}
				
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				connected = false;
				e.printStackTrace();
				Platform.runLater(new Runnable(){
					public void run()
					{
						connectionLost();
					}
				});
			}
		}
		
	}

	public void createGame() {
		CreateGameMessage createGameMsg = new CreateGameMessage(view.createGameNametxt.getText(), view.createGamePasswordtxt.getText(), view.createNumPlayerscbx.getValue());
		sendMessage(createGameMsg);
	}

	public void joinGame() {
	
		GameListItemDataModel gameToJoin = view.gameList.getSelectionModel().getSelectedItem();
		
		if(!gameToJoin.equals(null))
		{
			GameJoinMessage joinGamemsg = new GameJoinMessage(gameToJoin.getGameName(), view.joinPassword.getText());
			sendMessage(joinGamemsg);
		}
		
	}

	public void sendChatMessage() {
		LobbyChatMessage chatMessage = new LobbyChatMessage(userInfo.getUsername(), view.chatField.getText());
		sendMessage(chatMessage);
	}

	public synchronized void sendMessage(Message msg)
	{
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			connectionLost();
		}
	}
	
	
	private void connectionLost() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Connection error");
		alert.setContentText("Connection to server lost. Please try to restart the program later.");
		alert.showAndWait();
		
	}

	
}