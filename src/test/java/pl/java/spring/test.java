package pl.java.spring;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

public class test {
	@Test
	public void testSpringEncoder() {
	    PasswordEncoder encoder = new Md5PasswordEncoder();
	    String hashedPass = encoder.encodePassword("pass", null);

	    assertEquals("a564de63c2d0da68cf47586ee05984d7", hashedPass);
	}
}
