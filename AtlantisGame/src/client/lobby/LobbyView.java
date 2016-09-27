package client.lobby;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LobbyView {
	private Stage stage;
	private Scene scene;
	
	private BorderPane root;
	private VBox gamesSection;
	private VBox statsSection;
	
	private Label title;
	private TableView<String> gameList;
	private TableColumn gameNameCol;
	private TableColumn playersCol;
	private TableColumn playersRegCol;
	private TableColumn playersMaxCol;
	private TableView leaderBoard;
	
	
	public LobbyView()
	{
		root = new BorderPane();
		gamesSection = new VBox();
		statsSection = new VBox();
		
		gameList = new TableView<String>();
		gameNameCol = new TableColumn("Name");
		playersCol = new TableColumn("Players");
		playersRegCol = new TableColumn("Reg.");
		playersMaxCol = new TableColumn("Max.");
		playersCol.getColumns().addAll(playersRegCol, playersMaxCol);
		gameList.getColumns().addAll(gameNameCol, playersCol);
		gamesSection.getChildren().add(gameList);
		
		title = new Label("ATLANTIS LOBBY");
		
		root.setLeft(gamesSection);
		root.setTop(title);
		
		
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
	}

	public void start() {
		// TODO Auto-generated method stub
		stage.show();
	}
}
