package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

public class UserInfoListMessage extends LobbyMessage implements Serializable{
	ArrayList<UserInfoMessage> leaderboard;

	public UserInfoListMessage(ArrayList<UserInfoMessage> leaderboard) {
		super();
		this.leaderboard = leaderboard;
	}

	public ArrayList<UserInfoMessage> getLeaderboard() {
		return leaderboard;
	}
}
