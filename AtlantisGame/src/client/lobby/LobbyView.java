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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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
	private TitledPane chatSection;
	private GridPane joinGameSectionContent;
	private GridPane createGameControls;
	private GridPane chatContent;
	
	private Label title;
	protected TableView<GameListItemDataModel> gameList;
	protected TableColumn<GameListItemDataModel, String> gameNameCol;
	protected TableColumn<GameListItemDataModel, String> gamePassCol;
	protected TableColumn<GameListItemDataModel, Integer> playersCol;
	protected TableColumn<GameListItemDataModel, Integer> playersRegCol;
	protected TableColumn<GameListItemDataModel, Integer> playersMaxCol;
	private Label joinPasswordlbl;
	protected TextField joinPassword;
	protected Button joinButton;
	
	private Label createGameNamelbl;
	protected TextField createGameNametxt;
	private Label createGamePasswordlbl;
	protected TextField createGamePasswordtxt;
	private Label createNumPlayerslbl;
	protected ComboBox<Integer> createNumPlayerscbx;
	protected Button createButton;
	
	protected TextArea chatHistory;
	protected TextField chatField;
	protected Button chatButton;
	
	private TableView leaderBoard;
	
	protected ObservableList<GameListItemDataModel> gameData;
	
	
	public LobbyView()
	{
		gameData = FXCollections.observableArrayList();
		System.out.println("Initialized gameList");
		
		root = new GridPane();

		
		
		gameList = new TableView<GameListItemDataModel>();
		gameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
		gameNameCol.setCellValueFactory(new PropertyValueFactory<GameListItemDataModel, String>("gameName"));
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
		playersRegCol.setCellValueFactory(new PropertyValueFactory<GameListItemDataModel, Integer>("registeredPlayers"));
		playersMaxCol.setCellValueFactory(new PropertyValueFactory<GameListItemDataModel, Integer>("maxPlayers"));
		
		//Draw table
		playersCol.getColumns().addAll(playersRegCol, playersMaxCol);
		gameList.getColumns().addAll(gameNameCol, gamePassCol, playersCol);
		gameList.setPrefWidth(gameNameCol.getWidth() + gamePassCol.getWidth() + playersCol.getWidth()+2);
		gameList.autosize();
		gameList.setItems(gameData);
		
		//Example entry
		gameData.add(new GameListItemDataModel(new GameListItem("ProGame",true, 3,4)));
		gameData.add(new GameListItemDataModel(new GameListItem("TheHackerz",false, 1,2)));
		System.out.println("added items to game list");
		
		
		joinButton = new Button("Join game");
		joinPassword = new TextField();
		joinPasswordlbl = new Label("Password: ");
		joinGameSectionContent = new GridPane();
		joinGameSectionContent.add(gameList, 0,0,3,1);
		joinGameSectionContent.add(joinPasswordlbl, 0, 1);
		joinGameSectionContent.add(joinPassword,1,1);
		joinGameSectionContent.add(joinButton,2,1);
		joinGameSection = new TitledPane("Join game", joinGameSectionContent);
		joinGameSection.setExpanded(true);
		
		
		createGameControls = new GridPane();
		createGameNamelbl = new Label("Name: ");
		createGameNametxt = new TextField();
		createGamePasswordlbl = new Label("Password (opt.): ");
		createGamePasswordtxt = new TextField();
		createNumPlayerslbl = new Label("No. of players: ");
		createNumPlayerscbx = new ComboBox<Integer>();
		createNumPlayerscbx.getItems().addAll(2, 3, 4);
		createNumPlayerscbx.setValue(2);
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
		
		chatHistory = new TextArea();
		chatField = new TextField();
		chatButton = new Button("Send");
		chatContent = new GridPane();
		chatContent.add(chatHistory, 0, 0, 2, 1);
		chatContent.add(chatField, 0, 1);
		chatContent.add(chatButton, 1, 1);
		chatSection = new TitledPane("Chat", chatContent);
		
		title = new Label("ATLANTIS LOBBY");
		
		root.add(title,0,0,3,1);
		root.add(joinGameSection,0,1);
		root.add(createGameSection, 0, 2);
		root.add(chatSection, 1, 2);
		
		createGameSection.setMaxHeight(Double.MAX_VALUE);
		scene = new Scene(root);
		stage = new Stage();
		stage.setScene(scene);
	}

	public void start() {
		// TODO Auto-generated method stub
		stage.show();
		
	}
	

}
