package gameObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import gameObjects.ColorChoice;
/*
 * @author Ali Habbabeh
 */

public class Player implements Serializable {

	private String name; 

	private int victoryPoints = 0;

	private PlayerHand playerHand;

	private ArrayList<Pawn> pawns = new ArrayList<>();

	private ColorChoice color;
	
	private int playerIndex;
	
	private boolean yourTurn = false;
	
	private ArrayList<Player> opponents = new ArrayList<>();

	public Player(String name) {
		// the player extends vbox
		super();
		this.name = name;
		// create the player hand
		playerHand = new PlayerHand(name);

		// create 3 pawns for each player, -1 is the location for Atlantis
		for (int i = 0; i < 3; i++) {
			Pawn pawn = new Pawn(this, i);
			pawn.setOldLocation(-1);
			pawns.add(pawn);
			

		}

	}

	// this method is from the first semester as well
	public void addCard(Card card) {
		// Add card to the hand
		playerHand.addCard(card);

	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public int countVictoryPoints() {
		int result = 0;
		result += this.playerHand.getCards().size();
		result += this.playerHand.getTreasuresValue(this.playerHand.getTreasures());
		return result;
	}

	public String getPlayerName() {
		return name;
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

	public PlayerHand getPlayerHand() {
		return playerHand;
	}

	public void setPlayerHand(PlayerHand playerHand) {
		this.playerHand = playerHand;
	}

	public ArrayList<Pawn> getPawns() {
		return pawns;
	}

	public void setPawns(ArrayList<Pawn> pawns) {
		this.pawns = pawns;
	}

	public ColorChoice getColor() {
		return color;
	}

	public void setColor(ColorChoice color) {
		this.color = color;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public boolean isYourTurn() {
		return yourTurn;
	}

	public void setYourTurn(boolean yourTurn) {
		this.yourTurn = yourTurn;
	}

	public ArrayList<Player> getOpponents() {
		return opponents;
	}

	public void setOpponents(ArrayList<Player> opponents) {
		this.opponents = opponents;
	}

}
