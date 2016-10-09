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
	private TitledPane leaderboardSection;
	private GridPane joinGameSectionContent;
	private GridPane createGameControls;
	private GridPane chatContent;
	private GridPane leaderboardContent;
	
	
	private Label title;
	protected ObservableList<GameListItemDataModel> gameData;
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

	
	protected ObservableList<UserInfoDataModel> userData;
	protected TableView<UserInfoDataModel> userList;
	protected TableColumn<UserInfoDataModel, Integer> positionCol;
	protected TableColumn<UserInfoDataModel, String> usernameCol;
	protected TableColumn<UserInfoDataModel, Integer> gamesCol;
	protected TableColumn<UserInfoDataModel, Integer> gamesPlayedCol;
	protected TableColumn<UserInfoDataModel, Integer> gamesWonCol;
	protected TableColumn<UserInfoDataModel, Integer> gamesTieCol;
	protected TableColumn<UserInfoDataModel, Integer> gamesLostCol;
	protected TableColumn<UserInfoDataModel, Integer> pointsCol;
	
	public LobbyView()
	{
		root = new GridPane();
		
		//Init table for games
		gameData = FXCollections.observableArrayList();
		gameList = new TableView<GameListItemDataModel>();
		gameList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		//Columns for table
		gameNameCol = new TableColumn<GameListItemDataModel,String>("Name");
		gameNameCol.setResizable(false);
		gamePassCol = new TableColumn<GameListItemDataModel,String>("Password");
		gamePassCol.setResizable(false);
		playersCol = new TableColumn<GameListItemDataModel, Integer>("Players");
		playersCol.setResizable(false);
		playersRegCol = new TableColumn<GameListItemDataModel, Integer>("Reg.");
		playersRegCol.setResizable(false);
		playersMaxCol = new TableColumn<GameListItemDataModel, Integer>("Max.");
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
		chatHistory.setEditable(false);
		chatContent.add(chatButton, 1, 1);
		chatContent.add(chatField, 0, 1);
		chatSection = new TitledPane("Chat", chatContent);
		
		//Init table for users
		userData = FXCollections.observableArrayList();
		userList = new TableView<UserInfoDataModel>();
		userList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		//Columns for table
		positionCol = new TableColumn<UserInfoDataModel, Integer>("Position");
		positionCol.setResizable(false);
		usernameCol = new TableColumn<UserInfoDataModel, String>("Username");
		usernameCol.setResizable(false);
		gamesCol = new TableColumn<UserInfoDataModel, Integer>("Game Stats.");
		gamesCol.setResizable(false);
		gamesPlayedCol = new TableColumn<UserInfoDataModel, Integer>("Played");
		gamesPlayedCol.setResizable(false);
		gamesWonCol = new TableColumn<UserInfoDataModel, Integer>("Won");
		gamesWonCol.setResizable(false);
		gamesTieCol = new TableColumn<UserInfoDataModel, Integer>("Tie");
		gamesTieCol.setResizable(false);
		gamesLostCol = new TableColumn<UserInfoDataModel, Integer>("Lost");
		gamesLostCol.setResizable(false);
		pointsCol = new TableColumn<UserInfoDataModel, Integer>("Points");
		pointsCol.setResizable(false);
		
		positionCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("position"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, String>("username"));
		gamesCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("games"));
		gamesPlayedCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("gamesPlayed"));
		gamesWonCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("gamesWon"));
		gamesTieCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("gamesTie"));
		gamesLostCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("gamesLost"));
		pointsCol.setCellValueFactory(new PropertyValueFactory<UserInfoDataModel, Integer>("points"));
		
		gamesCol.getColumns().addAll(gamesPlayedCol,gamesWonCol,gamesTieCol,gamesLostCol);
		userList.getColumns().addAll(positionCol,usernameCol,gamesCol,pointsCol);
		userList.setItems(userData);

		leaderboardContent = new GridPane();
		leaderboardContent.add(userList, 0, 0);
		leaderboardSection = new TitledPane("Leaderboard",leaderboardContent);
		leaderboardSection.setMaxHeight(Double.MAX_VALUE);
		title = new Label("ATLANTIS LOBBY");
		
		root.add(title,0,0,3,1);
		root.add(joinGameSection,0,1,1,2);
		root.add(createGameSection, 0, 3);
		root.add(chatSection, 1, 3);
		root.add(leaderboardSection, 3, 2,1,2);
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
