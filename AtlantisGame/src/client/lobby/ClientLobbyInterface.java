package client.lobby;

import messageObjects.InGameMessage;
import messageObjects.Message;

public interface ClientLobbyInterface {
	//Send messages through this method
	public void sendMessage(Message msg);
	
	//Invoke this method when game is finished
	public void endGame(String gameName);
}
