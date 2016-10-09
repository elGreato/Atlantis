package server.backend;

import java.util.Comparator;

public class UserInfo implements Comparable<UserInfo>{

	private String username;
	private String password;
	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	private int points;
	
	
	public int getPoints() {
		return points;
	}


	//Constructor
	public UserInfo(String username, String password, int gamesPlayed, int gamesWon, int gamesLost) {
		
		this.username = username;
		this.password = password;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
		calculatePoints();
	}
	//Calculate points for leaderbord
	private void calculatePoints() {
		int gamesTie = gamesPlayed-gamesWon-gamesLost;
		points = 3 * gamesWon + 1 * gamesTie - 3 * gamesLost;
	}


	//Update game stats
	public void gameWon(){
		gamesPlayed += 1;
		gamesWon += 1;
		calculatePoints();
	}
	public void gameLost(){
		gamesPlayed += 1;
		gamesLost += 1;
		calculatePoints();
	}
	public void gameTie()
	{
		gamesPlayed += 1;
		calculatePoints();
	}
	
	
	//Getters
	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
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

	//Reverse sort (to get elements with highest points in the beginning of a list)
	@Override
	public int compareTo(UserInfo ui2) {
		return -((Integer)points).compareTo((Integer)ui2.getPoints());
	}

	
}
