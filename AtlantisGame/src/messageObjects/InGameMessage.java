package messageObjects;

import java.io.Serializable;
/**
* <h1>Superclass for messages concerning in-game data</h1>
* Main class for game Messages (User class recognizes it and executes methods that are part of a game instance)
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class InGameMessage extends Message implements Serializable {
	
	private String gameName;

	public InGameMessage(String gameName) {
		super();
		this.gameName = gameName;
	}

	public String getGameName() {
		return gameName;
	}
}
