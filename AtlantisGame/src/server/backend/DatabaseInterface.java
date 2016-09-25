package server.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
	}
	
	@Override
	public void run() {

		isRunning = true;
				
		//Update database every x minutes
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
	
	//Updates database with new data (called every x minutes and just before server app is closed)
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
