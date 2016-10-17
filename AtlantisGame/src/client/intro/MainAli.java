package client.intro;

import java.util.Set;
import java.util.Hashtable;
import java.util.Iterator;

import gameObjects.Card;
import gameObjects.DeckOfCards;
import gameObjects.DeckOfLandTiles;
import gameObjects.LandTile;
import gameObjects.MainBoard;
import gameObjects.Player;
import gameObjects.WaterTile;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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

		Player p1 = new Player("Ali");
		Player p2 = new Player("Kevin");
		DeckOfCards deck = new DeckOfCards();
		deck.shuffle();
		for (int i = 0; i < 5; i++) {
			Card card = deck.deal();// this returns the card object
			p1.addCard(card);

			card = deck.deal();
			p2.addCard(card);
		}
		
	MainBoard gp = new MainBoard(70);

		BorderPane root = new BorderPane();
		BorderPane.setAlignment(gp, Pos.CENTER);
		 root.setCenter(gp);
		root.setTop(p1);
		
		root.setBottom(p2);
		primaryStage.setScene(new Scene(root, 900, 950));
		primaryStage.show();
		System.out.println(p1.countVictoryPoints());
		DeckOfLandTiles dland= new DeckOfLandTiles();
		System.out.println(dland.getNumberOfLandTiles());
		int kk=1;
		for (int i=0; i< dland.getNumberOfLandTiles();i++){
			
			System.out.println("Treasure Nymber "+kk+" and id "+dland.getTileDetails(dland, i).getId()+" "+dland.getTileDetails(dland, i).getLandTileColor().toString()+" Value "+dland.getTileDetails(dland, i).getLandValue());
			kk++;
		}
	}
}