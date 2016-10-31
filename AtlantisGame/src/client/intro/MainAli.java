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
	//for (int i=0; i<3;i++)
	/*gp.add(pa1,0, 0);
	pa1.setAlignment(Pos.BOTTOM_CENTER);
	
	gp.add(pa2, 0, 0);
	gp.add(pa3, 0, 0);
	pa3.setAlignment(Pos.TOP_CENTER);
	//System.out.println(pa1.getOwner().getPlayerName());
		
		BorderPane.setAlignment(gp, Pos.CENTER);
		 root.setCenter(gp);
	//	root.setTop(p1);*/
		p2.setVictoryPoints(14);
		p2.getVpHolder().setText(String.valueOf(p2.countVictoryPoints()));
		root.setBottom(p2);
		root.setCenter(main);
		primaryStage.setScene(new Scene(root));
		primaryStage.setFullScreen(false);
		primaryStage.show();
		
		
		DeckOfLandTiles dland= new DeckOfLandTiles();

		int kk=1;
		for (int i=0; i< dland.getNumberOfLandTiles();i++){
			
			System.out.println("Treasure Nymber "+kk+" and id "+dland.getTileDetails(i).getTileId()
					+" "+dland.getTileDetails(i).getColor().toString()+" Value "+ dland.getTileDetails(i).getLandValue());
			kk++;
		}
	}
}