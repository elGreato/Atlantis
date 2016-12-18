package client.lobby;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import messageObjects.userMessages.UserInfoMessage;
/**
* <h1>Data model for user stats</h1>
* Contains data for leaderboard table in lobby window
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class UserInfoDataModel {
	private SimpleStringProperty username;
	private SimpleIntegerProperty gamesPlayed;
	private SimpleIntegerProperty gamesWon;
	private SimpleIntegerProperty gamesTie;
	private SimpleIntegerProperty gamesLost;
	private SimpleIntegerProperty points;
	private SimpleIntegerProperty position;


	
	public UserInfoDataModel(UserInfoMessage msg)
	{
		this.username = new SimpleStringProperty(msg.getUsername());
		this.gamesPlayed = new SimpleIntegerProperty(msg.getGamesPlayed());
		this.gamesWon = new SimpleIntegerProperty(msg.getGamesWon());
		this.gamesTie = new SimpleIntegerProperty(msg.getGamesTie());
		this.gamesLost = new SimpleIntegerProperty(msg.getGamesLost());
		this.points = new SimpleIntegerProperty(msg.getPoints());
		this.position = new SimpleIntegerProperty(msg.getPosition());
		
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
	public Integer getGamesTie() {
		return gamesTie.get();
	}

	public void setGamesTie(Integer gamesTie) {
		this.gamesTie.set(gamesTie);;
	}
	public Integer getGamesLost() {
		return gamesLost.get();
	}
	
	public void setGamesLost(Integer gamesLost) {
		this.gamesLost.set(gamesLost);
	}
	
	public void setPoints(Integer points) {
		this.points.set(points);
	}
	public Integer getPoints() {
		return points.get();
	}
	
	public void setPosition(Integer position) {
		this.position.set(position);
	}
	public Integer getPosition() {
		return position.get();
	}
	
}
