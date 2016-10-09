package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

public class UseInfoListMessage extends LobbyMessage implements Serializable{
	ArrayList<UserInfoMessage> leaderboard;

	public UseInfoListMessage(ArrayList<UserInfoMessage> leaderboard) {
		super();
		this.leaderboard = leaderboard;
	}

	public ArrayList<UserInfoMessage> getLeaderboard() {
		return leaderboard;
	}
}
