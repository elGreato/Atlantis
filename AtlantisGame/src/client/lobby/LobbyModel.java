package client.lobby;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import client.game.GameController;
import client.game.GameModel;
import client.game.GameView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.WindowEvent;
import javafx.scene.control.Alert.AlertType;
import messageObjects.InGameMessage;
import messageObjects.Message;
import messageObjects.ServerInfoMessage;
import messageObjects.lobbyMessages.CreateGameMessage;
import messageObjects.lobbyMessages.GameJoinMessage;
import messageObjects.lobbyMessages.GameListItem;
import messageObjects.lobbyMessages.GameListItemList;
import messageObjects.lobbyMessages.GameStartMessage;
import messageObjects.lobbyMessages.LobbyChatMessage;
import messageObjects.lobbyMessages.UserInfoListMessage;
import messageObjects.userMessages.UserInfoMessage;

/**
* <h1>Model of the lobby</h1>
* The client-side logic of the lobby. Responsible for sending and receiving messages about games, users and stats.
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/

public class LobbyModel implements Runnable, ClientLobbyInterface{

	Thread listener;
	private LobbyView view;
	
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	private UserInfoDataModel userInfo;
	
	private ArrayList<GameModel> runningGames;
	
	boolean connected;
	//Constructor called after a successful login
	public LobbyModel(LobbyView view, UserInfoMessage userInfo, ObjectOutputStream oos, ObjectInputStream ois) {
		this.view = view;
		this.oos = oos;
		this.ois = ois;
		this.userInfo = new UserInfoDataModel(userInfo);
		updateGUIUserStats();

		runningGames = new ArrayList<GameModel>();

	}
	//Updates stats of the user that is logged in (top right part of lobby)
	private void updateGUIUserStats() {
		
		view.userInfoName.setText(String.valueOf(userInfo.getUsername()));
		view.userInfoPosition.setText(String.valueOf(userInfo.getPosition()));
		view.userInfoPoints.setText(String.valueOf(userInfo.getPoints()));
		view.userInfoGamesPlayed.setText(String.valueOf(userInfo.getGamesPlayed()));
		view.userInfoGamesWon.setText(String.valueOf(userInfo.getGamesWon()));
		view.userInfoGamesTie.setText(String.valueOf(userInfo.getGamesTie()));
		view.userInfoGamesLost.setText(String.valueOf(userInfo.getGamesLost()));
	}
	//Starts message listener thread
	public void startListener()
	{
		listener = new Thread(this);
		listener.start();
	}
	//Listens for messages from server and finds out what type of message it is.
	@Override
	public void run() {
		connected = true;
		while(connected)
		{
			try {
				Object obj = ois.readUnshared();
				if(obj instanceof InGameMessage)
				{
					InGameMessage msg = (InGameMessage)obj;
					sendToGame(msg);
				}
				else if(obj instanceof ServerInfoMessage)
				{
					ServerInfoMessage serverInfoMessage = (ServerInfoMessage)obj;
					showMessageFromServer(serverInfoMessage);
				}
				else if(obj instanceof GameListItem)
				{
					GameListItemDataModel updatedGame = new GameListItemDataModel((GameListItem)obj);
					updateGameList(updatedGame);
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
					GameStartMessage gsm = (GameStartMessage)obj;
					startGame(gsm);
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
					updateUserStats(thisUserStats);

				}
			} catch (ClassNotFoundException e) {
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				connected = false;
				e.printStackTrace();
				
				connectionLost();
			
			} catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	//updates user stats of user that is logged in on this client instance
	private void updateUserStats(UserInfoMessage thisUserStats) {
		System.out.println("UserInfo received! New losses = " + thisUserStats.getGamesLost());
		userInfo.setUsername(thisUserStats.getUsername());
		userInfo.setGamesPlayed(thisUserStats.getGamesPlayed());
		userInfo.setGamesWon(thisUserStats.getGamesWon());
		userInfo.setGamesTie(thisUserStats.getGamesTie());
		userInfo.setGamesLost(thisUserStats.getGamesLost());
		userInfo.setPosition(thisUserStats.getPosition());
		userInfo.setPoints(thisUserStats.getPoints());
		Platform.runLater(()->{
			updateGUIUserStats();
		});
	}
	//Starts a game when the required amount of users have registered
	public void startGame(GameStartMessage gsm) {
		LobbyModel thisModel = this;
		Platform.runLater(new Runnable(){
			public void run()
			{
				String gameName = gsm.getGameName();
				GameView gameView = new GameView();
				GameModel gameModel = new GameModel(gameName, thisModel, gameView);
				GameController gameController = new GameController(gameView, gameModel);
				runningGames.add(gameModel);
			}
		});
		
	}
	//Sends message to the respective gameModel instance
	private void sendToGame(InGameMessage msg) {
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
	//Shows a message box if there is a string message from the server (e.g. game name already taken)
	private void showMessageFromServer(ServerInfoMessage serverInfoMessage) {
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
	//updates game list when a update message arrives
	private void updateGameList(GameListItemDataModel updatedGame) {
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
	//When a user creates a game this method sends a message to the server
	public void createGame() {
		CreateGameMessage createGameMsg = new CreateGameMessage(view.createGameNametxt.getText(), view.createGamePasswordtxt.getText(), view.createNumPlayerscbx.getValue(), view.createNumAIPlayerscbx.getValue());
		sendMessage(createGameMsg);
	}
	//When a user joins a game this method is invoked and sends a message to the server
	public void joinGame() {
	
		GameListItemDataModel gameToJoin = view.gameList.getSelectionModel().getSelectedItem();
		
		if(!gameToJoin.equals(null))
		{
			GameJoinMessage joinGamemsg = new GameJoinMessage(gameToJoin.getGameName(), view.joinPassword.getText());
			sendMessage(joinGamemsg);
		}
		
	}
	//Sends input from chat to server
	public void sendChatMessage() {
		LobbyChatMessage chatMessage = new LobbyChatMessage(userInfo.getUsername(), view.chatField.getText());
		sendMessage(chatMessage);
	}
	//Method that sends all the messages to server
	@Override
	public synchronized void sendMessage(Message msg)
	{
		try {
			oos.writeUnshared(msg);
			oos.flush();
		} catch (IOException e) {
			connectionLost();
		}
	}
	
	//Displays error message when the connection to a server is lost
	private void connectionLost() {
		Platform.runLater(() -> {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Connection error");
			alert.setContentText("Connection to server lost. Please try to restart the program later.");
			alert.showAndWait();
		});
		
	}
	//Invoked when a game ends
	@Override
	public void endGame(GameModel game)
	{
		runningGames.remove(game);
	}
	//Handles case when a user closes the lobby window.
	public void disconnect(WindowEvent e) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Close Atlantis");
		alert.setContentText("Do you really want to close Atlantis? You will lose all the games you are currently playing.");
		Optional<ButtonType> answer = alert.showAndWait();
		if(answer.get() == ButtonType.OK)
		{
			System.exit(0);
		}
		else
		{
			e.consume();
		}
	}
}