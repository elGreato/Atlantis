package server.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseInterface implements Runnable {

	private Connection dbAccessCon;
	private ArrayList<UserInfo> newUsers;
	private ArrayList<UserInfo> userUpdates;
	private boolean isRunning;
	
	
	public DatabaseInterface(Connection con)
	{
		this.dbAccessCon = con;
		newUsers = new ArrayList<UserInfo>();
		userUpdates = new ArrayList<UserInfo>();
		
		Thread dbUpdater = new Thread(this);
		dbUpdater.start();
	}
	
	@Override
	public void run() {

		isRunning = true;
				
		//Update database every 2 minutes
		while(isRunning)
		{
			updateDatabase();
			
			//wait 2 minutes
			try {
				Thread.sleep(2*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	//Get all user data from database
	public ArrayList<UserInfo> getUsers()
	{
		ArrayList<UserInfo> userInfo = new ArrayList<UserInfo>();
		

		try {
			
			Statement getUsersFromDb = dbAccessCon.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = getUsersFromDb.executeQuery("SELECT * from users");
			
			while (rs.next())
			{
				String username = rs.getString("username");
				String userpwd = rs.getString("userpwd");
				int games_played = rs.getInt("games_played");
				int games_won = rs.getInt("games_won");
				int games_lost = rs.getInt("games_lost");
				UserInfo user = new UserInfo(username, userpwd, games_played, games_won, games_lost);
				userInfo.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userInfo;
	}
	
	//Add user to list with users to be added in the next update
	public void addNewUserToDatabase(UserInfo addUser)
	{
		newUsers.add(addUser);
	}
	
	//Update database before closing server
	public void lastUpdate()
	{
		isRunning = false;
		updateDatabase();
	}
	
	//Updates database with new data (called every 2 minutes and just before server app is closed)
	private synchronized void updateDatabase()
	{
		//Add new users
		if(!newUsers.isEmpty())
		{
			Iterator<UserInfo> newUserIterator = newUsers.iterator();
			while(newUserIterator.hasNext())
			{
				UserInfo addUser = newUserIterator.next();
				try {
					PreparedStatement s = dbAccessCon.prepareStatement("INSERT INTO users VALUES (?, ?, 0, 0, 0);");
					s.setString(1, addUser.getUsername());
					s.setString(2, addUser.getPassword());
					s.executeUpdate();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newUserIterator.remove();
			}
		}
		//Update stats
	}
	
	

}
