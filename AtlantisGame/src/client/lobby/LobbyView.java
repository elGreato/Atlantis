package client.lobby;

import java.util.ArrayList;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import messageObjects.GameListItem;

public class LobbyView {
	private Stage stage;
	private Scene scene;
	
	private GridPane root;
	protected TitledPane createGameSection;
	protected TitledPane joinGameSection;
	protected TitledPane chatSection;
	protected TitledPane userInfoSection;
	protected TitledPane leaderboardSection;
	private GridPane joinGameSectionContent;
	private GridPane createGameControls;
	private GridPane chatContent;
	private GridPane userInfoContent;
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
	protected Label createNumAIPlayerslbl;
	protected ComboBox<Integer> createNumAIPlayerscbx;
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
	
	private Label userInfoNameDescription;
	protected Label userInfoName;
	private Label userInfoPositionDescription;
	protected Label userInfoPosition;
	private Label userInfoPointsDescription;
	protected Label userInfoPoints;
	private Label userInfoGamesPlayedDescription;
	protected Label userInfoGamesPlayed;
	private Label userInfoGamesWonDescription;
	protected Label userInfoGamesWon;
	private Label userInfoGamesTieDescription;
	protected Label userInfoGamesTie;
	private Label userInfoGamesLostDescription;
	protected Label userInfoGamesLost;
	
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
		gameList.setPlaceholder(new Label("No open games existing at the moment.\nCreate your own below!"));
		
		
		joinButton = new Button("Join game");
		joinPassword = new TextField();
		joinPasswordlbl = new Label("Password: ");
		joinGameSectionContent = new GridPane();
		joinGameSectionContent.add(gameList, 0,0,3,1);
		joinGameSectionContent.add(joinPasswordlbl, 0, 1);
		joinGameSectionContent.add(joinPassword,1,1);
		joinGameSectionContent.add(joinButton,2,1);
		joinGameSection = new TitledPane("Join game", joinGameSectionContent);
		joinGameSection.setCollapsible(false);
		joinGameSection.setMaxHeight(Double.MAX_VALUE);
		
		
		createGameControls = new GridPane();
		createGameNamelbl = new Label("Name: ");
		createGameNametxt = new TextField();
		createGamePasswordlbl = new Label("Password (opt.): ");
		createGamePasswordtxt = new TextField();
		createNumPlayerslbl = new Label("No. of players: ");
		createNumPlayerscbx = new ComboBox<Integer>();
		createNumPlayerscbx.getItems().addAll(2, 3, 4);
		createNumPlayerscbx.setValue(2);
		createNumAIPlayerslbl = new Label("of which AI: ");
		createNumAIPlayerscbx = new ComboBox<Integer>();
		createNumAIPlayerscbx.getItems().addAll(0,1);
		createNumAIPlayerscbx.setValue(0);
		createButton = new Button("Create");
		
		createGameControls.add(createGameNamelbl, 0, 0);
		createGameControls.add(createGameNametxt, 1, 0);
		createGameControls.add(createGamePasswordlbl, 0, 1);
		createGameControls.add(createGamePasswordtxt, 1, 1);
		createGameControls.add(createNumPlayerslbl, 0, 2);
		createGameControls.add(createNumPlayerscbx, 1, 2);
		createGameControls.add(createNumAIPlayerslbl, 0, 3);
		createGameControls.add(createNumAIPlayerscbx, 1, 3);
		createGameControls.add(createButton, 1, 4);
		createGameSection = new TitledPane("Create game",createGameControls);
		createGameSection.setCollapsible(false);
		createGameSection.setMaxHeight(Double.MAX_VALUE);
		
		chatHistory = new TextArea();
		chatField = new TextField();
		chatField.setMaxWidth(Double.MAX_VALUE);
		chatButton = new Button("Send");
		chatContent = new GridPane();
		chatContent.add(chatHistory, 0, 0, 2, 1);
		chatHistory.setEditable(false);
		chatHistory.setMaxWidth(Double.MAX_VALUE);
		chatHistory.wrapTextProperty().set(true);;
		chatContent.add(chatButton, 1, 1);
		chatContent.add(chatField, 0, 1);
		chatSection = new TitledPane("Chat", chatContent);
		chatSection.setCollapsible(false);
		
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
		userList.setFixedCellSize(25);
		userList.setPrefHeight(userList.getFixedCellSize()*12);
		
