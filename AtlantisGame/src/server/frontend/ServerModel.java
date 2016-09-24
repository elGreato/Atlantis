package server.frontend;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;


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
		
		//Test if connection to database is possible
		try {
			
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/", username, password);
			view.dbconnectbtn.setDisable(true);
			view.dbconnectbtn.setText("Connected..");
			
		} catch (SQLException e) {
			
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Unable to connect");
			alert.setContentText("Could not connect to database. Check whether username and password are entered correctly and whether there is a mysql server running on the localhost");
			alert.showAndWait();
			
		}
		
		//Test if database for atlantis exists
		
		try {
			Statement teststmt = con.createStatement();
			teststmt.executeUpdate("USE ATLANTISDB;");
			
		}
		catch(SQLException e)
		{
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("No instance existing");
			alert.setContentText("Did not detect an instance of atlantisdb on localhost. Create?");
			Optional<ButtonType> answer = alert.showAndWait();
			if(answer.get() == ButtonType.OK)
			{
				try {
					Statement createstmt = con.createStatement();
					createstmt.executeUpdate("CREATE DATABASE IF NOT EXISTS ATLANTISDB;");
					createstmt.executeUpdate("USE ATLANTISDB;");
					createstmt.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
							 + "username VARCHAR(10) PRIMARY KEY,"
							 + "userpwd VARCHAR(10),"
							 + "games_played INT(5),"
							 + "games_won INT(5),"
							 + "games_lost INT(5));");
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		}
	}
	
	
	
	//Creates new thread for listener (JavaFX crashes when doing this without a thread)
	protected void startServer()
	{
		view.serverstartbtn.setDisable(true);
		view.serverstartbtn.setText("Running...");
		Thread listener = new Thread(this);
		listener.start();
	}
	

	
	//Server loop
	@Override
	public void run() {
		
		try 
		{
			serverSocket = new ServerSocket(61452,10,null);
			
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
					
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Socket error");
					alert.setContentText("An error with the socket occured. Server stopped.");
					alert.showAndWait();
				}
			});
		}

		

	}
}
