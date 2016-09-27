package client.intro;

import gameObjects.Card;
import gameObjects.DeckOfCards;
import gameObjects.MainBoard;
import gameObjects.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
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
	        Player p2 = new Player ("Kevin");
	        DeckOfCards deck = new DeckOfCards();
	        deck.shuffle();
	        for (int i = 0; i < 5; i++) {
                Card card = deck.deal();//this returns the card object
                p1.addCard(card);
                
                card = deck.deal(); 
                p2.addCard(card);
                }
	   /*     MainBoard b = new MainBoard();
	        Pane p = new Pane();
	        p.getChildren().add(b.createBoard());*/
	       
	        
	        
	        BorderPane root = new BorderPane();
	     //   root.setCenter();
	        root.setTop(p1);
	        root.setBottom(p2);
	        primaryStage.setScene(new Scene(root, 800, 850));
	        primaryStage.show();
	    }
	}