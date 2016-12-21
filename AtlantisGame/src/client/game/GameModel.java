package client.game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import client.lobby.ClientLobbyInterface;
import client.lobby.LobbyModel;
import client.splashscreen.SplashScreen;
import gameObjects.Card;
import gameObjects.Pawn;
import gameObjects.LandTile;
import gameObjects.Player;
import gameObjects.WaterTile;
import javafx.animation.PauseTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.shape.Rectangle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import messageObjects.AtlantisMainLandMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerLeftMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.LastBillMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PaymentDoneMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.ResultMessage;
import messageObjects.turnMessages.RevertTurnMessage;
import messageObjects.turnMessages.ServerMessage;
import messageObjects.turnMessages.TurnMessage;
import messageObjects.turnMessages.WaterPaidMessage;
import messageObjects.turnMessages.BuyCardsMessage;
import messageObjects.turnMessages.CardsBoughtMessage;
import messageObjects.turnMessages.CloseGameMessage;
import messageObjects.turnMessages.EndMYTurnMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private ClientLobbyInterface msgOut;
	private int waterBill = 0;
	protected Player currentPlayer;
	private boolean nextPlayer = true;
	private boolean gameOver = false;
	private boolean landedOnPawn = false;

	public String getGameName() {
		return gameName;
	}

	public GameModel(String gameName, LobbyModel lobbyModel, GameView gameView) {
		this.gameName = gameName;
		msgOut = lobbyModel;
		this.view = gameView;
		// css
		view.scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		SplashScreen intro = new SplashScreen(view.stage);

	}

	// Here messages from server arrive
	public void processMessage(InGameMessage msgIn) {
		// first we receive the main board stuff
		if (msgIn instanceof WaterMessage) {
			view.addRecAndText(((WaterMessage) msgIn).getBase());
		}

		// the atlantis and the main land
		if (msgIn instanceof AtlantisMainLandMessage) {
			view.placeAtlantisMainLand(((AtlantisMainLandMessage) msgIn).getAtlantis(),
					((AtlantisMainLandMessage) msgIn).getMainland());
		}

		// now the players
		if (msgIn instanceof PlayerMessage) {
			currentPlayer = (((PlayerMessage) msgIn).getPlayer());

			for (Card c : currentPlayer.getPlayerHand().getCards()) {
				addClickToCard(c);
			}
			for (Pawn p : currentPlayer.getPawns()) {
				p.setOnMouseClicked(e -> view.handlePawn(p));
			}

			view.showPlayer(currentPlayer);

		}

		// here we assign each player his enemies
		if (msgIn instanceof OpponentMessage) {
			OpponentMessage message = ((OpponentMessage) msgIn);
			for (int i = 0; i < message.getOpponents().size(); i++) {
				if (currentPlayer.getPlayerIndex() != message.getOpponents().get(i).getPlayerIndex()) {
					view.setOpponent(message.getOpponents().get(i));
					currentPlayer.getOpponents().add((message).getOpponents().get(i));
				}
			}
		}
		// message about state of the game
		if (msgIn instanceof GameStatusMessage) {
			if (((GameStatusMessage) msgIn).isStarted())
				startTurn(((GameStatusMessage) msgIn).getCurrentPlayer());
		}

		if (msgIn instanceof ServerMessage) {
			view.showMessageFromServer(((ServerMessage) msgIn).getTheMessage());
		}

		// a turn message
		if (msgIn instanceof TurnMessage) {
			if (((TurnMessage) msgIn).isYourTurn()) {
				if (scanPawns() != null) {
					if (scanCards() != null) {
						currentPlayer.setYourTurn(true);
						Card selectedCard = null;
						Pawn selectedPawn = null;

						selectedPawn = scanPawns();
						selectedCard = scanCards();

						currentPlayer.getPlayerHand().removeCardFromHand(selectedCard);
						view.removeCardFromHand(selectedCard);
						msgOut.sendMessage(new PawnCardSelectedMessage(gameName, currentPlayer.getPlayerIndex(),
								selectedPawn, selectedCard));
						landedOnPawn = false;
					} else
						view.selectCardPlease();
				} else
					view.selectPawnPlease();

			} else
				view.showNotYourTurnAlert();

		}
		// update players
		if (msgIn instanceof RefreshPlayerMessage) {

			RefreshPlayerMessage message = (RefreshPlayerMessage) msgIn;
			// release the locked pawns
			if (message.getCurrentPlayer().getPlayerIndex() != currentPlayer.getPlayerIndex()) {
				view.setVpForEnemy(message.getCurrentPlayer().getPlayerIndex(), message.getVictoryPoints());
				for (Pawn p : currentPlayer.getPawns()) {
					if (!p.ReachedMainLand())
						p.setOnMouseClicked(e -> view.handlePawn(p));
				}
				// set the cards count for each enemy
				view.setCardCountForEnemy(message.getCurrentPlayer().getPlayerIndex(), message.getCardsCount());
			}
			if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				nextPlayer = message.isNextPlayer();
				view.vpHolder.setText("Your Victory Points: " + String.valueOf(message.getVictoryPoints()));
				payForPassingWater(message.getWaterBill(), message.getWaterPassedCount(), gameOver);
			}
			LandTile treasure = message.getTreasure();
			ArrayList<Card> newCards = message.getNewCards();
			if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex() && newCards != null) {
				addCardToPlayer(newCards);

			}
			if (treasure != null) {
				if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
					givePlayerTreasure(treasure);

				} else {
					giveEnemyTreasure(message.getCurrentPlayer().getPlayerIndex(), treasure);
				}
				removeTreasureFromBoard(treasure);
			}

			Pawn selectedPawn = message.getSelectedPawn();
			assignThenMovePawn(message.getCurrentPlayer().getPlayerIndex(), selectedPawn, message.getSelectedLand(),
					false);

		}
		// inform player to play another card
		if (msgIn instanceof PlayAnotherCardMessage) {
			nextPlayer = false;
			landedOnPawn = true;
			for (Pawn p : currentPlayer.getPawns()) {
				if (((PlayAnotherCardMessage) msgIn).getSelectedPawn().getPawnId() != p.getPawnId()) {
					p.setOnMouseClicked(null);
				}
			}
			if (((PlayAnotherCardMessage) msgIn).getExtraCards().size() != 0) {
				addCardToPlayer((((PlayAnotherCardMessage) msgIn).getExtraCards()));
			}
			view.playerAnother();

		}
		if (msgIn instanceof CardsBoughtMessage) {
			CardsBoughtMessage message = (CardsBoughtMessage) msgIn;
			ArrayList<LandTile> sold = message.getSold();
			if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				for (int i = 0; i < sold.size(); i++) {
					LandTile soldLand = sold.get(i);
					Iterator<LandTile> it = currentPlayer.getPlayerHand().getTreasures().iterator();
					while (it.hasNext()) {
						LandTile lt = it.next();
						if (lt.getTileId() == soldLand.getTileId()) {
							it.remove();

						}
					}
				}
				ArrayList<Card> boughtCards = message.getPurchase();
				if (boughtCards.size() != 0
						&& message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
					addCardToPlayer(boughtCards);

				}
				currentPlayer.setVictoryPoints(message.getVp());
				view.vpHolder.setText("Your Victory Points: " + String.valueOf(message.getVp()));

			} else {
				for (int i = 0; i < sold.size(); i++) {
					LandTile soldLand = sold.get(i);
					view.removeEnemyTreasures(message.getCurrentPlayer().getPlayerIndex(), soldLand);

				}

			}
		}
		if (msgIn instanceof PaymentDoneMessage) {
			PaymentDoneMessage message = (PaymentDoneMessage) msgIn;
			if (message.getPlayerIndex() != currentPlayer.getPlayerIndex()) {
				Iterator<LandTile> it = message.getTreasuresChosen().iterator();
				while (it.hasNext()) {
					LandTile paidTreasure = it.next();
					view.removeEnemyTreasures(message.getPlayerIndex(), paidTreasure);

				}
				view.setVpForEnemy(message.getPlayerIndex(), message.getVp());
				view.setCardCountForEnemy(message.getPlayerIndex(), message.getCardCount());
			}
			else view.setVpForPlayer(message.getVp());
		}
		if (msgIn instanceof EndMYTurnMessage) {
			EndMYTurnMessage message = (EndMYTurnMessage) msgIn;
			if (message.getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				if (message.getNewCards().size() != 0) {
					addCardToPlayer(message.getNewCards());
				}
			}
		}
		if (msgIn instanceof RevertTurnMessage) {
			RevertTurnMessage message = (RevertTurnMessage) msgIn;
			System.out.println(
					"Revert message arrived " + currentPlayer.getPlayerHand().getTreasures().size() + " treasures");
			if (message.getRemovedTreasure() != null) {
				view.base.get(message.getRemovedIndex()).getChildren().add(message.getRemovedTreasure());
				if (message.getPlayerIndex() == currentPlayer.getPlayerIndex()) {
					Rectangle rec = (Rectangle) message.getRemovedTreasure().getChildren().get(1);
					rec.setStroke(Color.TRANSPARENT);
					message.getRemovedTreasure().setSelected(false);
				}

			}
			if (message.getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				System.out.println("Removed cards: " + message.getRemovedCards().size());
				addCardToPlayer(message.getRemovedCards());

			}
			message.getSelectedPawn().setStartingLocation(message.getStartingLocation());
			assignThenMovePawn(message.getPlayerIndex(), message.getSelectedPawn(), message.getSelectedLand(), true);
			view.closePayWaterScene();

		}
		if (msgIn instanceof LastBillMessage) {
			System.out.println("last bill received");
			// true is for the game finished
			LastBillMessage message = (LastBillMessage) msgIn;
			System.out.println(
					"Bill amount: " + message.getWaterBill() + " you passed: " + message.getWaterPassedCount());
			gameOver = true;
			payForPassingWater(message.getWaterBill(), message.getWaterPassedCount(), gameOver);
			for (Pawn p : currentPlayer.getPawns()) {
				if (!p.ReachedMainLand())
					view.addPawnToMainLand(p);
			}
			for (Player enemy : currentPlayer.getOpponents()) {
				for (Pawn pp : enemy.getPawns()) {
					if (!pp.ReachedMainLand()) {
						view.addPawnToMainLand(pp);
					}
				}
			}
			for (Pawn p : currentPlayer.getPawns()) {
				if (!p.ReachedMainLand())
					view.addPawnToMainLand(p);
			}

		}
		if (msgIn instanceof ResultMessage) {
			ResultMessage message = (ResultMessage) msgIn;
			if (message.getWinner() == currentPlayer.getPlayerIndex()) {
				view.IWin(currentPlayer.getPlayerName());
			} else
				view.ILose(message.getWinnerName());
			finishGame();
		}
		if (msgIn instanceof PlayerLeftMessage) {
			PlayerLeftMessage plm = (PlayerLeftMessage) msgIn;
			view.replaceLeavingPlayer(plm.getPlayerIndex(), plm.getNewPlayerName());
			for (Player p : currentPlayer.getOpponents()) {
				if (p.getPlayerIndex() == plm.getPlayerIndex())
				{
					p.setPlayerName(plm.getNewPlayerName());
				}
			}
		}

	}

	private void assignThenMovePawn(int playerIndex, Pawn selectedPawn, LandTile selectedLand, boolean isRevert) {
		Pawn pawnToPlay = null;
		if (playerIndex == currentPlayer.getPlayerIndex()) {
			for (Pawn pp : currentPlayer.getPawns()) {
				if (selectedPawn.getPawnId() == pp.getPawnId())
					pawnToPlay = pp;

			}
		} else {
			for (Player enemy : currentPlayer.getOpponents()) {
				if (enemy.getPlayerIndex() == playerIndex) {
					for (Pawn pp : enemy.getPawns()) {
						if (pp.getPawnId() == selectedPawn.getPawnId()) {
							pawnToPlay = pp;

						}
					}
				}
			}
		}
		if (isRevert) {
			revertPawn(currentPlayer.getPlayerIndex(), pawnToPlay, selectedPawn.getStartingLocation());
		} else {
			movePawn(currentPlayer.getPlayerIndex(), pawnToPlay, selectedLand);
		}

	}

	// ADDED BY KEVIN
	private void revertPawn(int playerIndex, Pawn selectedPawn, int startingLocation) {
		System.out.println("revert pawn");
		boolean isThisPlayer = false;
		if (currentPlayer.getPawns().contains(selectedPawn)) {
			isThisPlayer = true;
		}
		if (selectedPawn.ReachedMainLand()) {
			System.out.println("Pawn reached mainland but needs to revert");
			selectedPawn.setReachedMainLand(false);
			view.removePawnFromMainLand(selectedPawn, isThisPlayer);

		}
		if (startingLocation == -1) {
			System.out.println("Revert to atlantis");
			view.addPawnToAtlantis(selectedPawn);
		} else {
			WaterTile tempWater = view.getBase().get(startingLocation);
			((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).setPawnOnTile(selectedPawn);
			((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).convertPawns();
		}

	}

	private Pawn scanPawns() {
		Pawn foundPawn = null;
		for (Pawn pawn : currentPlayer.getPawns()) {
			if (pawn.isPawnSelected()) {
				foundPawn = pawn;
				break;
			}
		}
		return foundPawn;
	}

	private void addClickToCard(Card c) {
		c.setOnMouseClicked(e -> view.handleCard(c));
		view.createCardView(c);

	}

	protected void addCardToPlayer(ArrayList<Card> newCards) {
		for (Card newCard : newCards) {
			currentPlayer.addCard(newCard);
			newCard.setOwner(currentPlayer);
			addClickToCard(newCard);
		}
	}

	private void movePawn(int indexOfPlayer, Pawn selectedPawn, LandTile selectedLand) {
		Pawn viewPawn = null;
		// ????
		if (currentPlayer.getPawns().contains(selectedPawn)) {
			viewPawn = selectedPawn;
		} else {
			for (int i = 0; i < currentPlayer.getOpponents().size(); i++) {
				if (currentPlayer.getOpponents().get(i).getPawns().contains(selectedPawn)) {
					viewPawn = selectedPawn;
				}
			}
		}
		for (int g = 0; g < view.getBase().size() && selectedLand != null; g++) {
			WaterTile tempWater = view.getBase().get(g);
			if (tempWater.getChildren().contains(selectedLand)) {
				((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).setPawnOnTile(viewPawn);
				((LandTile) tempWater.getChildren().get(tempWater.getChildren().size() - 1)).convertPawns();
			}

		}
		if (selectedLand == null) {
			selectedPawn.setPawnSelected(false);
			selectedPawn.setOnMouseClicked(null);
			selectedPawn.setReachedMainLand(true);
			view.addPawnToMainLand(selectedPawn);

		}

	}

	private void givePlayerTreasure(LandTile treasure) {
		if (treasure != null) {
			currentPlayer.getPlayerHand().addTreasure(treasure);
			view.givePlayerTreasure(treasure);

		}
	}

	private void giveEnemyTreasure(int indexOfPlayer, LandTile treasure) {
		if (treasure != null) {
			view.giveEnemyTreasure(indexOfPlayer, treasure);
		}

	}

	private void removeTreasureFromBoard(LandTile treasure) {
		for (int g = 0; g < view.getBase().size(); g++) {
			WaterTile tempWater = view.getBase().get(g);
			if (tempWater.getChildren().contains(treasure)) {
				tempWater.getChildren().remove(treasure);
			}

		}
	}

	public void startTurn(Player player) {

		view.gameStarted();
		if (currentPlayer.getPlayerIndex() == player.getPlayerIndex()) {
			view.yourTurn();
			currentPlayer.setYourTurn(true);

		} else {
			view.notYourTurn(player.getPlayerName());
			currentPlayer.setYourTurn(false);
		}
	}

	public void tryPlayCard() {

		msgOut.sendMessage(new GameStatusMessage(gameName, currentPlayer.getPlayerIndex()));

	}

	private Card scanCards() {
		Card cardSelected = null;
		for (Card card : currentPlayer.getPlayerHand().getCards()) {
			if (card.isCardSelected()) {
				cardSelected = card;
				break;
			}
		}

		return cardSelected;
	}

	public void tryBuyCards() {
		if (currentPlayer.isYourTurn()) {

			view.showBuyCards();
		}

	}

	public void pay4Cards() {
		ArrayList<LandTile> treasuresChosen = removeSelectedLandTiles();

		msgOut.sendMessage(new BuyCardsMessage(gameName, currentPlayer.getPlayerIndex(), treasuresChosen));

		view.closeBuyScene();

	}

	private ArrayList<LandTile> removeSelectedLandTiles() {
		ArrayList<LandTile> result = new ArrayList<>();
		Iterator<LandTile> it = currentPlayer.getPlayerHand().getTreasures().iterator();
		while (it.hasNext()) {
			LandTile treasureSelected = it.next();
			if (treasureSelected.isSelected()) {

				result.add(treasureSelected);
				it.remove();
				view.removePlayerTreasure(treasureSelected);
			}
		}
		return result;
	}

	private void payForPassingWater(int waterBill, int waterPassedCount, boolean gameFinished) {
		if (waterBill != 0) {

			this.waterBill = waterBill;
			view.showWaterBill(waterBill, waterPassedCount, gameFinished);
			System.out.println("reached show water bill , is it ended ?" + gameFinished);
		} else if (waterPassedCount < 1 && nextPlayer && !gameFinished)
			msgOut.sendMessage(new EndMYTurnMessage(gameName, currentPlayer.getPlayerIndex(), true));

	}

	public void pay4Water() {
		ArrayList<LandTile> treasuresChosen = removeSelectedLandTiles();
		ArrayList<Card> cardsChosen = new ArrayList<>();
		System.out.println("Cards chosen size "+cardsChosen.size());
		Iterator<Card> it = currentPlayer.getPlayerHand().getCards().iterator();
		while (it.hasNext()) {
			Card cardSelected = it.next();
			if (cardSelected.isCardSelected()) {
				cardSelected.setCardSelected(false);
				cardsChosen.add(cardSelected);
				view.removeCardFromHand(cardSelected);
				System.out.println("Cards chosen size "+cardsChosen.size());
	
			}
			
		}
		System.out.println("Cards chosen size333 "+cardsChosen.size());
		msgOut.sendMessage(new WaterPaidMessage(gameName, currentPlayer.getPlayerIndex(), treasuresChosen, cardsChosen,
				nextPlayer));
		view.closePayWaterScene();

	}

	public void handleCalc() {
		System.out.println("HandleCalc");
		ArrayList<LandTile> treasuresChosen = new ArrayList<>();
		ArrayList<Card> cardsChosen = new ArrayList<>();
		boolean allCardsSelected = true;
		boolean allTreasuresSelected = true;
		int totalChosen = 0;
		for (int i = 0; i < currentPlayer.getPlayerHand().getTreasures().size(); i++) {
			LandTile treasureSelected = currentPlayer.getPlayerHand().getTreasures().get(i);
			if (treasureSelected.isSelected()) {
				treasuresChosen.add(treasureSelected);
				totalChosen += treasureSelected.getLandValue();

			} else {
				System.out.println("not all treasures selected");
				allTreasuresSelected = false;
			}

		}
		for (int i = 0; i < currentPlayer.getPlayerHand().getNumCards(); i++) {
			Card cardSelected = currentPlayer.getPlayerHand().getCards().get(i);
			if (cardSelected.isCardSelected()) {
				cardsChosen.add(cardSelected);
				totalChosen += 1;

			} else {
				System.out.println("not all cards selected");
				allCardsSelected = false;
			}
		}
		// ability to revert only when you can't afford the water that you
		// jumped
		if (allCardsSelected && allTreasuresSelected && totalChosen <= waterBill && !gameOver) {
			System.out.println("activating revert button");
			view.btnRevert.setDisable(false);
		}
		if (allCardsSelected && allTreasuresSelected && totalChosen <= waterBill && gameOver) {
			view.btnNotEnough.setDisable(false);
			// if the pawn landed on another pawn he should not be allowed to
			// pay all cards, to prevent cheating

		}
		if (totalChosen >= waterBill) {
			view.lblWaterCalc.setText("");
			view.btnPay4Water.setDisable(false);
			view.lblWaterCalc
					.setText("You will pay " + String.valueOf(totalChosen) + "\n Remember, no change is offered");
			view.tempStage.sizeToScene();
		} else {
			view.btnPay4Water.setDisable(true);
			view.lblWaterCalc.setText("");
			view.lblWaterCalc.setText(
					"This is not enough!" + "You still need to pay extra" + String.valueOf(waterBill - totalChosen));
			view.tempStage.sizeToScene();
		}
		if (landedOnPawn == true && allCardsSelected) {
			view.btnPay4Water.setDisable(true);
			view.showDontUserAllCardsAlert();

		}

	}

	public void handleEndMyTurn() {
		// this is not considered a normal end cuz he pressed the button
		if (currentPlayer.isYourTurn()) {
			msgOut.sendMessage(new EndMYTurnMessage(gameName, currentPlayer.getPlayerIndex(), false));

		} else
			view.showNotYourTurnAlert();
	}

	public void handleRevert() {
		view.btnRevert.setDisable(true);
		// currentPlayer.getPlayerHand().getTreasures().clear();

		msgOut.sendMessage(new RevertTurnMessage(gameName, currentPlayer.getPlayerIndex()));

	}

	public void finishGame() {
		PauseTransition delay = new PauseTransition(Duration.seconds(4));
		delay.setOnFinished(event -> view.stage.close());
		delay.play();
		msgOut.sendMessage(new CloseGameMessage(gameName));
		msgOut.endGame(this);

	}

	public void handleNotEnough() {
		pay4Water();

	}

	public void playerLeft(WindowEvent e) {
		if (!gameOver) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Close Atlantis");
			alert.setContentText("Are you sure you want to close the game? \nYou will lose this game");
			Optional<ButtonType> answer = alert.showAndWait();
			if (answer.get() == ButtonType.OK) {
				msgOut.sendMessage(new PlayerLeftMessage(gameName, currentPlayer.getPlayerIndex()));
				msgOut.endGame(this);
			} else {
				e.consume();
			}
		}

	}

}