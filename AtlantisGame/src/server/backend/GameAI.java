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
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.ServerMessage;

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
		this.aiPawnSpread = aiPawnSpread;
		this.aiSpeed = aiSpeed;
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
		
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("AI plays another card");
			game.processMessage(new PawnCardSelectedMessage(game.getName(),me.getPlayerIndex(), bestPawn, bestCards.remove(0)));
			
		}
		else if(igm instanceof RefreshPlayerMessage)
		{

			RefreshPlayerMessage rpm = (RefreshPlayerMessage)igm;
			
		}
		else if(igm instanceof ServerMessage)
		{
			System.out.println(((ServerMessage)igm).getTheMessage());
		}
	}

	private double valueBestTurn;
	private ArrayList<Card> bestCards;
	private Pawn bestPawn;
	
	private double avgDistanceOtherPlayers;
	private double avgDistanceMe;
	private void doATurn() {
		//System.out.println("New turn initiated");
		bestCards = new ArrayList<Card>();
		bestCards.clear();
		bestPawn = null;
		calculateAverageDistances();
		doAMove(me.getPawns(), me.getPlayerHand().getCards(),new ArrayList<Card>(),  0, 0);
		//System.out.println("AI sends turn message");
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
		//System.out.println("New move calculation initiated");
		
		for(Pawn p : pawns)
		{
			for(Card c : cardsOnHand)
			{
				ArrayList<Card> tempCardsPlayed = new ArrayList<Card>(cardsAlreadyPlayed);
				tempCardsPlayed.add(c);
				//System.out.println("Distance before: " + distance);
				int distanceForMove = distance + calculateDistanceOfMove(p,distance, c);
				int costsForMove = costs + calculateCostsOfMove(p,distance, c);
				if((path.size()>p.getNewLocation()+distanceForMove) && (path.get(p.getNewLocation()+distanceForMove).getChildrenTiles().get(path.get(p.getNewLocation()+distanceForMove).getChildrenTiles().size()-1).hasPawn()))
				{
					//System.out.println("plan second move");
					ArrayList<Card> cardsForNextIteration = new ArrayList<Card>(cardsOnHand);
					cardsForNextIteration.remove(c);
					
					ArrayList<Pawn> pawnsForNextIteration = new ArrayList<Pawn>();
					pawnsForNextIteration.add(p);
					if(cardsForNextIteration.size() != 0)
					{
						doAMove(pawnsForNextIteration, cardsForNextIteration,tempCardsPlayed, distanceForMove,costsForMove);
					}
				}
				else if((path.size()<=p.getNewLocation()+distanceForMove)||
						((path.size()>p.getNewLocation()+distanceForMove)
								&&(!path.get(p.getNewLocation()+distanceForMove).getChildrenTiles().get(path.get(p.getNewLocation()+distanceForMove).getChildrenTiles().size()-1).hasPawn())))
				{
					int points = calculatePointsOfTurn(p,distanceForMove);
					double valueOfTurn = points + (aiSpeed * ((avgDistanceOtherPlayers+2)/(avgDistanceMe+2))*distanceForMove)/*+ aiPawnSpread*(avgDistanceMe - p.getNewLocation())*/;
					//System.out.println("New calculation for turn: " + valueOfTurn + " points: " + points +" Comparing to: " + valueBestTurn);
					if(valueOfTurn > valueBestTurn||bestCards.isEmpty())
					{
						valueBestTurn = valueOfTurn;
						bestPawn = p;
						bestCards = new ArrayList<Card>(tempCardsPlayed);
						//System.out.println("New best turn detected " + valueBestTurn + " points: " + points + " speed: " + (aiSpeed * ((avgDistanceOtherPlayers+2)/(avgDistanceMe+2))*distanceForMove)+ " spread: " + aiPawnSpread*(avgDistanceMe - p.getNewLocation()));
						//System.out.println("Average other players: " + avgDistanceOtherPlayers+ " AVGMe: "+ avgDistanceMe);
						//System.out.println("Distance: " + distanceForMove);
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
			if(!(wt.getChildrenTiles().isEmpty())&&(wt.getChildrenTiles().get(wt.getChildrenTiles().size()-1).getColor().equals(c.getColor())))
			{
				return i-startingLocation;
			}
		
		}
		return path.size()-startingLocation;
	}
}
