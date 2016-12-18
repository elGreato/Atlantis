package messageObjects.lobbyMessages;

import java.io.Serializable;
/**
* <h1>Message for creating new game</h1>
* When a new game is created this message is sent to server for verification of game data and the creation of the game.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class CreateGameMessage extends LobbyMessage implements Serializable{

	private String gameName;
	private String password;
	private int maxPlayers;
	private int aiPlayers;
	
	public CreateGameMessage(String gameName, String password, int maxPlayers, int aiPlayers) {
		this.gameName = gameName;
		this.password = password;
		this.maxPlayers = maxPlayers;
		this.aiPlayers = aiPlayers;
	}
	
	public String getGameName() {
		return gameName;
	}

	public String getPassword() {
		return password;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}
	public int getaiPlayers() {
		return aiPlayers;
	}

}
