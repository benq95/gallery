package pl.java.spring.gallery.controller;

/**
 * json map object for inserting new user
 * @author Wojciech Bêdkowski
 *
 */
public class NewUserRequest {
	private String username, password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public NewUserRequest(String username, String password){
		this.username = username;
		this.password = password;
	}
	
	public NewUserRequest(){
		
	}
}
