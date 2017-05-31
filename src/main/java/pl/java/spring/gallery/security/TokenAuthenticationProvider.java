package pl.java.spring.gallery.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import pl.java.spring.gallery.service.TokenService;

/**
 * Custom authentication provider for token request
 * @author Wojciech Bêdkowski
 *
 */
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private TokenService tokenService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		Optional<String> token = Optional.ofNullable((String)authentication.getPrincipal());
		if (!token.isPresent() || token.get().isEmpty()) {
			throw new BadCredentialsException("Invalid token");
		}
		if (!tokenService.contains(token.get())) {//check if token is correct
			throw new BadCredentialsException("Invalid token or token expired");
		}
		PreAuthenticatedAuthenticationToken result = new PreAuthenticatedAuthenticationToken(token.get(), null);
		result.setAuthenticated(true);
		
		return result;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(PreAuthenticatedAuthenticationToken.class);
	}

}
