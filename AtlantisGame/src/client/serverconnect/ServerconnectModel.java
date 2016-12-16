package client.serverconnect;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import client.login.LoginController;
import client.login.LoginModel;
import client.login.LoginView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
/**
* <h1>Logic for serverconnect window</h1>
* Tries to connect to server, either automatically via lan or manually with the IP adress entered by the user
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
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
		LoginView loginView = new LoginView();
		LoginModel loginModel = new LoginModel(socket, loginView);
		LoginController loginController = new LoginController(loginView, loginModel);
		loginView.start();
		view.close();
	}

	public void scanLAN() {
		ipEnding = 0;
		negativeTests = 0;
		InetAddress localHost;
		try {
			localHost = InetAddress.getLocalHost();
			String ipAdress = localHost.getHostAddress();
			int numPoints = 0;
			for(int i = 0;i<ipAdress.length();i++)
			{
				if(ipAdress.charAt(i) == '.')
				{
					numPoints+=1;
					if(numPoints == 3)
					{
						subnetAdress = ipAdress.substring(0,i+1);
						break;
					}
				}
			}
		} catch (UnknownHostException e) {
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
