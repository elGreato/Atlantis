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
	private Boolean workingOnDatabase;
	
	public DatabaseInterface(Connection con)
	{
		this.dbAccessCon = con;
		newUsers = new ArrayList<UserInfo>();
		userUpdates = new ArrayList<UserInfo>();
		
		Thread dbUpdater = new Thread(this);
		dbUpdater.start();
	}
	
	
	//Loop of the thread that is responsible for constantly updating database, thread safe
	@Override
	public void run() {

		isRunning = true;
		
		workingOnDatabase = new Boolean(true);	
		
		//Thread updates database every 2 minutes
		while(isRunning)
		{
			
			updateDatabase();
			
			
			synchronized(workingOnDatabase)
			{
				workingOnDatabase.notify();
				workingOnDatabase = false;
			}
			
			
			try {
				//wait 2 minutes
				Thread.sleep(2*60*1000);
				
			} catch (InterruptedException e) {
				//continue normally
			}
			
			
			synchronized(workingOnDatabase)
			{
				workingOnDatabase = true;
			}
			
		}
	}
	
	
	//Get all user data from database
	public ArrayList<UserInfo> getUsers()
	{
		ArrayList<UserInfo> userInfos = new ArrayList<UserInfo>();
		

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
				userInfos.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return userInfos;
	}
	
	
	//Add user to list with users to be added in the next update
	public void addNewUserToDatabase(UserInfo addUser)
	{
		synchronized(newUsers)
		{
			newUsers.add(addUser);
		}
	}
	
	
	//Wait till other thread has finished updating the database. Then, update database for the last time before closing server.
	public void lastUpdate()
	{
		//Loop that says that thread should wait till the other thread has finished working on the database.
		synchronized(workingOnDatabase)
		{
			while(workingOnDatabase)
			{
				try {
				workingOnDatabase.wait();
				} catch (InterruptedException e) {
				//Thread will continue with updateDatabase() below
				}
			}
			isRunning = false;
		}
		
		
		updateDatabase();
	}
	
	
	//Updates database with new data (called every 2 minutes and once just before the server app is closed)
	private void updateDatabase()
	{
		
		
		//Add new users. Copy list(thread safe) and add the items copied to the database
		ArrayList<UserInfo> newUsersCopy;
		synchronized(newUsers)
		{
			newUsersCopy = new ArrayList<UserInfo>(newUsers);
			newUsers.clear();
		}
		Iterator<UserInfo> newUserCopyIterator = newUsersCopy.iterator();
		while(newUserCopyIterator.hasNext())
		{
			UserInfo addUser = newUserCopyIterator.next();
			try {
				PreparedStatement s = dbAccessCon.prepareStatement("INSERT INTO users VALUES (?, ?, 0, 0, 0);");
				s.setString(1, addUser.getUsername());
				s.setString(2, addUser.getPassword());
				s.executeUpdate();
			} catch (SQLException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			newUserCopyIterator.remove();
				
		}
		
		
		//Update user stats. Copy list(thread safe) and add the items copied to the database.
		ArrayList<UserInfo> userUpdatesCopy;
		synchronized(userUpdates)
		{
			userUpdatesCopy = new ArrayList<UserInfo>(userUpdates);
			userUpdates.clear();
		}
		Iterator<UserInfo> userUpdateCopyIterator = userUpdatesCopy.iterator();
		while(userUpdateCopyIterator.hasNext())
			{
			UserInfo updateUser = userUpdateCopyIterator.next();
			try {
				PreparedStatement s = dbAccessCon.prepareStatement("UPDATE users SET games_played = ?, games_won = ?, games_lost = ? WHERE username = ?;");
				s.setInt(1, updateUser.getGamesPlayed());
				s.setInt(2, updateUser.getGamesWon());
				s.setInt(3, updateUser.getGamesLost());
				s.setString(4, updateUser.getUsername());
				s.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			userUpdateCopyIterator.remove();
		}
		
	}
	
	

}
