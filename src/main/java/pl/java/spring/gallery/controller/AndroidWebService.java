package pl.java.spring.gallery.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.java.spring.gallery.service.PhotoService;
import pl.java.spring.gallery.service.ServiceException;
import pl.java.spring.gallery.service.TokenService;
import pl.java.spring.gallery.service.UserService;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


/**
 * Controller class used to provide webservice communication with android GalleryAp application.
 * @author Wojciech Bêdkowski
 *
 */
@RestController
@RequestMapping(value="/webservice")
public class AndroidWebService {
	
	@Autowired
	private UserService usersService;
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private TokenService tokenService;
	
	/**
	 * New user insert by webservice
	 * @param request Request string value
	 * @return
	 */
	@RequestMapping(value="/insert", method = RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity newUser(@RequestBody String request){
		try{
			ObjectMapper mapper = new ObjectMapper();
			//map request string to json with requested username and password
			NewUserRequest newUserRequest = mapper.readValue(request, NewUserRequest.class);
			usersService.insertUser(newUserRequest.getUsername(), newUserRequest.getPassword());
			return ResponseEntity.ok().body(null);//send ok if user created
			//send bad request if bad input provided
		} catch(ServiceException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	/**
	 * New picture add by webservice
	 * @param request Request string
	 * @return
	 */
	@RequestMapping(value="/newpicture", method = RequestMethod.POST, consumes={MediaType.APPLICATION_JSON_UTF8_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	public ResponseEntity newPicture(@RequestBody String request){
		try{
			ObjectMapper mapper = new ObjectMapper();
			//map json with token and picture
			NewPictureRequest newPictureRequest = mapper.readValue(request, NewPictureRequest.class);
			int userId = tokenService.getId(newPictureRequest.getToken());//get user id by token
			photoService.instertPhoto(newPictureRequest.getPicture(), userId);
			return ResponseEntity.ok().body(null);
			//send error if bad input
		} catch(ServiceException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (JsonParseException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
}
