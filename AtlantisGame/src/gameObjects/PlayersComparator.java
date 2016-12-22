package gameObjects;

import java.util.Comparator;
/**
* <h1>Player comparator</h1>
*  comapres the victory points of 2 players
* 
* @author  Ali Habbabeh
* @version 1.2
* @since   2016-12-22
*/
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
