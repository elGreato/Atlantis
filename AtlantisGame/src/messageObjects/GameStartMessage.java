package messageObjects;

import java.io.Serializable;
/**
* <h1>Message that indicates start of a game</h1>
* Informs client about the start of a game.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class GameStartMessage extends LobbyMessage implements Serializable{
	private String gameName;

	public String getGameName() {
		return gameName;
	}

	public GameStartMessage(String gameName) {
		super();
		this.gameName = gameName;
	}
}
