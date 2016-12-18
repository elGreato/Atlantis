package messageObjects.userMessages;

import java.io.Serializable;
/**
* <h1>Message for creating new users</h1>
* When a new user is created the user data is sent to the server for verification and creation of new user.
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class CreateUserMessage extends UserMessage implements Serializable{
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public CreateUserMessage(String username, String password)
	{
		super(username);
		this.password = password;
	}

}
