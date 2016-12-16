package messageObjects;

import java.io.Serializable;
/**
* <h1>Class that sends simple Strings to client</h1>
* Simple String message that informs user about an error that happened on the server 
* (wrong password entered, username already taken etc.)
*
* @author  Kevin Neuschwander
* @version 1.0
* @since   2016-12-16
*/
public class ServerInfoMessage extends Message implements Serializable{
	private String message;

	public ServerInfoMessage(String message)
	{
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
