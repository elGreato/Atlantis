package messageObjects;

import java.io.Serializable;

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
