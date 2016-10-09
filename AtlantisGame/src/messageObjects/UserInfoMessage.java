package messageObjects;

import java.io.Serializable;

import server.backend.UserInfo;

public class UserInfoMessage extends UserMessage implements Serializable {

	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	private int points;
	private int position;
	
	
	public UserInfoMessage(String username, int gamesPlayed, int gamesWon, int gamesLost, int position) {
		super(username);
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		this.position = position;
	}
	
	public UserInfoMessage(UserInfo userInfo, int position) {
		super(userInfo.getUsername());
		this.gamesPlayed = userInfo.getGamesPlayed();
		this.gamesWon = userInfo.getGamesWon();
		this.gamesLost = userInfo.getGamesLost();
		this.position = position;
	}

	public int getGamesPlayed() {
		return gamesPlayed;
	}


	public int getGamesWon() {
		return gamesWon;
	}


	public int getGamesLost() {
		return gamesLost;
	}


	
}
