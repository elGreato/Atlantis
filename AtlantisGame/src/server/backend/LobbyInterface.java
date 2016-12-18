package server.backend;

import java.util.ArrayList;

import gameObjects.Game;
import messageObjects.lobbyMessages.CreateGameMessage;
import messageObjects.lobbyMessages.GameJoinMessage;
import messageObjects.lobbyMessages.LobbyChatMessage;
/**
* <h1>Interface for lobby for external access</h1>
* Users and games access lobby functionality through this interface.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public interface LobbyInterface {
	
	UserInfo loginUser(String username, String password, HumanUser user);
	
	void logoutFromOnlineUsers(HumanUser user);
	
	UserInfo createNewUser(String username, String password, HumanUser user);
	
	
	String createGame(HumanUser user, CreateGameMessage cgm);

	String joinGame(HumanUser user, GameJoinMessage gjm);
	
	

	void processChatMessage(LobbyChatMessage chatMessage);

	void sendWholeLobby(HumanUser user);

	int getPosition(UserInfo userInfo);
	
	public AIUser requestAI(ArrayList<User> playersRegistered);
	
	//Invoke these methods to add wins/ties/losses to user data (Stores new value in lobby and updates database but does not message about winner)
	void addWin(User user);

	void addLoss(User user);
	
	void addTie(User user);
	
	//Invoke this method at the end of the game (after adding wins losses and ties above)
	void endGame(Game game);
	


	
}
