package server.backend;

import java.sql.Connection;
import java.util.ArrayList;

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
	
	//Update database before closing server
	public void lastUpdate()
	{
		isRunning = false;
		updateDatabase();
	}
	
	private synchronized void updateDatabase()
	{
		//Add new users
		
		//Update stats
	}
	
	

}
