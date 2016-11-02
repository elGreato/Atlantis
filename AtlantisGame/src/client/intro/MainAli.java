package client.intro;

import java.util.Set;
import java.util.Hashtable;
import java.util.Iterator;

import gameObjects.Card;
import gameObjects.ColorChoice;
import gameObjects.DeckOfCards;
import gameObjects.DeckOfLandTiles;
import gameObjects.LandTile;
import gameObjects.MainBoard;
import gameObjects.Pawn;
import gameObjects.Player;
import gameObjects.WaterTile;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainAli extends Application {
	public static void main(String[] args) {
		launch();

	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Atlantis");


		Player p2 = new Player("Kevin");
		p2.setColor(ColorChoice.green);
		Pawn pa1 = new Pawn(p2);
		Pawn pa2 = new Pawn(p2);
		Pawn pa3 = new Pawn(p2);
		
		
		DeckOfCards deck = new DeckOfCards();
	
		for (int i = 0; i < 5; i++) {
			Card card = deck.deal();// this returns the card object
		//	p1.addCard(card);

		//	card = deck.deal();
			p2.addCard(card); 
		}
	//	p2.setVictoryPoints(p2.countVictoryPoints());
		BorderPane root = new BorderPane();	
	MainBoard main = new MainBoard();
	root.setLeft(new Label(String.valueOf(p2.countVictoryPoints())));

		p2.setVictoryPoints(14);
		p2.getVpHolder().setText(String.valueOf(p2.countVictoryPoints()));
		root.setBottom(p2);
		root.setCenter(main);
		primaryStage.setScene(new Scene(root));
		primaryStage.setFullScreen(false);
		primaryStage.show();
		
		
		DeckOfLandTiles dland= new DeckOfLandTiles();
		
	
	}
	
}