package gameObjects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

//For games not started yet in lobby, nothing to do with an actual game instance


public class GameListItem {

	private SimpleStringProperty gameName;
	private SimpleIntegerProperty registeredPlayers;
	private SimpleIntegerProperty maxPlayers;
	
	public GameListItem(String gameName, int registeredPlayers, int maxPlayers) {
		
		this.gameName = new SimpleStringProperty(gameName);
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
}
