package messageObjects.lobbyMessages;

import java.io.Serializable;
import java.util.ArrayList;

import messageObjects.userMessages.UserInfoMessage;
/**
* <h1>Message with data for leaderboard</h1>
* Sends leaderboard data to clients.
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
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
