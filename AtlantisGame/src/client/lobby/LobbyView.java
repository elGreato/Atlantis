package client.lobby;

import java.util.ArrayList;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import messageObjects.GameListItem;

public class LobbyView {
	private Stage stage;
	private Scene scene;
	
	private GridPane root;
	private TitledPane createGameSection;
	private TitledPane joinGameSection;
	private GridPane joinGameSectionContent;
	private GridPane createGameControls;
	
	private Label title;
	private TableView<GameListItem> gameList;
	protected TableColumn<GameListItem, String> gameNameCol;
	protected TableColumn<GameListItem, String> gamePassCol;
	protected TableColumn<GameListItem, Integer> playersCol;
	protected TableColumn<GameListItem, Integer> playersRegCol;
	protected TableColumn<GameListItem, Integer> playersMaxCol;
	protected TextField joinPassword;
	protected Button joinButton;
	
	private Label createGameNamelbl;
	protected TextField createGameNametxt;
	private Label createGamePasswordlbl;
	protected TextField createGamePasswordtxt;
	private Label createNumPlayerslbl;
	protected ComboBox<Integer> createNumPlayerscbx;
	protected Button createButton;
	
	private TableView leaderBoard;
	
	protected ObservableList<GameListItem> gameData;
	
	
	public LobbyView()
	{
		
		gameData = FXCollections.observableArrayList();
		
		
		root = new GridPane();

		
		
		gameList = new TableView<GameListItem>();
		//Columns for table
		gameNameCol = new TableColumn("Name");
		gameNameCol.setResizable(false);
		gamePassCol = new TableColumn("PW");
		gamePassCol.setResizable(false);
		playersCol = new TableColumn("Players");
		playersCol.setResizable(false);
		playersRegCol = new TableColumn("Reg.");
		playersRegCol.setResizable(false);
		playersMaxCol = new TableColumn("Max.");
		playersMaxCol.setResizable(false);
		
		//Connect table content to variables
		gameNameCol.setCellValueFactory(new PropertyValueFactory<GameListItem, String>("gameName"));
		//table cell boolean to string by http://stackoverflow.com/questions/36436169/boolean-to-string-in-tableview-javafx
		gamePassCol.setCellValueFactory(cellData -> {
            boolean hasPassword = cellData.getValue().getHasPassword();
            String cellInfo;
            if(hasPassword)
            {
                cellInfo = "Yes";
            }
            else
            {
                cellInfo = "No";
            }

         return new ReadOnlyStringWrapper(cellInfo);
        });
		playersRegCol.setCellValueFactory(new PropertyValueFactory<GameListItem, Integer>("registeredPlayers"));
		playersMaxCol.setCellValueFactory(new PropertyValueFactory<GameListItem, Integer>("maxPlayers"));
		
		//Draw table
		playersCol.getColumns().addAll(playersRegCol, playersMaxCol);
		gameList.getColumns().addAll(gameNameCol, gamePassCol, playersCol);
		gameList.setPrefWidth(gameNameCol.getWidth() + gamePassCol.getWidth() + playersCol.getWidth()+2);
		gameList.autosize();
		gameList.setItems(gameData);
		
		//Example entry
		gameData.add(new GameListItem("ProGame",true, 3,4));
		gameData.add(new GameListItem("TheHackerz",false, 1,2));
		
		
		joinButton = new Button("Join game");
		joinPassword = new TextField("Password");
		joinGameSectionContent = new GridPane();
		joinGameSectionContent.add(gameList, 0,0,2,1);
		joinGameSectionContent.add(joinPassword,0,1);
		joinGameSectionContent.add(joinButton,1,1);
		joinGameSection = new TitledPane("Join game", joinGameSectionContent);
		joinGameSection.setExpanded(true);
		
		
		createGameControls = new GridPane();
		createGameNamelbl = new Label("Name: ");
		createGameNametxt = new TextField();
		createGamePasswordlbl = new Label("Password: ");
		createGamePasswordtxt = new TextField();
		createNumPlayerslbl = new Label("No. of players: ");
		createNumPlayerscbx = new ComboBox();
		createNumPlayerscbx.getItems().addAll(2, 3, 4);
		createButton = new Button("Create");
		
		createGameControls.add(createGameNamelbl, 0, 0);
		createGameControls.add(createGameNametxt, 1, 0);
		createGameControls.add(createGamePasswordlbl, 0, 1);
		createGameControls.add(createGamePasswordtxt, 1, 1);
		createGameControls.add(createNumPlayerslbl, 0, 2);
		createGameControls.add(createNumPlayerscbx, 1, 2);
		createGameControls.add(createButton, 1, 3);
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
