package messageObjects;

import java.io.Serializable;

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
