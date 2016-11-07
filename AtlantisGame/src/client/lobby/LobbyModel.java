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
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.util.converter.NumberStringConverter;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.GameListItem;
import messageObjects.GameListItemList;
import messageObjects.GameStartMessage;
import messageObjects.InGameMessage;
import messageObjects.LobbyChatMessage;
import messageObjects.Message;
import messageObjects.ServerInfoMessage;
import messageObjects.UserInfoListMessage;
import messageObjects.UserInfoMessage;
import server.backend.UserInfo;

public class LobbyModel implements Runnable, ClientLobbyInterface{

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
		
		view.userInfoName.textProperty().bind(new SimpleStringProperty(userInfo.getUsername()));
		view.userInfoPosition.textProperty().bind(new SimpleStringProperty(((Integer)userInfo.getPosition()).toString()));
		view.userInfoPoints.textProperty().bind(new SimpleStringProperty(((Integer)userInfo.getPoints()).toString()));
		view.userInfoGamesPlayed.textProperty().bind(new SimpleStringProperty(((Integer)userInfo.getGamesPlayed()).toString()));
		view.userInfoGamesWon.textProperty().bind(new SimpleStringProperty(((Integer)userInfo.getGamesWon()).toString()));
		view.userInfoGamesTie.textProperty().bind(new SimpleStringProperty(((Integer)userInfo.getGamesTie()).toString()));
		view.userInfoGamesLost.textProperty().bind(new SimpleStringProperty(((Integer)userInfo.getGamesLost()).toString()));
		
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
				Object obj = ois.readUnshared();
				if(obj instanceof InGameMessage)
				{
					InGameMessage msg = (InGameMessage)obj;
					String gameName = msg.getGameName();
					Platform.runLater(new Runnable(){
					public void run(){
						for(GameModel gm : runningGames)
						{
							if (gm.getGameName().equals(gameName))
							{
							gm.processMessage(msg);
							break;
							}
						}
					}
					});
				}
				else if(obj instanceof ServerInfoMessage)
				{
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
				
				else if(obj instanceof LobbyChatMessage)
				{
					LobbyChatMessage chatMessage = (LobbyChatMessage)obj;
					view.chatHistory.appendText(chatMessage.getAuthor() + " : " + chatMessage.getMessage() + "\n");
				}
				
				else if(obj instanceof UserInfoListMessage)
				{
					UserInfoListMessage leaderboard = (UserInfoListMessage)obj;
					view.userData.clear();
					for(UserInfoMessage uim : leaderboard.getLeaderboard())
					{
						view.userData.add(new UserInfoDataModel(uim));
					}
				}
				else if(obj instanceof UserInfoMessage)	
				{
					UserInfoMessage thisUserStats = (UserInfoMessage)obj;
					userInfo.setUsername(thisUserStats.getUsername());
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
			} catch(Exception e)
			{
				e.printStackTrace();
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
	@Override
	public synchronized void sendMessage(Message msg)
	{
		try {
			oos.writeObject(msg);
			oos.flush();
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

	public void endGame(GameModel game)
	{
		runningGames.remove(game);
	}


	@Override
	public void endGame(String gameName) {
		for(GameModel m: runningGames)
		{
			if(m.getGameName().equals(gameName))
			{
				runningGames.remove(m);
			}
		}
		
	}
}