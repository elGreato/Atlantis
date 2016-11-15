package server.backend;

import java.util.ArrayList;

import gameObjects.Card;
import gameObjects.GameInterface;
import gameObjects.LandTile;
import gameObjects.Pawn;
import gameObjects.Player;
import gameObjects.WaterTile;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;

public class GameAI {
	private GameInterface game;
	private ArrayList<WaterTile> path;
	private ArrayList<Player> opponentInfo;
	public Player me;
	private double aiSpeed;
	private double aiPawnSpread;
	public String getGameName() {
		return game.getName();
	}
	
	public GameAI(GameInterface game, double aiSpeed, double aiPawnSpread)
	{
		this.game = game;
	}
	public void processMessage(InGameMessage igm) {
		if(igm instanceof WaterMessage)
		{
			path = ((WaterMessage)igm).getBase();
		}
		else if(igm instanceof PlayerMessage)
		{
			me = ((PlayerMessage)igm).getPlayer();
		}
		else if(igm instanceof OpponentMessage)
		{
			opponentInfo = ((OpponentMessage)igm).getOpponents();
		}
		else if(igm instanceof GameStatusMessage)
		{
			GameStatusMessage gsm =(GameStatusMessage)igm;
			if(gsm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex())
			{
				doATurn();
			}
		}
		else if(igm instanceof PlayAnotherCardMessage)
		{
			game.processMessage(new PawnCardSelectedMessage(game.getName(),me.getPlayerIndex(), bestPawn, bestCards.remove(0)));
		}
		
	}

	private double valueBestTurn;
	private ArrayList<Card> bestCards;
	private Pawn bestPawn;
	
	private double avgDistanceOtherPlayers;
	private double avgDistanceMe;
	private void doATurn() {
		calculateAverageDistances();
		doAMove(me.getPawns(), me.getPlayerHand().getCards(),new ArrayList<Card>(),  0, 0);
		game.processMessage(new PawnCardSelectedMessage(game.getName(),me.getPlayerIndex(),bestPawn, bestCards.remove(0)));
		
	}

	private void calculateAverageDistances() {
		avgDistanceOtherPlayers = 0;
		avgDistanceMe = 0;
		for(Player pl: opponentInfo)
		{
			for(Pawn pa :pl.getPawns())
			{
				avgDistanceOtherPlayers += pa.getNewLocation();
			}
		}
		avgDistanceOtherPlayers /= (3*opponentInfo.size());
		for(Pawn pa : me.getPawns())
		{
			avgDistanceMe += pa.getNewLocation();
		}
		avgDistanceMe /= 3;
	}

	private void doAMove(ArrayList<Pawn> pawns, ArrayList<Card> cardsOnHand,ArrayList<Card> cardsAlreadyPlayed, int distance, int costs) {
		bestCards = new ArrayList<Card>();
		bestCards.clear();
		bestPawn = null;
		for(Pawn p : pawns)
		{
			for(Card c : cardsOnHand)
			{
				cardsAlreadyPlayed.add(c);
				
				int distanceForMove = calculateDistanceOfMove(p,distance, c);
				int costsForMove = calculateCostsOfMove(p,distance, c);
				distance+= distanceForMove;
				costs += costsForMove;
				if((path.get(p.getNewLocation()+distanceForMove)).hasPawn())
				{
					ArrayList<Card> cardsForNextIteration = new ArrayList<Card>(cardsOnHand);
					cardsForNextIteration.remove(c);
					
					ArrayList<Pawn> pawnsForNextIteration = new ArrayList<Pawn>();
					pawnsForNextIteration.add(p);
					
					ArrayList<Card> cardsAlreadyPlayedForNextIteration = new ArrayList<Card>(cardsAlreadyPlayed);
					
					doAMove(pawnsForNextIteration, cardsForNextIteration,cardsAlreadyPlayedForNextIteration, distance,costs);
				}
				else
				{
					int points = calculatePointsOfTurn(p,distance);
					double valueOfTurn = points + (aiSpeed * (avgDistanceOtherPlayers/avgDistanceMe)*distance)+ aiPawnSpread/(avgDistanceMe - p.getNewLocation());
					if(valueOfTurn > valueBestTurn||bestCards.isEmpty())
					{
						valueBestTurn = valueOfTurn;
						bestPawn = p;
						bestCards = new ArrayList<Card>(cardsAlreadyPlayed);
					}
				}
			}
		}
		
	}

	private int calculateCostsOfMove(Pawn p, int distance, Card c) {

		return 0;
	}

	private int calculatePointsOfTurn(Pawn p, int distance) {
		for(int i = p.getNewLocation() + distance -1;i>=0; i--)
		{
			WaterTile wt = path.get(i);
			if(!(wt.getChildrenTiles().isEmpty()))
			{
				return (wt.getChildrenTiles().get(wt.getChildrenTiles().size()-1)).getLandValue();
			}
		}
		return 0;
	}

	private int calculateDistanceOfMove(Pawn p, int distance, Card c) {
		int startingLocation = p.getNewLocation()+distance;
		for(int i = startingLocation +1; i<path.size(); i++)
		{
			WaterTile wt = path.get(i);
			if((wt.getChildrenTiles().get(wt.getChildrenTiles().size()-1).getColor().equals(c.getColor())))
			{
				return i-startingLocation;
			}
		}
		return path.size();
	}
}
