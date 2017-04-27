package pl.java.spring.gallery.security;

/**
 * Token request json object
 * @author Wojciech Bêdkowski
 *
 */
public class TokenMessage {
	private String token;
	

    public String getToken() {
		return token;
	}


	public TokenMessage(String token) {
        this.token = token;
    }
	
	public TokenMessage(){
		
	}
}
