package messageObjects;

import java.io.Serializable;
/**
* <h1>Message that sends login data</h1>
* Sends login data to server when a client tried to login in. 
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class LoginMessage extends UserMessage implements Serializable{
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public LoginMessage(String username, String password)
	{
		super(username);
		this.password = password;
	}

}
