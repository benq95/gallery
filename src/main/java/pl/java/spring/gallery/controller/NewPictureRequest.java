package pl.java.spring.gallery.controller;

/**
 * json map object for inserting new picture
 * @author Wojciech Bêdkowski
 *
 */
public class NewPictureRequest {
	private String token;
	private byte [] picture;
	
	public NewPictureRequest() {}
	
	public byte [] getPicture() {
		return picture;
	}
	public void setPicture(byte [] picture) {
		this.picture = picture;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
}
