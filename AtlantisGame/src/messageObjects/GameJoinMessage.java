package messageObjects;

import java.io.Serializable;

public class GameJoinMessage implements Serializable {
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
