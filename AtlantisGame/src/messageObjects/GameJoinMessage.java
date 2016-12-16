package messageObjects;

import java.io.Serializable;
/**
* <h1>Message that contains user request to join a game</h1>
* When a user registers for a game this message is sent to server.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class GameJoinMessage extends LobbyMessage implements Serializable {
	private String gameName;
	private String gamePassword;
	
	public GameJoinMessage(String gameName, String gamePassword) {
		this.gameName = gameName;
		this.gamePassword = gamePassword;
	}

	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGamePassword() {
		return gamePassword;
	}
	public void setGamePassword(String gamePassword) {
		this.gamePassword = gamePassword;
	}

}
