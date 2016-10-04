package messageObjects;

import java.io.Serializable;

public class InGameMessage extends Message implements Serializable {
	//Main class for game Messages (User class recognizes it and executes methods that are part of a game instance)
	private String gameName;

	public InGameMessage(String gameName) {
		super();
		this.gameName = gameName;
	}

	public String getGameName() {
		return gameName;
	}
}
