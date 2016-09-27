package client.lobby;

import java.util.ArrayList;

import gameObjects.GameListItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LobbyView {
	private Stage stage;
	private Scene scene;
	
	private GridPane root;
	private TitledPane createGameSection;
	private TitledPane joinGameSection;
	private GridPane createGameControls;
	
	private Label title;
	private TableView<GameListItem> gameList;
	private TableColumn<GameListItem, String> gameNameCol;
	private TableColumn<GameListItem, ?> playersCol;
	private TableColumn<GameListItem, Integer> playersRegCol;
	private TableColumn<GameListItem, Integer> playersMaxCol;
	private TableView leaderBoard;
	private ObservableList<GameListItem> gameData;
	
	public LobbyView()
	{
		
		gameData = FXCollections.observableArrayList();
		
		
		root = new GridPane();
		createGameControls = new GridPane();
		
		gameList = new TableView<GameListItem>();
		gameNameCol = new TableColumn("Name");
		gameNameCol.setResizable(false);
		playersCol = new TableColumn("Players");
		playersCol.setResizable(false);
		playersRegCol = new TableColumn("Reg.");
		playersRegCol.setResizable(false);
		playersMaxCol = new TableColumn("Max.");
		playersMaxCol.setResizable(false);

		gameNameCol.setCellValueFactory(new PropertyValueFactory<GameListItem, String>("gameName"));
		playersRegCol.setCellValueFactory(new PropertyValueFactory<GameListItem, Integer>("registeredPlayers"));
		playersMaxCol.setCellValueFactory(new PropertyValueFactory<GameListItem, Integer>("maxPlayers"));
		
		playersCol.getColumns().addAll(playersRegCol, playersMaxCol);
		gameList.getColumns().addAll(gameNameCol, playersCol);
		gameList.setPrefWidth(gameNameCol.getWidth() + playersCol.getWidth()+2);
		gameList.autosize();
		gameList.setItems(gameData);
		
		gameData.add(new GameListItem("ProGame",3,4));
		
		joinGameSection = new TitledPane("Join game",gameList);
		joinGameSection.setExpanded(true);
		createGameSection = new TitledPane("Create game",createGameControls);
		createGameSection.setExpanded(true);
		
		title = new Label("ATLANTIS LOBBY");
		
		root.add(title,0,0,3,1);
		root.add(joinGameSection,0,1);
		root.add(createGameSection, 0, 2);
		
		
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
	}

	public void start() {
		// TODO Auto-generated method stub
		stage.show();
	}
	
	public void updateGameList(ArrayList<GameListItem> update)
	{
		gameData.clear();
		gameData.addAll(update);
	}
}
