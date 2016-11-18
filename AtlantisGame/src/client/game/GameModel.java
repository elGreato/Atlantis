package client.game;

import java.util.ArrayList;

import client.lobby.ClientLobbyInterface;
import client.lobby.LobbyModel;
import gameObjects.Card;
import gameObjects.Pawn;
import gameObjects.LandTile;
import gameObjects.Player;
import gameObjects.WaterTile;
import messageObjects.AtlantisMainLandMessage;
import messageObjects.InGameMessage;
import messageObjects.OpponentMessage;
import messageObjects.PlayerMessage;
import messageObjects.WaterMessage;
import messageObjects.turnMessages.GameStatusMessage;
import messageObjects.turnMessages.PawnCardSelectedMessage;
import messageObjects.turnMessages.PaymentDoneMessage;
import messageObjects.turnMessages.PlayAnotherCardMessage;
import messageObjects.turnMessages.RefreshPlayerMessage;
import messageObjects.turnMessages.RevertTurnMessage;
import messageObjects.turnMessages.ServerMessage;
import messageObjects.turnMessages.TurnMessage;
import messageObjects.turnMessages.WaterPaidMessage;
import messageObjects.turnMessages.BuyCardsMessage;
import messageObjects.turnMessages.CardsBoughtMessage;
import messageObjects.turnMessages.EndMYTurnMessage;

public class GameModel {

	private String gameName;
	private GameView view;
	// send messages to server through method msgOut.sendMessage()
	private ClientLobbyInterface msgOut;
	private int waterBill = 0;
	protected Player currentPlayer;

	public String getGameName() {
		return gameName;
	}

