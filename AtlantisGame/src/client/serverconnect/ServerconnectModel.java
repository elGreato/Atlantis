package client.serverconnect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ServerconnectModel implements Runnable{
	
	private ServerconnectView view;
	
	private String subnetAdress;
	private int ipEnding;
	private int negativeTests;
	private boolean hasFoundIP;
	
	public ServerconnectModel(ServerconnectView view)
	{
		this.view = view;
		
	}
	
	//sets up connection to server
	protected void setUpManualConnection(String text)
	{
		
		try{
			Socket socket = new Socket(text, 61452);
			showLogin(socket);
		}
		catch(IOException e)
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setHeaderText("Connection error");
			alert.setContentText("IP adress wrong. Could not connect to server.");
			alert.showAndWait();
			view.autoConnectButton.setDisable(false);
			view.manualConnectButton.setDisable(false);
		}
	}

	private void showLogin(Socket socket) {
		// TODO Auto-generated method stub
		LoginView loginView = new LoginView();
		LoginModel loginModel = new LoginModel(socket, loginView);
		LoginController loginController = new LoginController(loginView, loginModel);
		loginView.start();
		view.close();
	}

	public void scanLAN() {
		// TODO Auto-generated method stub
		ipEnding = 0;
		negativeTests = 0;
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			subnetAdress = (localHost.getHostAddress()).substring(0, 10);
			System.out.println("Found it " +subnetAdress);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			subnetAdress = "192.168.1.";
		}
		
		
		hasFoundIP = false;
		for(int i = 0; i<255;i++)
		{
			Thread t = new Thread(this);
			t.start();
		}
		
	}

	@Override
	public void run() {
		String testableIP = subnetAdress + takeIP();
		System.out.println(testableIP);
		
		try
		{
			Socket testSocket = new Socket(testableIP,61452);
			if(processHasFoundServer())
			{
				Platform.runLater(() ->
				{
					showLogin(testSocket);
				});
				
			}
			else
			{
				testSocket.close(); 
			}
			System.out.println("No catch" + testableIP);
		}
		catch(IOException e)
		{
			addNegativeTest();
			
			if(getNegativeTests() == 255)
			{
				Platform.runLater(()->
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Could not find server");
					alert.setContentText("No running server on network detected. Please try to enter the IP manually.");
					alert.showAndWait();
					view.autoConnectButton.setDisable(false);
					view.manualConnectButton.setDisable(false);
				}); 
			}
		}
	}


	private synchronized boolean processHasFoundServer() {
		if(hasFoundIP)
		{
			return false;
		}
		else
		{
			hasFoundIP = true;
			return true;
		}
		
	}

	private synchronized int getNegativeTests() {
		// TODO Auto-generated method stub
		return negativeTests;
	}

	private synchronized void addNegativeTest() {
		negativeTests+=1;
		
	}

	public synchronized int takeIP()
	{
		ipEnding+=1;
		return ipEnding;
	}
}
