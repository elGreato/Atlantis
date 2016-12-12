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
import messageObjects.turnMessages.BuyCardsMessage;
import messageObjects.turnMessages.CardsBoughtMessage;
import messageObjects.turnMessages.EndMYTurnMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.LastBillMessage;
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

	public String getGameName() {
		return game.getName();
	}

	public GameAI(GameInterface game, double aiSpeed, double aiPawnSpread) {
		this.game = game;
		this.aiPawnSpread = aiPawnSpread;
		this.aiSpeed = aiSpeed;
	}

	public void processMessage(InGameMessage igm) {
		if (igm instanceof WaterMessage) {
			path = ((WaterMessage) igm).getBase();
		} else if (igm instanceof PlayerMessage) {
			me = ((PlayerMessage) igm).getPlayer();
		} else if (igm instanceof OpponentMessage) {
			opponentInfo = ((OpponentMessage) igm).getOpponents();
		} else if (igm instanceof GameStatusMessage) {
			GameStatusMessage gsm = (GameStatusMessage) igm;
			if (gsm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex()) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				doATurn(false);
			}
		} else if (igm instanceof PlayAnotherCardMessage) {

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			game.processMessage(
					new PawnCardSelectedMessage(game.getName(), me.getPlayerIndex(), bestPawn, bestCards.remove(0)));

		} else if (igm instanceof RefreshPlayerMessage) {
			
			RefreshPlayerMessage rpm = (RefreshPlayerMessage) igm;
			doPayment(rpm);

			
		} else if (igm instanceof ServerMessage) {
			System.out.println(((ServerMessage) igm).getTheMessage());
			
			
		} else if (igm instanceof LastBillMessage) {
			System.out.println("Last Bill received by AI");
			int totalPayment = ((LastBillMessage)igm).getWaterBill();
			System.out.println("Total payment for last bill" + totalPayment);
			int valueOfHand = 0;
			valueOfHand += me.getPlayerHand().getNumCards();
			for(LandTile lt : me.getPlayerHand().getTreasures())
			{
				valueOfHand += lt.getLandValue();
			}
			if(totalPayment > 0 && valueOfHand <= totalPayment)
			{
				System.out.println("Pay with everything");
				WaterPaidMessage wpm = new WaterPaidMessage(game.getName(), me.getPlayerIndex(), me.getPlayerHand().getTreasures(), me.getPlayerHand().getCards(),true);
				game.processMessage(wpm);
			}
			else if (totalPayment > 0)
			{

				HashMap<Integer, ArrayList<LandTile>> tilesToPay =new HashMap<Integer, ArrayList<LandTile>>();
				tilesToPay.put(0, new ArrayList<LandTile>());
				HashMap<Integer, ArrayList<Card>>cardsToPay = new HashMap<Integer, ArrayList<Card>>();
				cardsToPay.put(0, new ArrayList<Card>());
				AICostObject payment = determineTilesToPay(new AICostObject(tilesToPay,cardsToPay),me.getPlayerHand().getCards(),me.getPlayerHand().getTreasures(),totalPayment,0,true);
				
				System.out.println("Send payment message. Payable amount = " + ((LastBillMessage)igm).getWaterBill());
				System.out.println("Payment size: Tiles: " +payment.getTilesPaidInEachMove().get(0).size() + " Cards: " + payment.getCardsPaidInEachMove().get(0).size());
				for (LandTile lt : payment.getTilesPaidInEachMove().get(0)) {
					System.out.println("Tile with value: " + lt.getLandValue());
				}
				
				WaterPaidMessage wpm = new WaterPaidMessage(game.getName(),me.getPlayerIndex(), payment.getTilesPaidInEachMove().get(0),payment.getCardsPaidInEachMove().get(0),true);
				game.processMessage(wpm);
			}
		} else if(igm instanceof CardsBoughtMessage)
		{
			CardsBoughtMessage cbm = (CardsBoughtMessage)igm;
			if(cbm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex())
			{
				System.out.println(me.getPlayerName() + " has successfully bought "+ cbm.getPurchase().size() + " cards");
				doATurn(true);
				
			}
			
		}
	}

	private void doPayment(RefreshPlayerMessage rpm) {
		// TODO Auto-generated method stub
		if (rpm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex() && rpm.getWaterBill() > 0) {

			System.out.println(me.getPlayerName() + 
					" Payment message arrived...." + " Waterbill: " + rpm.getWaterBill() + " moveNumber: " + moves);
			ArrayList<LandTile> tilesForPayment = null;
			ArrayList<Card> cardsForPayment = null;
		
			tilesForPayment = payments.getTilesPaidInEachMove().remove(moves);
		
			cardsForPayment = payments.getCardsPaidInEachMove().remove(moves);
			
			System.out.println("Payment size: Tiles: " + tilesForPayment.size() + " Cards: " + cardsForPayment.size());
			if (tilesForPayment != null && !tilesForPayment
					.isEmpty() || (cardsForPayment != null && !cardsForPayment.isEmpty())) {
				System.out.println("Send payment message. Payable amount = " + rpm.getWaterBill());
				for (LandTile lt : tilesForPayment) {
					System.out.println("Tile with value: " + lt.getLandValue());
				}
				if(rpm.isNextPlayer())
				{
					WaterPaidMessage wpm = new WaterPaidMessage(game.getName(), me.getPlayerIndex(), tilesForPayment,
							cardsForPayment, true);
					game.processMessage(wpm);
				}
				else
				{
					WaterPaidMessage wpm = new WaterPaidMessage(game.getName(), me.getPlayerIndex(), tilesForPayment,
							cardsForPayment, false);
					game.processMessage(wpm);
				}
			}
			moves += 1;
		} else if (rpm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex()) {
			
			moves += 1;
			if(rpm.isNextPlayer())
			{
				game.processMessage(new EndMYTurnMessage(game.getName(),me.getPlayerIndex(),true));
			}
		}
	}

	private void doATurn(boolean alreadyBoughtCards) {
		boolean cardsRequiredToBuy = true;
		if(!alreadyBoughtCards)
		{
			cardsRequiredToBuy = buyCardsIfRequired();
		}
		if(!cardsRequiredToBuy ||alreadyBoughtCards)
		{
			planMoves();
		}
		

	}

	private void planMoves() {
		bestCards = new ArrayList<Card>();
		bestCards.clear();
		bestPawn = null;
		payments = null;
		moves = 0;
		calculateAverageDistances();
		AITurnObject thisTurn = determineBestMoves(
				new AITurnObject(me.getPawns(), me.getPlayerHand().getCards(), me.getPlayerHand().getTreasures()), 1);
	
		if (thisTurn != null && thisTurn.getCardsAlreadyPlayed() != null
				&& !thisTurn.getCardsAlreadyPlayed().isEmpty()) {
			bestCards = thisTurn.getCardsAlreadyPlayed();
			bestPawn = thisTurn.getPawnsThatCanBePlayed().get(0);
			payments = thisTurn.getCostsIncurredPerMove();
			moves += 1;
			System.out.println(me.getPlayerName() + " has " + me.getPlayerHand().getNumCards() + " cards");
			game.processMessage(
					
					new PawnCardSelectedMessage(game.getName(), me.getPlayerIndex(), bestPawn, bestCards.remove(0)));
					
		} else {
			System.out.println("Can't make a turn");
			giveUpTurn();
		}
		
	}

	private boolean buyCardsIfRequired() {
		if(me.getPlayerHand().getNumCards() < 2)
		{
			System.out.println("Should buy cards");
			ArrayList<LandTile> payment = new ArrayList<LandTile>();
			payment = determineTreasuresForBuyProcess(2,me.getPlayerHand().getTreasures(), new ArrayList<LandTile>());
			
			if(payment != null &&payment.size()>0 &&determineValueOfLandTiles(payment)>1)
			{
				System.out.println("Will buy cards");
				BuyCardsMessage bcm = new BuyCardsMessage(game.getName(),me.getPlayerIndex(),payment);
				game.processMessage(bcm);
				return true;
			}
		}
		System.out.println("Will not buy Cards");
		return false;
		
		
	}

	private ArrayList<LandTile> determineTreasuresForBuyProcess(int desiredAmount, ArrayList<LandTile> tilesLeftOnHand, ArrayList<LandTile>payment) {
		ArrayList<LandTile> bestPayment = null;
		int valueDesired = 2*desiredAmount;
		for(LandTile lt: tilesLeftOnHand)
		{
			ArrayList<LandTile> updatedTilesLeftOnHand = new ArrayList<LandTile>(tilesLeftOnHand);
			ArrayList<LandTile> updatedPayment = new ArrayList<LandTile>(payment);
			updatedPayment.add(lt);
			updatedTilesLeftOnHand.remove(lt);
			int valueOfBestPayment = 0;
			if(bestPayment != null)
			{
				valueOfBestPayment = determineValueOfLandTiles(bestPayment);
			}
			
			int valueOfUpdatedPayment = determineValueOfLandTiles(updatedPayment);
			if(bestPayment == null||bestPayment.isEmpty()
					||(valueOfUpdatedPayment % 2 == 0 &&valueOfBestPayment%2==1)
					||(valueOfUpdatedPayment % 2 == valueOfBestPayment % 2
						&& Math.abs(valueOfUpdatedPayment-valueDesired) < Math.abs(valueOfBestPayment-valueDesired)))
			{
				bestPayment = updatedPayment;
			}
			if(valueOfUpdatedPayment < valueDesired)
			{
				int valueOfAgainUpdatedPayment = 0;
				ArrayList<LandTile> againUpdatedPayment = determineTreasuresForBuyProcess(desiredAmount, updatedTilesLeftOnHand, updatedPayment);
				if(againUpdatedPayment != null && 
						(bestPayment == null||bestPayment.isEmpty()
						||(valueOfAgainUpdatedPayment % 2 == 0 &&valueOfBestPayment % 2==1)
						||(valueOfAgainUpdatedPayment % 2 == valueOfBestPayment % 2 
							&& Math.abs(valueOfAgainUpdatedPayment-valueDesired) < Math.abs(valueOfBestPayment-valueDesired))))
				{
					bestPayment = againUpdatedPayment;
				}
			}
		}
		
		return bestPayment;
	}
	

	private int determineValueOfLandTiles(ArrayList<LandTile> bestPayment) {
		int valueOfPayment = 0;
		for(LandTile lt2:bestPayment)
		{
			valueOfPayment += lt2.getLandValue();
		}
		return valueOfPayment;
	}

	private void giveUpTurn() {
		game.processMessage(new EndMYTurnMessage(game.getName(), me.getPlayerIndex(),false));

	}

	private void calculateAverageDistances() {
		avgDistanceOtherPlayers = 0;
		avgDistanceMe = 0;
		for (Player pl : opponentInfo) {
			for (Pawn pa : pl.getPawns()) {
				avgDistanceOtherPlayers += pa.getNewLocation();
			}
		}
		avgDistanceOtherPlayers /= (3 * opponentInfo.size());
		for (Pawn pa : me.getPawns()) {
			avgDistanceMe += pa.getNewLocation();
		}
		avgDistanceMe /= 3;
	}

	private AITurnObject determineBestMoves(AITurnObject inputForMove, int moveNumber)
	{
		AITurnObject bestPossibleTurn = null;
		for(Pawn p : inputForMove.getPawnsThatCanBePlayed())
		{
			if(p.getNewLocation() < 53)
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
					AICostObject costs = new AICostObject(tilesPaidInEachMove, cardsPaidInEachMove);
					
					boolean canBePaid = true;
					if(costsForThisMove>0)
					{
						ArrayList<Card> newCostsCards = new ArrayList<Card>();
						ArrayList<LandTile> tilesCostCards = new ArrayList<LandTile>();
						costs.getCardsPaidInEachMove().put(moveNumber, newCostsCards);
						costs.getTilesPaidInEachMove().put(moveNumber, tilesCostCards);
						costs = determineTilesToPay(costs, cardsLeftOnHand, tilesLeftInHand,costsForThisMove,moveNumber, false);
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
							AITurnObject output = determineBestMoves(new AITurnObject(pawnsForNextIteration,
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
						double valueOfTurn = (points - costsAlreadyIncurred - 2*cardsAlreadyPlayed.size()) + (aiSpeed * ((avgDistanceOtherPlayers+2)/(avgDistanceMe+2))*distanceAlreadyTraveled)+(aiPawnSpread*(avgDistanceMe- p.getNewLocation()));
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
		}
		return bestPossibleTurn;
	}

	private AICostObject determineTilesToPay(AICostObject input, ArrayList<Card> cardsLeftInHand,
			ArrayList<LandTile> tilesLeftInHand, int paymentSize, int moveNumber, boolean isLastPayment) {
		AICostObject bestPayment = null;
		if(paymentSize <= cardsLeftInHand.size())
		{
			ArrayList<Card> cardsToPay = new ArrayList<Card>();
			for(int i = 0; i<paymentSize; i++)
			{
				cardsToPay.add(cardsLeftInHand.get(i));
			}
			HashMap<Integer, ArrayList<Card>> updatedCardPayment = new HashMap<Integer, ArrayList<Card>>();
			for (Integer e : input.getCardsPaidInEachMove().keySet()) {
				if (e != moveNumber) {
					ArrayList<Card> copy = input.getCardsPaidInEachMove().get(e);
					updatedCardPayment.put(e, copy);
				}
			}
			updatedCardPayment.put(moveNumber, cardsToPay);
			AICostObject paymentWithCards = new AICostObject(input.getTilesPaidInEachMove(), updatedCardPayment);
			if(bestPayment == null
					||paymentWithCards.getRealCosts(moveNumber, isLastPayment)< bestPayment.getRealCosts(moveNumber, isLastPayment))
			{
				bestPayment = paymentWithCards;
			}
		}
		for (LandTile lt : tilesLeftInHand) {
			//Copy object
			ArrayList<LandTile> updatedTilesLeftInHand = new ArrayList<LandTile>(tilesLeftInHand);
			updatedTilesLeftInHand.remove(lt);
			ArrayList<LandTile> updatedTilePaymentForThisMove = new ArrayList<LandTile>(
					input.getTilesPaidInEachMove().get(moveNumber));
			updatedTilePaymentForThisMove.add(lt);
			int updatedPaymentSize = paymentSize - lt.getLandValue();
			HashMap<Integer, ArrayList<LandTile>> updatedTilePayment = new HashMap<Integer, ArrayList<LandTile>>();
			for (Integer e : input.getTilesPaidInEachMove().keySet()) {
				if (e != moveNumber) {
					ArrayList<LandTile> copy = input.getTilesPaidInEachMove().get(e);
					updatedTilePayment.put(e, copy);
				}
			}
			updatedTilePayment.put(moveNumber, updatedTilePaymentForThisMove);
			AICostObject updatedCosts = new AICostObject(updatedTilePayment, input.getCardsPaidInEachMove());
			
			// Iterative
			// Payment value not reached
			if (updatedPaymentSize > 0) {
				//Try to pay with cards if possible
				/*if(updatedPaymentSize <= cardsLeftInHand.size())
				{
					ArrayList<Card> cardsToPay = new ArrayList<Card>();
					for(int i = 0; i<updatedPaymentSize; i++)
					{
						cardsToPay.add(cardsLeftInHand.get(i));
					}
					HashMap<Integer, ArrayList<Card>> updatedCardPayment = new HashMap<Integer, ArrayList<Card>>();
					for (Integer e : input.getCardsPaidInEachMove().keySet()) {
						if (e != moveNumber) {
							ArrayList<Card> copy = input.getCardsPaidInEachMove().get(e);
							updatedCardPayment.put(e, copy);
						}
					}
					updatedCardPayment.put(moveNumber, cardsToPay);
					AICostObject paymentWithCards = new AICostObject(updatedTilePayment, updatedCardPayment);
					if(bestPayment == null
							||paymentWithCards.getRealCosts(moveNumber, isLastPayment)< bestPayment.getRealCosts(moveNumber, isLastPayment))
					{
						bestPayment = paymentWithCards;
					}
				}*/
				AICostObject finalPayment = determineTilesToPay(updatedCosts, cardsLeftInHand, updatedTilesLeftInHand,
						updatedPaymentSize, moveNumber, isLastPayment);
				if (finalPayment != null && (bestPayment == null
						|| finalPayment.getRealCosts(moveNumber,isLastPayment) < bestPayment.getRealCosts(moveNumber,isLastPayment))) {
					bestPayment = finalPayment;
				}
				
				
				//Payment value reached
			} else if (updatedPaymentSize <= 0) {
				if (bestPayment == null
						|| updatedCosts.getRealCosts(moveNumber,isLastPayment) < bestPayment.getRealCosts(moveNumber,isLastPayment)) {
					bestPayment = updatedCosts;
					// System.out.println("New best payment detected: " +
					// updatedCosts.getRealCosts(moveNumber) + " Costs left: " +
					// updatedPaymentSize);
				}
			}
		}
		return bestPayment;
	}

	private int calculateCostsOfMove(Pawn p, int distanceTraveledBefore, int distanceTraveledAfter) {
		int totalCosts = 0;
		int costsForThisWater = 0;
		boolean isInWater = false;
		boolean isBeginningWater = false;
		for (int i = p.getNewLocation() + distanceTraveledBefore + 1; i <= p.getNewLocation()
				+ distanceTraveledAfter; i++) {
			if (i < path.size()) {
				WaterTile wt = path.get(i);
				if (wt.getChildren().isEmpty() && !isInWater && i > 0 && !isBeginningWater) {
					isInWater = true;
					costsForThisWater = ((LandTile) path.get(i - 1).getChildren()
							.get(path.get(i - 1).getChildren().size() - 1)).getLandValue();
				} else if ((!wt.getChildren().isEmpty()) && (isInWater) && (!isBeginningWater)) {
					isInWater = false;
					if (((LandTile) wt.getChildren().get(wt.getChildren().size() - 1))
							.getLandValue() < costsForThisWater) {
						costsForThisWater = ((LandTile) wt.getChildren().get(wt.getChildren().size() - 1))
								.getLandValue();
					}
					totalCosts += costsForThisWater;
					// System.out.println("Could add " + costsForThisWater + "
					// to these total costs: " + totalCosts);
				}

				else if (wt.getChildren().isEmpty() && i == 0) {
					isBeginningWater = true;
				} else if ((!wt.getChildren().isEmpty()) && isBeginningWater) {
					isBeginningWater = false;
				}
			} else if (isInWater) {
				isInWater = false;
			}
		}
		return totalCosts;
	}

	private int calculatePointsOfTurn(Pawn p, int distance) {
		for (int i = p.getNewLocation() + distance - 1; i >= 0; i--) {
			WaterTile wt = path.get(i);
			if (!(wt.getChildren().isEmpty())
					&& !(((LandTile) wt.getChildren().get(wt.getChildren().size() - 1))).hasPawn()) {
				return ((LandTile) (wt.getChildren().get(wt.getChildren().size() - 1))).getLandValue();
			}
		}
		return 0;
	}

	private int calculateDistanceOfMove(Pawn p, int distance, Card c) {
		int startingLocation = p.getNewLocation() + distance;
		for (int i = startingLocation + 1; i < path.size(); i++) {
			WaterTile wt = path.get(i);
			if (!(wt.getChildren().isEmpty())
					&& ((LandTile) wt.getChildren().get(wt.getChildren().size() - 1)).getColor().equals(c.getColor())) {
				return i - startingLocation;
			}

		}
		return path.size() - startingLocation;
	}
}
