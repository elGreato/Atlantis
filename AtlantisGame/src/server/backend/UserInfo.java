package server.backend;

public class UserInfo {

	private String username;
	private String password;
	private int gamesPlayed;
	private int gamesWon;
	private int gamesLost;
	
	
	//Constructor
	public UserInfo(String username, String password, int gamesPlayed, int gamesWon, int gamesLost) {
		
		this.username = username;
		this.password = password;
		this.gamesPlayed = gamesPlayed;
		this.gamesWon = gamesWon;
		this.gamesLost = gamesLost;
	}
	
	
	//Update game stats
	public void gameWon(){
		gamesPlayed += 1;
		gamesWon += 1;
	}
	public void gameLost(){
		gamesPlayed += 1;
		gamesLost += 1;
	}
	public void gameTie()
	{
		gamesPlayed += 1;
	}
	
	
	//Getters and setters
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
}
