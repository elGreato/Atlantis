package messageObjects;

import java.io.Serializable;

public class CreateGameMessage implements Serializable{

	private String gameName;
	private String password;
	private int maxPlayers;
	
	public CreateGameMessage(String gameName, String password, int maxPlayers) {
		this.gameName = gameName;
		this.password = password;
		this.maxPlayers = maxPlayers;
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

}
