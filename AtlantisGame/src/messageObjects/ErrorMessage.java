package messageObjects;

import java.io.Serializable;

public class ErrorMessage extends Message implements Serializable{
	//Simple String message that informs user about an error that happened on the server (wrong password entered, username already taken etc.)
	private String message;

	public ErrorMessage(String message)
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
