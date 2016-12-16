package messageObjects;

import java.io.Serializable;
/**
* <h1>Superclass for messages containing user data</h1>
* Contains the username which is required for every user related message.
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class UserMessage extends Message implements Serializable {
	private String username;

	public UserMessage(String username) {
		super();
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	
}
