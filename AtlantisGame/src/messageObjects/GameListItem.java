package messageObjects;

import java.io.Serializable;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//For games not started yet in lobby, nothing to do with an actual game instance


public class GameListItem implements Serializable{

	private SimpleStringProperty gameName;
	private SimpleIntegerProperty registeredPlayers;
	private SimpleIntegerProperty maxPlayers;
	private SimpleBooleanProperty hasPassword;
	
	public GameListItem(String gameName, boolean hasPassword, int registeredPlayers, int maxPlayers) {
		
		this.gameName = new SimpleStringProperty(gameName);
		this.hasPassword = new SimpleBooleanProperty(hasPassword);
		this.registeredPlayers = new SimpleIntegerProperty(registeredPlayers);
		this.maxPlayers = new SimpleIntegerProperty(maxPlayers);
	}
	
	public String getGameName() {
		return gameName.get();
	}

	public void setGameName(String gameName) {
		this.gameName.set(gameName);
	}

	public Integer getRegisteredPlayers() {
		return registeredPlayers.get();
	}

	public void setRegisteredPlayers(Integer registeredPlayers) {
		this.registeredPlayers.set(registeredPlayers);
	}

	public Integer getMaxPlayers() {
		return maxPlayers.get();
	}

	public void setMaxPlayers(Integer maxPlayers) {
		this.maxPlayers.set(maxPlayers);;
	}

	public Boolean getHasPassword() {
		return hasPassword.get();
	}

	public void setHasPassword(Boolean hasPassword) {
		this.hasPassword.set(hasPassword);
	}
}
