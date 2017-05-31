package pl.java.spring.gallery.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import pl.java.spring.gallery.service.ServiceException;
import pl.java.spring.gallery.service.TokenService;
import pl.java.spring.gallery.service.UserService;

/**
 * Custom authentication provider for webservice by username and password
 * @author Wojtek
 *
 */
@Component
public class UserCredentialsAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	TokenService tokenService;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<String> username = Optional.ofNullable((String)authentication.getPrincipal());
        Optional<String> password = Optional.ofNullable((String)authentication.getCredentials());

        if (!username.isPresent() || !password.isPresent()) {
            throw new BadCredentialsException("Invalid Domain User Credentials");
        }
        PreAuthenticatedAuthenticationToken result;
        try{
        	String token = tokenService.store(username.get(), password.get());//try to generate new token
        	result = new PreAuthenticatedAuthenticationToken(token,null);
        	result.setAuthenticated(true);
        }catch(ServiceException e){//if failed, authentication failed,token service did not authenticated requested user
        	result = new PreAuthenticatedAuthenticationToken(null,null);
        	result.setAuthenticated(false);
        }

        return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
