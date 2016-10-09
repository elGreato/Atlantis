package messageObjects;

import java.io.Serializable;

import server.backend.UserInfo;

public class UserInfoMessage extends UserMessage implements Serializable {

	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	private int points;
	private int position;
	
	
	public UserInfoMessage(UserInfo userInfo, int position) {
		super(userInfo.getUsername());
		this.gamesPlayed = userInfo.getGamesPlayed();
		this.gamesWon = userInfo.getGamesWon();
		this.gamesLost = userInfo.getGamesLost();
		this.points = userInfo.getPoints();
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
	
	public int getPoints() {
		return points;
	}

	public int getPosition() {
		return position;
	}
	


	
}