	public GameModel(String gameName, LobbyModel lobbyModel, GameView gameView) {
		this.gameName = gameName;
		msgOut = lobbyModel;
		this.view = gameView;

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
				for (Pawn p : currentPlayer.getPawns()) {
					p.setOnMouseClicked(e -> view.handlePawn(p));
				}
			}
			if (message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {

				payForPassingWater(message.getWaterBill(), message.getWaterPassedCount());
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
			Pawn selectedPawn = null;
			assignThenMovePawn(message.getCurrentPlayer().getPlayerIndex(),message.getSelectedPawn(),message.getSelectedLand());
			

		}
		// inform player to play another card
		if (msgIn instanceof PlayAnotherCardMessage) {
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
					for (int k = 0; k < currentPlayer.getPlayerHand().getTreasures().size(); k++) {
						if (currentPlayer.getPlayerHand().getTreasures().get(k).getTileId() == soldLand.getTileId()) {
							currentPlayer.getPlayerHand().getTreasures().remove(k);

						}
					}
				}
				ArrayList<Card> boughtCards = message.getPurchase();
				if (boughtCards.size() != 0
						&& message.getCurrentPlayer().getPlayerIndex() == currentPlayer.getPlayerIndex()) {
					addCardToPlayer(boughtCards);

				}

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
				for (int i = 0; i < message.getTreasuresChosen().size(); i++) {
					LandTile paidTreasure = message.getTreasuresChosen().get(i);
					view.removeEnemyTreasures(message.getPlayerIndex(), paidTreasure);

				}
			}
		}
		if (msgIn instanceof EndMYTurnMessage) {
			EndMYTurnMessage message = (EndMYTurnMessage) msgIn;
			if (message.getPlayerIndex() == currentPlayer.getPlayerIndex()) {
				if (message.getNewCards().size() != 0) {
					addCardToPlayer(message.getNewCards());
				}
			}
		}
		if(msgIn instanceof RevertTurnMessage){
			System.out.println("Revert received in client");
			RevertTurnMessage message = (RevertTurnMessage)msgIn;
			if(message.getRemovedTreasure()!=null){
				view.base.get(message.getRemovedIndex()).getChildren().add(message.getRemovedTreasure());
			}
			assignThenMovePawn(message.getPlayerIndex(), message.getSelectedPawn(), message.getSelectedLand());
			
		}

	}

	private void assignThenMovePawn(int playerIndex, Pawn selectedPawn, LandTile selectedLand) {
		if (playerIndex == currentPlayer.getPlayerIndex()) {
			for (Pawn pp : currentPlayer.getPawns()) {
				if (selectedPawn.getPawnId() == pp.getPawnId())
					selectedPawn = pp;
			}
		} else {
			for (Player enemy : currentPlayer.getOpponents()) {
				if (enemy.getPlayerIndex() ==playerIndex) {
					for (Pawn pp : enemy.getPawns()) {
						if (pp.getPawnId() == selectedPawn.getPawnId()) {
							selectedPawn = pp;
						}
					}
				}
			}
		}

		movePawn(currentPlayer.getPlayerIndex(), selectedPawn, selectedLand);
		
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

	private void addCardToPlayer(ArrayList<Card> newCards) {
		for (Card newCard : newCards) {
			currentPlayer.addCard(newCard);
			newCard.setOwner(currentPlayer);
			addClickToCard(newCard);
		}
	}

	private void movePawn(int indexOfPlayer, Pawn selectedPawn, LandTile selectedLand) {
		Pawn viewPawn = null;

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
		ArrayList<LandTile> treasuresChosen = new ArrayList<>();
		for (int i = 0; i < currentPlayer.getPlayerHand().getTreasures().size(); i++) {
			LandTile treasureSelected = currentPlayer.getPlayerHand().getTreasures().get(i);
			if (treasureSelected.isSelected()) {
				treasuresChosen.add(treasureSelected);
				currentPlayer.getPlayerHand().getTreasures().remove(treasureSelected);
				view.removePlayerTreasure(treasureSelected);
			}

		}

		msgOut.sendMessage(new BuyCardsMessage(gameName, currentPlayer.getPlayerIndex(), treasuresChosen));

		view.closeBuyScene();

	}

	private void payForPassingWater(int waterBill, int waterPassedCount) {
		if (waterPassedCount > 0) {
			
			this.waterBill = waterBill;
			view.showWaterBill(waterBill, waterPassedCount);
		}

	}

	public void pay4Water() {
		ArrayList<LandTile> treasuresChosen = new ArrayList<>();
		ArrayList<Card> cardsChosen = new ArrayList<>();
		int totalChosen = 0;
		for (int i = 0; i < currentPlayer.getPlayerHand().getTreasures().size(); i++) {
			LandTile treasureSelected = currentPlayer.getPlayerHand().getTreasures().get(i);
			if (treasureSelected.isSelected()) {
				treasuresChosen.add(treasureSelected);
				totalChosen += treasureSelected.getLandValue();
				currentPlayer.getPlayerHand().getTreasures().remove(treasureSelected);
				view.removePlayerTreasure(treasureSelected);
			}

		}
		for (int i = 0; i < currentPlayer.getPlayerHand().getNumCards(); i++) {
			Card cardSelected = currentPlayer.getPlayerHand().getCards().get(i);
			if (cardSelected.isCardSelected()) {
				cardsChosen.add(cardSelected);
				totalChosen += 1;
				currentPlayer.getPlayerHand().getCards().remove(cardSelected);
				view.removeCardFromHand(cardSelected);
			}
		}
		msgOut.sendMessage(
				new WaterPaidMessage(gameName, currentPlayer.getPlayerIndex(), treasuresChosen, cardsChosen));
		view.closePayWaterScene();

	}

	public void handleCalc() {
		// try RemoveIf method in Server tomorrow
		ArrayList<LandTile> treasuresChosen = new ArrayList<>();
		ArrayList<Card> cardsChosen = new ArrayList<>();
		int totalChosen = 0;
		for (int i = 0; i < currentPlayer.getPlayerHand().getTreasures().size(); i++) {
			LandTile treasureSelected = currentPlayer.getPlayerHand().getTreasures().get(i);
			if (treasureSelected.isSelected()) {
				treasuresChosen.add(treasureSelected);
				totalChosen += treasureSelected.getLandValue();

			}

		}
		for (int i = 0; i < currentPlayer.getPlayerHand().getNumCards(); i++) {
			Card cardSelected = currentPlayer.getPlayerHand().getCards().get(i);
			if (cardSelected.isCardSelected()) {
				cardsChosen.add(cardSelected);
				totalChosen += 1;

			}
		}

		if (totalChosen >= waterBill) {
			view.btnPay4Water.setDisable(false);
			view.lblWaterCalc.setText("");
			view.lblWaterCalc
					.setText("You will pay " + String.valueOf(totalChosen) + "\n Remember, no change is offered");
		} else {
			view.btnPay4Water.setDisable(true);
			view.lblWaterCalc.setText("");
			view.lblWaterCalc.setText(
					"This is not enough!" + "You still need to pay extra" + String.valueOf(waterBill - totalChosen));
		}

	}

	public void handleEndMyTurn() {
		if (currentPlayer.isYourTurn()) {
			msgOut.sendMessage(new EndMYTurnMessage(gameName, currentPlayer.getPlayerIndex()));

		}
		else view.showNotYourTurnAlert();
	}

	public void handleRevert() {
		System.out.println("Rever from client");
		msgOut.sendMessage(new RevertTurnMessage(gameName, currentPlayer.getPlayerIndex()));
		
	}

}