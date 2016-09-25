package messageObjects;

import java.io.Serializable;

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
		this.setUsername(username);
		this.password = password;
	}

}
