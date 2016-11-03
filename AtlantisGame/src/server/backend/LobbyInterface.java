package server.backend;

import gameObjects.Game;
import messageObjects.CreateGameMessage;
import messageObjects.GameJoinMessage;
import messageObjects.LobbyChatMessage;

public interface LobbyInterface {
	
	UserInfo loginUser(String username, String password, User user);
	
	void logoutFromOnlineUsers(User user);
	
	UserInfo createNewUser(String username, String password, User user);
	
	
	String createGame(User user, CreateGameMessage cgm);

	String joinGame(User user, GameJoinMessage gjm);
	
	

	void processChatMessage(LobbyChatMessage chatMessage);

	void sendWholeLobby(User user);

	int getPosition(UserInfo userInfo);
	
	//Invoke these methods to add wins/ties/losses to user data (Stores new value in lobby and updates database but does not message about winner)
	void addWin(User user);

	void addLoss(User user);
	
	void addTie(User user);
	
	//Invoke this method at the end of the game (after adding wins losses and ties above)
	void endGame(Game game);
	


	
}
