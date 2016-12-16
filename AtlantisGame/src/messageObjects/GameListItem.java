package messageObjects;

import java.io.Serializable;



/**
* <h1>Sends single game item to client</h1>
* When a new game is created or a user has registered, this message is sent to all the clients
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
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
