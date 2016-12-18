package client.lobby;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import messageObjects.lobbyMessages.GameListItem;
/**
* <h1Data model for games</h1>
* Contains data for game list table in lobby
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class GameListItemDataModel {
	private SimpleStringProperty gameName;
	private SimpleIntegerProperty registeredPlayers;
	private SimpleIntegerProperty maxPlayers;
	private SimpleBooleanProperty hasPassword;
	
	public GameListItemDataModel(GameListItem input) {
		
		this.gameName = new SimpleStringProperty(input.getGameName());
		this.hasPassword = new SimpleBooleanProperty(input.getHasPassword());
		this.registeredPlayers = new SimpleIntegerProperty(input.getRegisteredPlayers());
		this.maxPlayers = new SimpleIntegerProperty(input.getMaxPlayers());
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
