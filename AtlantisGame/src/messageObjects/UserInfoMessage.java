package messageObjects;

import java.io.Serializable;

import server.backend.UserInfo;

public class UserInfoMessage extends UserMessage implements Serializable {

	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	
	
	public UserInfoMessage(String username, int gamesPlayed, int gamesWon, int gamesLost) {
		super(username);
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
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
