package gameObjects;

import java.util.Comparator;

public class PlayersComparator implements Comparator<Player> {

	@Override
	public int compare(Player player1, Player player2) {
		if(player1.countVictoryPoints()>player2.countVictoryPoints())
			return -1;
		if(player2.countVictoryPoints()==player2.countVictoryPoints())
			return 0;
		return 1;
	}
	


}
