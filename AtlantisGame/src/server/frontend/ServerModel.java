package server.frontend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

import gameObjects.Game;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import server.backend.Lobby;
import server.backend.User;

public class ServerModel implements Runnable{
	
	private ServerSocket serverSocket;
	private Lobby lobby;
	private ServerView view;
	private Connection con;

	

	//Constructor
	public ServerModel(ServerView view)
	{
		this.view = view;
	}

	
	
	//Set up database connection for future transactions
	public void connectToDatabase()
	{
		String username = view.dbusernametxt.getText();
		String password = view.dbpasswordtxt.getText();
		
		//Test if setting up a connection to mysql is possible
		try {
			
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", username, password);
			view.dbconnectbtn.setDisable(true);
			view.dbconnectbtn.setText("Connected...");
			
		} catch (SQLException e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unable to connect");
			alert.setContentText("Could not connect to database. Check whether username and password are entered correctly and whether there is a mysql server running on the localhost");
			alert.showAndWait();
			
		}
		
		testDatabase();

	}
	
	//Test database for validity
	public void testDatabase()
	{
		try {
			Statement teststmt = con.createStatement();
			teststmt.executeUpdate("USE atlantisdb;");
			teststmt.executeQuery("SELECT username, userpwd, games_played, games_won, games_lost FROM users");
			view.serverstartbtn.setDisable(false);
			view.dbState.setText("Database (connected)");
			
		}
		catch(SQLException e)
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No instance existing");
			alert.setContentText("Did not detect a valid instance of atlantisdb on this host. Create?"
					+ "Warning: If there is an existing instance of 'atlantisdb', it will be deleted");
			Optional<ButtonType> answer = alert.showAndWait();
			if(answer.get() == ButtonType.OK)
			{
				createDatabase();
			}
		}
	}
	
	
	//Create new valid database
	public void createDatabase()
	{
		try {
			Statement createstmt = con.createStatement();
			createstmt.executeUpdate("DROP DATABASE IF EXISTS atlantisdb");
			createstmt.executeUpdate("CREATE DATABASE atlantisdb;");
			createstmt.executeUpdate("USE atlantisdb;");
			createstmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
					 + "username VARCHAR(15) PRIMARY KEY,"
					 + "userpwd VARCHAR(15),"
					 + "games_played INT(5),"
					 + "games_won INT(5),"
					 + "games_lost INT(5));");
			
			view.serverstartbtn.setDisable(false);
			view.dbState.setText("Database (connected)");
			
		} catch (SQLException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unexpected error");
			alert.setContentText("An unexpected error occured when trying to create the database. Try to restart the application or the system.");
			alert.showAndWait();;
		}
		
	}
	
	//Creates new thread for listener (JavaFX crashes when doing this without a thread)
	protected void startServer()
	{
		view.serverstartbtn.setDisable(true);
		view.serverstartbtn.setText("Running...");
		view.serverState.setText("Server (running)");
		Thread listener = new Thread(this);
		listener.start();
	}
	
	//Init last database update before closing server through lobby
	public void lastDbUpdate()
	{
		if(lobby != null)
		{
			lobby.lastDbUpdate();
		}
	}
	
	//Server loop
	@Override
	public void run() {
		
		try 
		{
			serverSocket = new ServerSocket(61452,10,null);
			lobby = new Lobby(con);
			
			while(true)
			{
				Socket client = serverSocket.accept();
				User user = new User(client, lobby);
				Thread thread = new Thread(user);
				thread.start();
			}
			
		} 
		catch(IOException e)
		{
			Platform.runLater(new Runnable(){
				public void run()
				{
					view.serverstartbtn.setDisable(false);
					view.serverstartbtn.setText("Start");
					view.serverState.setText("Server");
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Socket error");
					alert.setContentText("An error with the socket occured. Server stopped.");
					alert.showAndWait();
				}
			});
		}

		

	}
}
