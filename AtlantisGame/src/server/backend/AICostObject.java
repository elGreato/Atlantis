package server.backend;

import java.util.ArrayList;
import java.util.HashMap;

import gameObjects.Card;
import gameObjects.LandTile;
/**
* <h1>Contains Payment AI considers doing</h1>
* The payments are stored for each move (also if the AI considers playing multiple cards)
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class AICostObject {
	private HashMap<Integer, ArrayList<LandTile>> tilesPaidInEachMove;
	private HashMap<Integer, ArrayList<Card>>cardsPaidInEachMove;
	public AICostObject()
	{
		this.tilesPaidInEachMove = new HashMap<Integer, ArrayList<LandTile>>();
		this.cardsPaidInEachMove = new HashMap<Integer, ArrayList<Card>>();
	}
	public AICostObject(HashMap<Integer, ArrayList<LandTile>> tilesPaidInEachMove,
			HashMap<Integer,ArrayList<Card>> cardsPaidInEachMove) {
		super();
		this.tilesPaidInEachMove = tilesPaidInEachMove;
		this.cardsPaidInEachMove = cardsPaidInEachMove;
	}

	public HashMap<Integer,ArrayList<LandTile>> getTilesPaidInEachMove() {
		return tilesPaidInEachMove;
	}

	public void setTilesPaidInEachMove(HashMap<Integer,ArrayList<LandTile>> tilesPaidInEachMove) {
		this.tilesPaidInEachMove = tilesPaidInEachMove;
	}

	public HashMap<Integer,ArrayList<Card>> getCardsPaidInEachMove() {
		return cardsPaidInEachMove;
	}

	public void setCardsPaidInEachMove(HashMap<Integer,ArrayList<Card>> cardsPaidInEachMove) {
		this.cardsPaidInEachMove = cardsPaidInEachMove;
	
	}
	public int getRealCosts(int moveNumber, boolean isLastPayment) {
		int realCosts = 0;
		if(!isLastPayment)
		{
			realCosts = cardsPaidInEachMove.get(moveNumber).size()*2;
		}
		else
		{
			realCosts = cardsPaidInEachMove.get(moveNumber).size();
		}
		for(LandTile lt:tilesPaidInEachMove.get(moveNumber))
		{
			realCosts+=lt.getLandValue();
		}
		return realCosts;
	}
}
