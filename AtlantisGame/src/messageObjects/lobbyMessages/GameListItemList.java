package messageObjects.lobbyMessages;

import java.io.Serializable;
import java.util.ArrayList;
/**
* <h1>Message that sends whole game list to client</h1>
* When a client logs in the whole list of waiting games is sent to it.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class GameListItemList extends LobbyMessage implements Serializable {
	private ArrayList<GameListItem> games;

	public GameListItemList(ArrayList<GameListItem> games) {
		super();
		this.games = games;
	}

	public ArrayList<GameListItem> getGames() {
		return games;
	}
}
