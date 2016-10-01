package messageObjects;

import java.io.Serializable;


//For games not started yet in lobby, nothing to do with an actual game instance


public class GameListItem extends LobbyMessage implements Serializable{

	private String gameName;
	private Integer registeredPlayers;
	private Integer maxPlayers;
	private Boolean hasPassword;
	
	public GameListItem(String gameName, boolean hasPassword, int registeredPlayers, int maxPlayers) {
		
		this.gameName = gameName;
		this.hasPassword = hasPassword;
		this.registeredPlayers = registeredPlayers;
		this.maxPlayers = maxPlayers;
	}
	
	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public Integer getRegisteredPlayers() {
		return registeredPlayers;
	}

	public void setRegisteredPlayers(Integer registeredPlayers) {
		this.registeredPlayers = registeredPlayers;
	}

	public Integer getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public Boolean getHasPassword() {
		return hasPassword;
	}

	public void setHasPassword(Boolean hasPassword) {
		this.hasPassword = hasPassword;
	}
	
	
}
