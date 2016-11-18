package server.backend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
import messageObjects.turnMessages.PaymentDoneMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.ServerMessage;
import messageObjects.turnMessages.WaterPaidMessage;

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
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doATurn();
			}
		}
		else if(igm instanceof PlayAnotherCardMessage)
		{
		
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println("AI plays another card");
			moves+=1;
			game.processMessage(new PawnCardSelectedMessage(game.getName(),me.getPlayerIndex(), bestPawn, bestCards.remove(0)));
			
		}
		else if(igm instanceof RefreshPlayerMessage)
		{

			RefreshPlayerMessage rpm = (RefreshPlayerMessage)igm;
			if (rpm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex()&&rpm.getWaterBill()>0)
			{
				ArrayList<LandTile> tilesForPayment = null;
				ArrayList<Card> cardsForPayment = null;
				if(payments != null && payments.getTilesPaidInEachMove().containsKey(moves))
				{
					tilesForPayment = payments.getTilesPaidInEachMove().remove(moves);
				}
				if(payments != null &&payments.getCardsPaidInEachMove().containsKey(moves))
				{
					cardsForPayment = payments.getCardsPaidInEachMove().remove(moves);
				}
				if(cardsForPayment != null && !cardsForPayment.isEmpty() /*|| tilesForPayment != null*/)
				{
					System.out.println("Send payment message ");
					for(LandTile lt:tilesForPayment)
					{
						System.out.println("Tile with value: " + lt.getLandValue());
					}
					WaterPaidMessage wpm = new WaterPaidMessage(game.getName(),me.getPlayerIndex(),tilesForPayment,cardsForPayment);
					game.processMessage(wpm);
				}
			}
		}
		else if(igm instanceof ServerMessage)
		{
			System.out.println(((ServerMessage)igm).getTheMessage());
		}
	}

	private double valueBestTurn;
	private ArrayList<Card> bestCards;
	private ArrayList<Integer> bestTurnCosts;
	private Pawn bestPawn;
	private HashMap<Integer, ArrayList<LandTile>> LandTilesPayments;
	private HashMap<Integer, ArrayList<Card>> CardPayments;
	private double avgDistanceOtherPlayers;
	private double avgDistanceMe;
	private AICostObject payments;
	private int moves;
	private void doATurn() {
		//System.out.println("New turn initiated");
		bestCards = new ArrayList<Card>();
		bestCards.clear();
		bestPawn = null;
		payments = null;
		moves = 0;
		calculateAverageDistances();
		AITurnObject thisTurn = doMove(new AITurnObject(me.getPawns(), me.getPlayerHand().getCards(),me.getPlayerHand().getTreasures()),1);
		bestCards = thisTurn.getCardsAlreadyPlayed();
		bestPawn = thisTurn.getPawnsThatCanBePlayed().get(0);
		payments = thisTurn.getCostsIncurredPerMove();
		getValueOfCosts(payments);
		System.out.println("Showing Indexes");
		/*if(payments != null && payments.getTilesPaidInEachMove() !=null)
		{
			for(Integer intg : payments.getTilesPaidInEachMove().keySet())
			{
			System.out.println("Index: " + intg);
			}
		}*/
		//System.out.println("AI sends turn message");
		if(bestPawn != null && !bestCards.isEmpty())
		{
			moves+=1;
			game.processMessage(new PawnCardSelectedMessage(game.getName(),me.getPlayerIndex(),bestPawn, bestCards.remove(0)));
			
		}
		else
		{
			System.out.println("Can't make a turn");
			giveUpTurn();
		}

	}
	


	private void giveUpTurn() {
		// TODO Auto-generated method stub
		
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

	private AITurnObject doMove(AITurnObject inputForMove, int moveNumber)
	{
		AITurnObject bestPossibleTurn = null;
		for(Pawn p : inputForMove.getPawnsThatCanBePlayed())
		{
			for(Card c : inputForMove.getCardsOnHandLeft())
			{
				ArrayList<Card> cardsAlreadyPlayed = new ArrayList<Card>(inputForMove.getCardsAlreadyPlayed());
				cardsAlreadyPlayed.add(c);
				ArrayList<Card> cardsLeftOnHand = new ArrayList<Card>(inputForMove.getCardsOnHandLeft());
				cardsLeftOnHand.remove(c);
				
				ArrayList<LandTile> tilesLeftInHand = new ArrayList<LandTile>(inputForMove.getLandTilesLeftOnHand());
				HashMap<Integer,ArrayList<LandTile>> tilesPaidInEachMove = new HashMap<Integer,ArrayList<LandTile>>();

				for(Integer e: inputForMove.getCostsIncurredPerMove().getTilesPaidInEachMove().keySet())
				{
					ArrayList<LandTile> copy = new ArrayList<LandTile>(inputForMove.getCostsIncurredPerMove().getTilesPaidInEachMove().get(e));
					tilesPaidInEachMove.put(e, copy);
				}

				HashMap<Integer,ArrayList<Card>> cardsPaidInEachMove = new HashMap<Integer,ArrayList<Card>>(inputForMove.getCostsIncurredPerMove().getCardsPaidInEachMove());
				int distanceAlreadyTraveled = inputForMove.getDistanceTraveled() + calculateDistanceOfMove(p,inputForMove.getDistanceTraveled(), c);
				int costsForThisMove =calculateCostsOfMove(p,inputForMove.getDistanceTraveled(), distanceAlreadyTraveled);
				int costsAlreadyIncurred = inputForMove.getCostsIncurred() + costsForThisMove;
				AICostObject costs = new AICostObject(tilesPaidInEachMove, inputForMove.getCostsIncurredPerMove().getCardsPaidInEachMove());
				
				boolean canBePaid = true;
				if(costsForThisMove>0)
				{
					ArrayList<Card> newCostsCards = new ArrayList<Card>();
					ArrayList<LandTile> tilesCostCards = new ArrayList<LandTile>();
					costs.getCardsPaidInEachMove().put(moveNumber, newCostsCards);
					costs.getTilesPaidInEachMove().put(moveNumber, tilesCostCards);
					costs = determineTilesToPay(costs, cardsLeftOnHand, tilesLeftInHand,costsForThisMove,moveNumber);
					if(costs == null)
					{
						canBePaid = false;
					}
					if(canBePaid && costs.getTilesPaidInEachMove().containsKey(moveNumber)&& costs.getTilesPaidInEachMove().get(moveNumber)!= null)
					{
						for(LandTile t : costs.getTilesPaidInEachMove().get(moveNumber))
						{
							tilesLeftInHand.remove(t);
						}
					}
					if(canBePaid&& costs.getCardsPaidInEachMove().containsKey(moveNumber)&&costs.getCardsPaidInEachMove().get(moveNumber) !=null)
					{
						for(Card crd : costs.getCardsPaidInEachMove().get(moveNumber))
						{
							cardsLeftOnHand.remove(crd);
						}
					}
				}
				if(canBePaid&&(path.size()>p.getNewLocation()+distanceAlreadyTraveled) && 
						(((LandTile) ((path.get(p.getNewLocation()+distanceAlreadyTraveled).getChildren().get(path.get(p.getNewLocation()+distanceAlreadyTraveled).getChildren().size()-1)))).hasPawn())
						/*&&costsAlreadyIncurred < 0.25 * me.getPlayerHand().getNumCards() + me.getPlayerHand().getTreasuresValue(me.getPlayerHand().getTreasures())"*/)
				{
					//System.out.println("plan second move");
			
					
					ArrayList<Pawn> pawnsForNextIteration = new ArrayList<Pawn>();
					pawnsForNextIteration.add(p);
					if(cardsLeftOnHand.size() != 0)
					{
						int updatedMoveNumber = moveNumber + 1;
						AITurnObject output = doMove(new AITurnObject(pawnsForNextIteration,
								cardsLeftOnHand,cardsAlreadyPlayed,
								tilesLeftInHand, costs,
								distanceAlreadyTraveled,costsAlreadyIncurred),updatedMoveNumber);
						if(output != null&&(bestPossibleTurn == null||output.getValueOfTurn() > bestPossibleTurn.getValueOfTurn()))
						{
							bestPossibleTurn = output;
						}
					}
				}
				else if(canBePaid)
				{
					int points = calculatePointsOfTurn(p,distanceAlreadyTraveled);
					double valueOfTurn = points - costsAlreadyIncurred + (aiSpeed * ((avgDistanceOtherPlayers+2)/(avgDistanceMe+2))*distanceAlreadyTraveled);
					//System.out.println("New calculation for turn: " + valueOfTurn + " points: " + points +" Comparing to: " + valueBestTurn);
					if(bestPossibleTurn == null||valueOfTurn > bestPossibleTurn.getValueOfTurn())
					{
						ArrayList<Pawn> pawn = new ArrayList<Pawn>();
						pawn.add(p);
						bestPossibleTurn = new AITurnObject(pawn, cardsLeftOnHand,cardsAlreadyPlayed,tilesLeftInHand,costs,
								distanceAlreadyTraveled,costsAlreadyIncurred);
						bestPossibleTurn.setValueOfTurn(valueOfTurn);
						//int i = getValueOfCosts(costs);
						//System.out.println("Total value of payment: " + i + " costs: " + costsForThisMove);
						//System.out.println("New best turn detected " + valueBestTurn + " points: " + points + " speed: " + (aiSpeed * ((avgDistanceOtherPlayers+2)/(avgDistanceMe+2))*distanceForMove)+ " spread: " + aiPawnSpread*(avgDistanceMe - p.getNewLocation()));
						//System.out.println("Average other players: " + avgDistanceOtherPlayers+ " AVGMe: "+ avgDistanceMe);
						//System.out.println("Distance: " + distanceForMove);
					}
					
				}
			}
		}
		return bestPossibleTurn;
	}
	private int getValueOfCosts(AICostObject costs) {
		int i = 0;
		System.out.println("Starting cost calc.");
		for(Integer e:costs.getTilesPaidInEachMove().keySet())
		{
			int moveCosts = 0;
			System.out.println("Getting values from key " + e);
			for(LandTile lt:costs.getTilesPaidInEachMove().get(e))
			{
				moveCosts+=lt.getLandValue();
			}
			System.out.println("Move: " + e +" Costs paid: " + moveCosts);
			i+=moveCosts;
		}
		return i;
	}

	/*private AICostObject determineTilesToPay(AICostObject input, ArrayList<Card> cardsLeftInHand, ArrayList<LandTile> tilesLeftInHand, int paymentSize, int moveNumber) {
		System.out.println("Start with calculation of costs for move " + moveNumber);
		AICostObject bestPayment = null;
		for(LandTile lt: tilesLeftInHand)
		{
			int costsLeftToPay = paymentSize - lt.getLandValue();
			ArrayList<LandTile> tempTilesLeftOnHand = new ArrayList<LandTile>(tilesLeftInHand);
			tempTilesLeftOnHand.remove(lt);
			ArrayList<LandTile> updatedMovePayment = new ArrayList<LandTile>(input.getTilesPaidInEachMove().get(moveNumber));
			updatedMovePayment.add(lt);
			HashMap<Integer,ArrayList<LandTile>> updatedTilesCostList = new HashMap<Integer,ArrayList<LandTile>>();
			for (Integer e:input.getTilesPaidInEachMove().keySet())
			{
				ArrayList<LandTile> copy = new ArrayList<LandTile>(input.getTilesPaidInEachMove().get(e));
				updatedTilesCostList.put(e, copy);
			}
			updatedTilesCostList.remove(moveNumber);
			updatedTilesCostList.put(moveNumber, updatedMovePayment);
			AICostObject updatedPayment = new AICostObject(updatedTilesCostList,input.getCardsPaidInEachMove());
			
			if(costsLeftToPay <= 0)
			{
				if(bestPayment == null || bestPayment.getRealCosts(moveNumber)>updatedPayment.getRealCosts(moveNumber))
				{
					bestPayment = updatedPayment;
					System.out.println("new payment calc: " +bestPayment.getTilesPaidInEachMove().size() + " " + bestPayment.getTilesPaidInEachMove().get(moveNumber).size());
				}
			}
			/*else if(costsLeftToPay <= cardsLeftInHand.size())
			{
				ArrayList<Card> cardsUsedInPayment = new ArrayList<Card>();
				for(int i=0;i<costsLeftToPay;i++)
				{
					cardsUsedInPayment.add(cardsLeftInHand.get(i));
				}
				HashMap<Integer,ArrayList<Card>> updatedCardCostList = new HashMap<Integer,ArrayList<Card>>(input.getCardsPaidInEachMove());
				updatedCardCostList.put(moveNumber, cardsUsedInPayment);
				AICostObject paymentIncludingCards = new AICostObject(updatedTilesCostList, updatedCardCostList);
				if(bestPayment == null || paymentIncludingCards.getRealCosts(moveNumber) < bestPayment.getRealCosts(moveNumber))
				{
					bestPayment = paymentIncludingCards;
					
				}
				AICostObject finishedPayment =determineTilesToPay(updatedPayment,cardsLeftInHand, tempTilesLeftOnHand,costsLeftToPay,moveNumber);
				if(bestPayment == null || finishedPayment.getRealCosts(moveNumber)< bestPayment.getRealCosts(moveNumber))
				{
					bestPayment = finishedPayment;
				}
			}/
			else
			{
				
				AICostObject finishedPayment = determineTilesToPay(updatedPayment,cardsLeftInHand,tempTilesLeftOnHand,costsLeftToPay,moveNumber);
				if(finishedPayment != null &&(bestPayment == null || finishedPayment.getRealCosts(moveNumber)< bestPayment.getRealCosts(moveNumber)))
				{
					bestPayment = finishedPayment;
					System.out.println("new payment calc: " +bestPayment.getTilesPaidInEachMove().size() + " " + bestPayment.getTilesPaidInEachMove().get(moveNumber).size());
					
				}
			}
		}
		return bestPayment;
		
	}*/
	private AICostObject determineTilesToPay(AICostObject input, ArrayList<Card> cardsLeftInHand, ArrayList<LandTile> tilesLeftInHand, int paymentSize, int moveNumber) {
		AICostObject bestPayment = null;
		for(LandTile lt:tilesLeftInHand)
		{
			ArrayList<LandTile> updatedTilesLeftInHand = new ArrayList<LandTile>(tilesLeftInHand);
			updatedTilesLeftInHand.remove(lt);
			ArrayList<LandTile> updatedTilePaymentForThisMove = new ArrayList<LandTile>(input.getTilesPaidInEachMove().get(moveNumber));
			updatedTilePaymentForThisMove.add(lt);
			int updatedPaymentSize = paymentSize -lt.getLandValue();
			HashMap<Integer, ArrayList<LandTile>> updatedTilePayment = new HashMap<Integer, ArrayList<LandTile>>();
			for(Integer e : input.getTilesPaidInEachMove().keySet())
			{
				if(e != moveNumber)
				{
					ArrayList<LandTile> copy = input.getTilesPaidInEachMove().get(e);
					updatedTilePayment.put(e, copy);
				}
			}
			updatedTilePayment.put(moveNumber, updatedTilePaymentForThisMove);
			AICostObject updatedCosts = new AICostObject(updatedTilePayment, input.getCardsPaidInEachMove());
			//Iterative
			if(updatedPaymentSize >0&&updatedTilesLeftInHand.size() > 0)
			{
				AICostObject finalPayment = determineTilesToPay(updatedCosts,cardsLeftInHand,updatedTilesLeftInHand,updatedPaymentSize, moveNumber);
				if (bestPayment == null || finalPayment.getRealCosts(moveNumber) < bestPayment.getRealCosts(moveNumber))
				{
					bestPayment = finalPayment;
				}
			}
			else if(updatedPaymentSize <=0)
			{
				if (bestPayment == null || updatedCosts.getRealCosts(moveNumber) < bestPayment.getRealCosts(moveNumber))
				{
					bestPayment = updatedCosts;
					//System.out.println("New best payment detected: " + updatedCosts.getRealCosts(moveNumber) + " Costs left: " + updatedPaymentSize);
				}
			}
		}
		return bestPayment;
	}
	private int calculateCostsOfMove(Pawn p, int distanceTraveledBefore, int distanceTraveledAfter) {
		int totalCosts = 0;
		int costsForThisWater =0;
		boolean isInWater = false;
		boolean isBeginningWater = false;
		for(int i = p.getNewLocation() + distanceTraveledBefore +1;i<=p.getNewLocation() + distanceTraveledAfter; i++)
		{
			if(i<path.size())
			{
				WaterTile wt = path.get(i);
				if(wt.getChildren().isEmpty()&&!isInWater&&i>0 && !isBeginningWater)
				{
					isInWater = true;
					costsForThisWater = ((LandTile)path.get(i-1).getChildren().get(path.get(i-1).getChildren().size()-1)).getLandValue();
				}
				else if((!wt.getChildren().isEmpty())&&(isInWater)&&(!isBeginningWater))
				{
					isInWater= false;
					if(((LandTile)wt.getChildren().get(wt.getChildren().size()-1)).getLandValue() < costsForThisWater)
					{
						costsForThisWater = ((LandTile)wt.getChildren().get(wt.getChildren().size()-1)).getLandValue();
					}
					totalCosts+=costsForThisWater;
					//System.out.println("Could add " + costsForThisWater + " to these total costs: " + totalCosts);
				}

				else if(wt.getChildren().isEmpty() && i==0)
				{
					isBeginningWater = true;
				}
				else if((!wt.getChildren().isEmpty())&&isBeginningWater)
				{
					isBeginningWater = false;
				}
			}
			else if(isInWater)
			{
				isInWater = false;
			}
		}
		return totalCosts; 
	}

	private int calculatePointsOfTurn(Pawn p, int distance) {
		for(int i = p.getNewLocation() + distance -1;i>=0; i--)
		{
			WaterTile wt = path.get(i);
			if(!(wt.getChildren().isEmpty())&&!(((LandTile)wt.getChildren().get(wt.getChildren().size()-1))).hasPawn())
			{
				return ((LandTile)(wt.getChildren().get(wt.getChildren().size()-1))).getLandValue();
			}
		}
		return 0;
	}

	private int calculateDistanceOfMove(Pawn p, int distance, Card c) {
		int startingLocation = p.getNewLocation()+distance;
		for(int i = startingLocation +1; i<path.size(); i++)
		{
			WaterTile wt = path.get(i);
			if(!(wt.getChildren().isEmpty())&&((LandTile)wt.getChildren().get(wt.getChildren().size()-1)).getColor().equals(c.getColor()))
			{
				return i-startingLocation;
			}
		
		}
		return path.size()-startingLocation;
	}
}