		leaderboardContent = new GridPane();
		leaderboardContent.add(userList, 0, 0);
		leaderboardSection = new TitledPane("Leaderboard",leaderboardContent);
		leaderboardSection.setCollapsible(false);
		leaderboardSection.setMaxHeight(Double.MAX_VALUE);
		
		userInfoNameDescription = new Label("Username: ");
		userInfoPositionDescription = new Label("Position: ");
		userInfoPointsDescription = new Label("Points: ");
		userInfoGamesPlayedDescription = new Label("Games played: ");
		userInfoGamesWonDescription = new Label("Games won: ");
		userInfoGamesTieDescription = new Label("Games tie: ");
		userInfoGamesLostDescription = new Label("Games lost: ");

		userInfoName = new Label();
		userInfoName.setFont(Font.font("Brush Script MT", FontWeight.BOLD, 50));
		userInfoPosition = new Label();
		userInfoPoints = new Label();
		userInfoGamesPlayed = new Label();
		userInfoGamesWon = new Label();
		userInfoGamesTie = new Label();
		userInfoGamesLost = new Label();
		
		userInfoContent = new GridPane();
		userInfoContent.add(userInfoNameDescription, 0, 0);
		userInfoContent.add(userInfoPositionDescription, 0, 1);
		userInfoContent.add(userInfoPointsDescription, 0, 2);
		userInfoContent.add(userInfoGamesPlayedDescription, 0, 3);
		userInfoContent.add(userInfoGamesWonDescription, 0, 4);
		userInfoContent.add(userInfoGamesTieDescription, 0, 5);
		userInfoContent.add(userInfoGamesLostDescription, 0, 6);
		userInfoContent.add(userInfoName, 1, 0,2,1);
		userInfoContent.add(userInfoPosition, 1, 1);
		userInfoContent.add(userInfoPoints, 1, 2);
		userInfoContent.add(userInfoGamesPlayed, 1, 3);
		userInfoContent.add(userInfoGamesWon, 1, 4);
		userInfoContent.add(userInfoGamesTie, 1, 5);
		userInfoContent.add(userInfoGamesLost, 1, 6);
		
		for(Node n: userInfoContent.getChildren())
		{
			userInfoContent.setHalignment(n, HPos.RIGHT);
		}
		
		userInfoSection = new TitledPane("Your stats",userInfoContent);
		userInfoSection.setCollapsible(false);
		
		title = new Label("Journey of Atlantis");
		title.setFont(Font.font("Brush Script MT",FontWeight.BOLD, 70));
		title.setTextFill(Color.DARKBLUE);
		
		root.add(joinGameSection, 0, 1, 1, 2);
		root.add(createGameSection, 0, 3);
		root.add(chatSection, 1, 3);
		root.add(userInfoSection, 1, 1);
		root.add(leaderboardSection, 1, 2, 1, 1);
		//changes transparency of all the nodes in root
		for(Node n : root.getChildren())
		{
			n.setOpacity(0.85);
			
		}
		
		root.add(title, 0, 0, 4, 1);
		root.setHalignment(title, HPos.CENTER);
		BackgroundSize backgroundSize = new BackgroundSize(root.getWidth(),root.getHeight(), true, true, true, true);
		BackgroundImage myBI= new BackgroundImage(new Image(getClass().getResourceAsStream("images4lobby/lobbyBackground.png")),
		        BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, backgroundSize);
		root.setBackground(new Background(myBI));
		//root.setBackground(new Background(new BackgroundFill(Color.AZURE,CornerRadii.EMPTY,Insets.EMPTY)));
		
		
		scene = new Scene(root);
		stage = new Stage();
		stage.setTitle("Atlantis");
		stage.setScene(scene);
		stage.setResizable(false);
		stage.sizeToScene();
		
	}

	public void start() {
		// TODO Auto-generated method stub
		stage.show();
		chatHistory.setPrefWidth(chatContent.getWidth());
		chatField.setPrefWidth(0.9*chatContent.getWidth());
		chatButton.setPrefWidth(0.1*chatContent.getWidth());
		double rootHeight = root.getHeight();
		double scaling = 1.0d;
		if(rootHeight > 0.95*Screen.getPrimary().getVisualBounds().getHeight())
		{
			scaling = 0.95*Screen.getPrimary().getVisualBounds().getHeight()/rootHeight;
			System.out.println("primary: " + 0.8*Screen.getPrimary().getVisualBounds().getHeight() + "rootHeight: " + rootHeight);
			System.out.println("Scaling: "+ scaling);
			stage.setHeight(scaling * stage.getHeight());
			
		}

		
	}
	

}
