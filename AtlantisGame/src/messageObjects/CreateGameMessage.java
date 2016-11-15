package messageObjects;

import java.io.Serializable;

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
