package client.lobby;

import client.game.GameModel;
import messageObjects.Message;
/**
* <h1>Interface for lobby functionality</h1>
* Every GameModel instance can invoke these methods in the LobbyModel class.
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public interface ClientLobbyInterface {
	//Send messages through this method
	public void sendMessage(Message msg);
	
	//Invoke this method when game is finished
	public void endGame(GameModel game);
}
