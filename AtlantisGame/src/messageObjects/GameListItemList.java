package messageObjects;

import java.io.Serializable;
import java.util.ArrayList;

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
