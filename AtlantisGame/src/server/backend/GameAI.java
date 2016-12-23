package server.backend;

import java.util.ArrayList;
import java.util.HashMap;

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
import messageObjects.turnMessages.CloseGameMessage;
import messageObjects.turnMessages.EndMYTurnMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.LastBillMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.ResultMessage;
import messageObjects.turnMessages.ServerMessage;
import messageObjects.turnMessages.WaterPaidMessage;
/**
* <h1>Manages a specific game a specific AI players participates in.</h1>
* Buys cards, calculates best turns, does turns, pays for water etc.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class GameAI {
	private GameInterface game;
	private ArrayList<WaterTile> path;
	private ArrayList<Player> opponentInfo;
	public Player me;
	private double aiGreediness;
	private double aiSpeed;
	private double aiTeamSpirit;
	private double aiEvilness;
	private ArrayList<Card> bestCards;
	private Pawn bestPawn;
	private double avgDistanceOtherPlayers;
	private double avgDistanceMe;
	private AICostObject payments;
	private int moves;
	//Getter needed
	public String getGameName() {
		return game.getName();
	}
	//Constructor
	public GameAI(GameInterface game, double aiGreediness, double aiSpeed, double aiTeamSpirit, double aiEvilness) {
		this.game = game;
		this.aiGreediness = aiGreediness;
		this.aiTeamSpirit = aiTeamSpirit;
		this.aiSpeed = aiSpeed;
		this.aiEvilness = aiEvilness;
	}
	//Processes incoming messages
	public void processMessage(InGameMessage igm) {
		if (igm instanceof WaterMessage) {
			path = ((WaterMessage) igm).getBase();
		} else if (igm instanceof PlayerMessage) {
			me = ((PlayerMessage) igm).getPlayer();
		} else if (igm instanceof OpponentMessage) {
			opponentInfo = ((OpponentMessage) igm).getOpponents();
		} else if (igm instanceof GameStatusMessage) {
			GameStatusMessage gsm = (GameStatusMessage) igm;
			checkIfMyTurn(gsm);
		} else if (igm instanceof PlayAnotherCardMessage) {
			playAnotherCard();
		} else if (igm instanceof RefreshPlayerMessage) {
			RefreshPlayerMessage rpm = (RefreshPlayerMessage) igm;
			doPayment(rpm);
		} else if (igm instanceof LastBillMessage) {
			int totalPayment = ((LastBillMessage)igm).getWaterBill();
			payLastBill(totalPayment);
		} else if(igm instanceof CardsBoughtMessage)
		{
			CardsBoughtMessage cbm = (CardsBoughtMessage)igm;
			if(cbm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex())
			{
				doATurn(true);
				
			}
			
		} else if(igm instanceof ResultMessage)
		{
			game.processMessage(new CloseGameMessage(game.getName()));
		}
	}
	//Does a turn if its the turn of this AI
	private void checkIfMyTurn(GameStatusMessage gsm) {
		if (gsm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex()) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			doATurn(false);
		}
		
	}
	//If the AI needs to play another card this method is invoked. It takes card, pawn and costs from calcuation done in the beginning of the turn.
	private void playAnotherCard() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		game.processMessage(
				new PawnCardSelectedMessage(game.getName(), me.getPlayerIndex(), bestPawn, bestCards.remove(0)));
		
	}
	//Initiates payment of last bill, calls determineTilesToPay to calculate best payment if it doesn't have to pay with everything.
	private void payLastBill(int totalPayment) {
		int valueOfHand = 0;
		valueOfHand += me.getPlayerHand().getNumCards();
		for(LandTile lt : me.getPlayerHand().getTreasures())
		{
			valueOfHand += lt.getLandValue();
		}
		if(totalPayment > 0 && valueOfHand <= totalPayment)
		{
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
			
			WaterPaidMessage wpm = new WaterPaidMessage(game.getName(),me.getPlayerIndex(), payment.getTilesPaidInEachMove().get(0),payment.getCardsPaidInEachMove().get(0),true);
			game.processMessage(wpm);
		}
		else
		{
			int pawnsReachedMainland = 0;
			for(Pawn p : me.getPawns())
			{
				if(p.ReachedMainLand())
				{
					pawnsReachedMainland +=1;
				}
			}
			if(pawnsReachedMainland<3)
			{
				WaterPaidMessage wpm = new WaterPaidMessage(game.getName(),me.getPlayerIndex(), new ArrayList<LandTile>(),new ArrayList<Card>(),true);
				game.processMessage(wpm);
			}
		}
		
	}
	//Does payment if receives message asking for it. Takes data from calculated move at the beginning of turn.
	private void doPayment(RefreshPlayerMessage rpm) {
		if (rpm.getCurrentPlayer().getPlayerIndex() == me.getPlayerIndex() && rpm.getWaterBill() > 0) {

			ArrayList<LandTile> tilesForPayment = null;
			ArrayList<Card> cardsForPayment = null;
		
			tilesForPayment = payments.getTilesPaidInEachMove().remove(moves);
		
			cardsForPayment = payments.getCardsPaidInEachMove().remove(moves);
			
			if (tilesForPayment != null && !tilesForPayment
					.isEmpty() || (cardsForPayment != null && !cardsForPayment.isEmpty())) {
				
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
	//Invoked when the AI recevies message that its turn has started
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
	//Plans best move including cards to play, pawn to play (both using determineBestMoves method) and payments(using determineBestPayment method
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
			game.processMessage(
					
					new PawnCardSelectedMessage(game.getName(), me.getPlayerIndex(), bestPawn, bestCards.remove(0)));
					
		} else {
			giveUpTurn();
		}
		
	}
	//Determines whether it's necessary to buy cards in the beginning of the turn
	private boolean buyCardsIfRequired() {
		if(me.getPlayerHand().getNumCards() < 2)
		{
			ArrayList<LandTile> payment = new ArrayList<LandTile>();
			payment = determineTreasuresForBuyProcess(4-me.getPlayerHand().getNumCards(),me.getPlayerHand().getTreasures(), new ArrayList<LandTile>());
			
			if(payment != null &&payment.size()>0 &&determineValueOfLandTiles(payment)>1)
			{
				BuyCardsMessage bcm = new BuyCardsMessage(game.getName(),me.getPlayerIndex(),payment);
				game.processMessage(bcm);
				return true;
			}
		}
		return false;
		
		
	}
	//When buying cards this method determines the treasures to pay with
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
	
	//Returns total value of land tiles in an arraylist
	private int determineValueOfLandTiles(ArrayList<LandTile> bestPayment) {
		int valueOfPayment = 0;
		for(LandTile lt2:bestPayment)
		{
			valueOfPayment += lt2.getLandValue();
		}
		return valueOfPayment;
	}
	//When AI can't find a turn to do this method is invoked to send the message to end the turn.
	private void giveUpTurn() {
		game.processMessage(new EndMYTurnMessage(game.getName(), me.getPlayerIndex(),false));

	}
	//Calculates distance of AI and average distance of other players (all together)
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
	//Recursively plans best moves including cards to play pawn to play and costs(using determineBestPayment method
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
							(((LandTile) ((path.get(p.getNewLocation()+distanceAlreadyTraveled).getChildren().get(path.get(p.getNewLocation()+distanceAlreadyTraveled).getChildren().size()-1)))).hasPawn()))
					{
			
					
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
						int positionOfRemoval = calculatePositionOfTileRemoved(p,distanceAlreadyTraveled);
						int points = 0;
						int newWaterCosts = 0;
						if(positionOfRemoval >=0)
						{
							points = ((LandTile) (path.get(positionOfRemoval).getChildren().get(path.get(positionOfRemoval).getChildren().size() - 1))).getLandValue();
							//Tests if ai can open a new water to make opponents pay more
							if(morePawnsOfOpponentsBehind(positionOfRemoval, p) && positionOfRemoval != 0 && positionOfRemoval != 52 && 
									landBeforeAndAfter(positionOfRemoval) && path.get(positionOfRemoval).getChildren().size() == 1)
							{
								newWaterCosts = ((LandTile) (path.get(positionOfRemoval-1).getChildren().get(path.get(positionOfRemoval-1).getChildren().size() - 1))).getLandValue();
								if(((LandTile) (path.get(positionOfRemoval+1).getChildren().get(path.get(positionOfRemoval+1).getChildren().size() - 1))).getLandValue() < newWaterCosts)
								{
									newWaterCosts = ((LandTile) (path.get(positionOfRemoval+1).getChildren().get(path.get(positionOfRemoval+1).getChildren().size() - 1))).getLandValue();
								}
							}
						}
						
						//calculates value of this turn
						double valueOfTurn = ((aiGreediness * Math.pow(points-3, 3)) - costsAlreadyIncurred - 2*cardsAlreadyPlayed.size()) + (aiSpeed * ((avgDistanceOtherPlayers+2)/(avgDistanceMe+2))*distanceAlreadyTraveled)+(aiTeamSpirit*(avgDistanceMe- p.getNewLocation()))+ (aiEvilness * (newWaterCosts-1));
						if(bestPossibleTurn == null||valueOfTurn > bestPossibleTurn.getValueOfTurn())
						{
							ArrayList<Pawn> pawn = new ArrayList<Pawn>();
							pawn.add(p);
							bestPossibleTurn = new AITurnObject(pawn, cardsLeftOnHand,cardsAlreadyPlayed,tilesLeftInHand,costs,
									distanceAlreadyTraveled,costsAlreadyIncurred);
							bestPossibleTurn.setValueOfTurn(valueOfTurn);
						}	
					}
				}
			}
		}
		return bestPossibleTurn;
	}
	private boolean landBeforeAndAfter(int positionOfRemoval) {
		if(path.get(positionOfRemoval +1).getChildren().size() >= 1 && path.get(positionOfRemoval -1).getChildren().size() >=1)
		{
			return true;
		}
		return false;
	}
	private boolean morePawnsOfOpponentsBehind(int positionOfRemoval, Pawn p) {
		int numOfMyPawns = 0;
		int numOfOpponentsPawns = 0;
		double avgNumberOpponentsPawns = 0;
		for(Pawn pawn: me.getPawns())
		{
			if(pawn != p && pawn.getNewLocation() < positionOfRemoval)
			{
				numOfMyPawns +=1;
			}
		}
		for(Player opponent : opponentInfo)
		{
			for(Pawn pawn : opponent.getPawns())
			{
				if(pawn.getNewLocation() < positionOfRemoval)
				{
					numOfOpponentsPawns += 1;
				}
			}
		}
		avgNumberOpponentsPawns = numOfOpponentsPawns/opponentInfo.size();
		if(avgNumberOpponentsPawns > numOfMyPawns)
		{
			return true;
		}
		return false;
	}
	//Recursively determines best payment to do for a calculated move
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
				AICostObject finalPayment = determineTilesToPay(updatedCosts, cardsLeftInHand, updatedTilesLeftInHand,
						updatedPaymentSize, moveNumber, isLastPayment);
				if (finalPayment != null && (bestPayment == null
						|| finalPayment.getRealCosts(moveNumber,isLastPayment) < bestPayment.getRealCosts(moveNumber,isLastPayment))) {
					bestPayment = finalPayment;
				}
			} else if (updatedPaymentSize <= 0) {
				if (bestPayment == null
						|| updatedCosts.getRealCosts(moveNumber,isLastPayment) < bestPayment.getRealCosts(moveNumber,isLastPayment)) {
					bestPayment = updatedCosts;
				}
			}
		}
		return bestPayment;
	}
	
	//calculates the costs of a specific move
	private int calculateCostsOfMove(Pawn p, int distanceTraveledBefore, int distanceTraveledAfter) {
		if(p.getNewLocation() + distanceTraveledBefore + distanceTraveledAfter == 53)
		{
			int numReached = 0;
			for(Pawn pawn:me.getPawns())
			{
				if (pawn.ReachedMainLand())
				{
					numReached +=1;
				}
			}
			if(numReached == 2)
			{
				return 0;
			}
		}
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
	//calculates points for a specific move
	private int calculatePositionOfTileRemoved(Pawn p, int distance) {
		for (int i = p.getNewLocation() + distance - 1; i >= 0; i--) {
			WaterTile wt = path.get(i);
			if (!(wt.getChildren().isEmpty())
					&& !(((LandTile) wt.getChildren().get(wt.getChildren().size() - 1))).hasPawn()) {
				return i;
			}
		}
		return -1;
	}
	//calculates distance traveled in a specific move
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
