package client.lobby;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import messageObjects.UserInfoMessage;

public class UserInfoDataModel {
	private SimpleStringProperty username;
	private SimpleIntegerProperty gamesPlayed;
	private SimpleIntegerProperty gamesWon;
	private SimpleIntegerProperty gamesLost;
	
	public UserInfoDataModel(UserInfoMessage msg)
	{
		this.username = new SimpleStringProperty(msg.getUsername());
		this.gamesPlayed = new SimpleIntegerProperty(msg.getGamesPlayed());
		this.gamesWon = new SimpleIntegerProperty(msg.getGamesWon());
		this.gamesLost = new SimpleIntegerProperty(msg.getGamesLost());
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String username) {
		this.username.set(username);
	}

	public Integer getGamesPlayed() {
		return gamesPlayed.get();
	}

	public void setGamesPlayed(Integer gamesPlayed) {
		this.gamesPlayed.set(gamesPlayed);
	}

	public Integer getGamesWon() {
		return gamesWon.get();
	}

	public void setGamesWon(Integer gamesWon) {
		this.gamesWon.set(gamesWon);;
	}

	public Integer getGamesLost() {
		return gamesLost.get();
	}

	public void setGamesLost(Integer gamesLost) {
		this.gamesLost.set(gamesLost);
	}
}
