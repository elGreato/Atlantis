package server.backend;

import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.LandTile;
import gameObjects.Pawn;
/**
* <h1>Contains turns AI considers doing</h1>
* When an AI calculates its turns it stores the information about played pawns, played cards, costs and values here.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class AITurnObject {
	private ArrayList<Pawn> pawnsThatCanBePlayed;
	private ArrayList<Card> cardsOnHandLeft;
	private ArrayList<Card> cardsAlreadyPlayed;
	private ArrayList<LandTile> landTilesLeftOnHand;
	private AICostObject costsIncurredPerMove;
	private int distanceTraveled; 
	private int costsIncurred;	
	private double valueOfTurn;
	
	public AITurnObject(ArrayList<Pawn> pawns, ArrayList<Card> cards, ArrayList<LandTile> landTilesLeftOnHand)
	{
		this.pawnsThatCanBePlayed = pawns;
		this.cardsOnHandLeft = cards;
		this.landTilesLeftOnHand = landTilesLeftOnHand;
		this.cardsAlreadyPlayed = new ArrayList<Card>();
		this.costsIncurredPerMove = new AICostObject();
	}

	public AITurnObject(ArrayList<Pawn> pawnsThatCanBePlayed, ArrayList<Card> cardsOnHandLeft,
			ArrayList<Card> cardsAlreadyPlayed, ArrayList<LandTile> landTilesLeftOnHand,
			AICostObject costsIncurredPerMove, int distanceTraveled, int costsIncurred) {
		super();
		this.pawnsThatCanBePlayed = pawnsThatCanBePlayed;
		this.cardsOnHandLeft = cardsOnHandLeft;
		this.cardsAlreadyPlayed = cardsAlreadyPlayed;
		this.landTilesLeftOnHand = landTilesLeftOnHand;
		this.costsIncurredPerMove = costsIncurredPerMove;
		this.distanceTraveled = distanceTraveled;
		this.costsIncurred = costsIncurred;
	}

	public ArrayList<Pawn> getPawnsThatCanBePlayed() {
		return pawnsThatCanBePlayed;
	}

	public void setPawnsThatCanBePlayed(ArrayList<Pawn> pawnsThatCanBePlayed) {
		this.pawnsThatCanBePlayed = pawnsThatCanBePlayed;
	}

	public ArrayList<Card> getCardsOnHandLeft() {
		return cardsOnHandLeft;
	}

	public void setCardsOnHandLeft(ArrayList<Card> cardsOnHandLeft) {
		this.cardsOnHandLeft = cardsOnHandLeft;
	}

	public ArrayList<Card> getCardsAlreadyPlayed() {
		return cardsAlreadyPlayed;
	}

	public void setCardsAlreadyPlayed(ArrayList<Card> cardsAlreadyPlayed) {
		this.cardsAlreadyPlayed = cardsAlreadyPlayed;
	}

	public ArrayList<LandTile> getLandTilesLeftOnHand() {
		return landTilesLeftOnHand;
	}

	public void setLandTilesLeftOnHand(ArrayList<LandTile> landTilesLeftOnHand) {
		this.landTilesLeftOnHand = landTilesLeftOnHand;
	}

	public AICostObject getCostsIncurredPerMove() {
		return costsIncurredPerMove;
	}

	public void setCostsIncurredPerMove(AICostObject costsIncurredPerMove) {
		this.costsIncurredPerMove = costsIncurredPerMove;
	}

	public int getDistanceTraveled() {
		return distanceTraveled;
	}

	public void setDistanceTraveled(int distanceTraveled) {
		this.distanceTraveled = distanceTraveled;
	}

	public int getCostsIncurred() {
		return costsIncurred;
	}

	public void setCostsIncurred(int costsIncurred) {
		this.costsIncurred = costsIncurred;
	}

	public double getValueOfTurn() {
		return valueOfTurn;
	}

	public void setValueOfTurn(double valueOfTurn) {
		this.valueOfTurn = valueOfTurn;
	}
}
