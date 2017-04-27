package pl.java.spring.gallery.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * Custom authentication filter for webservice token authentication
 * @author Wojciech Bêdkowski
 *
 */
public class AuthenticationFilter extends GenericFilterBean{
	
	protected AuthenticationManager authenticationManager;
    
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
    	this.authenticationManager = authenticationManager;
    }

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MultipleReadHttpRequest httpRequest = new MultipleReadHttpRequest((HttpServletRequest)request);
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String username = null;
		String password = null;
		String token = null;
		//get request body and encode json object
		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			InputStream inputStream = httpRequest.getInputStream();
			if(inputStream==null){
				fail(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        	return;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			/* report an error */ }
		JSONObject jsonObject;
		
		try{
			jsonObject = new JSONObject(jb.toString());
		} catch (JSONException e) {
			fail(httpResponse,HttpServletResponse.SC_UNAUTHORIZED);//send error if no json
   		 	return;
		}
		
		//check if json contains username or password
		try{
			username = jsonObject.getString("username");
			password = jsonObject.getString("password");
		}catch (JSONException e) {/*Do nothing*/}
		
		//check if json contins token
		try{
			token = jsonObject.getString("token");
		}catch (JSONException e) {/*Do nothing*/}
		
        //
        if(!httpRequest.getMethod().equals("POST")){
        	fail(httpResponse,HttpServletResponse.SC_UNAUTHORIZED);//send error if no post method
        	return;
        }
        
        //create authentication token for specific request type(user credentials or token)
        Authentication authenticatonToken;
        if((username!=null)&&(password!=null)){
        	authenticatonToken = new UsernamePasswordAuthenticationToken(username, password);
        }else if(token!=null){
        	authenticatonToken = new PreAuthenticatedAuthenticationToken(token, null);
        }else{
        	fail(httpResponse, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);//send error if some internal problem occured
        	return;
        }
        
        //authenticate with proper authentication provider
		Authentication responseAuthentication = this.authenticationManager.authenticate(authenticatonToken);
    	if((responseAuthentication==null) || !responseAuthentication.isAuthenticated()){
    		 fail(httpResponse,HttpServletResponse.SC_UNAUTHORIZED);//send error if authorization failed
    		 return;
    	}
    	SecurityContextHolder.getContext().setAuthentication(responseAuthentication);
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        if(username!=null){//end http connection and send back token if request json contained username and password
        	token = (String)responseAuthentication.getPrincipal();
        	TokenMessage tokenMessage = new TokenMessage(token);
            String tokenJsonResponse = new ObjectMapper().writeValueAsString(tokenMessage);
            httpResponse.addHeader("Content-Type", "application/json");
            httpResponse.getWriter().print(tokenJsonResponse);
            return;
        }
    
        chain.doFilter(httpRequest, httpResponse);//allow request to reach target controller if request json contained proper token
	}
	
	//method to send fail resposne
	private void fail(HttpServletResponse httpResponse, int sc) throws IOException{
		SecurityContextHolder.clearContext();
        httpResponse.sendError(sc);
        return;
	}
	
	//private class to allow multiple request body read
	private class MultipleReadHttpRequest extends HttpServletRequestWrapper {
	    private byte [] body; //cached request body

	    public MultipleReadHttpRequest(HttpServletRequest request) throws IOException {
	    	super(request);
	    	ByteArrayOutputStream cachedContent = new ByteArrayOutputStream();
	    	IOUtils.copy(request.getInputStream(), cachedContent);
	    	body = cachedContent.toByteArray();
	    	
	        // Read the request body and populate the cachedContent
	    }

	    @Override
	    public ServletInputStream getInputStream() throws IOException {
	    	final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
	    	   return new ServletInputStream() {
	    	       public int read() throws IOException {
	    	           return byteArrayInputStream.read();
	    	       }
	    	   };
	    }

	    @Override
	    public BufferedReader getReader() throws IOException {
	    	return new BufferedReader(new InputStreamReader(this.getInputStream()));
	    }
	}
	
}
