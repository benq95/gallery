package pl.java.spring.gallery.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import pl.java.spring.gallery.dao.DatabaseErrorCode;
import pl.java.spring.gallery.dao.DatabaseException;
import pl.java.spring.gallery.dao.User;
import pl.java.spring.gallery.dao.UsersDAO;

/**
 * Service class for user operations
 * @author Wojciech Bêdkowski
 *
 */
@Service
public class UserService {
	
	@Autowired
	private UsersDAO usersDAO;
	
	private void handleException(DatabaseException e) throws ServiceException{
		if(e.getErrorCode()==DatabaseErrorCode.InternalDbError){
			throw new ServiceException("Internal database error", ServiceErrorCode.InternalError);
		}else{
			throw new ServiceException(e.getMessage(), ServiceErrorCode.InvalidInput);
		}
	}
	
	/**
	 * Calculate md5 sum
	 * @param text input string
	 * @return md5hash
	 */
	private String md5(String text) throws ServiceException{
		String hash;
		if((text==null)||(text.length()==0)){
			throw new ServiceException("Null input", ServiceErrorCode.NullReference);
		}
		PasswordEncoder encoder = new Md5PasswordEncoder();
	    hash = encoder.encodePassword("pass", null);
		return hash;
	}
	
	
	/**
	 * Method to check user's credentials.
	 * @param name User name
	 * @param password Password
	 * @throws ServiceException - if some problem occured in service.
	 * @return True if input is correct, false when invalid password or user doesn't exist. 
	 */
	@Deprecated
	public boolean checkUser(String name, String password) throws ServiceException{
		password = md5(password);
		User user = null;
		try{
			user = usersDAO.findUser(name, password);
		}catch(DatabaseException e){
			handleException(e);
		}
		if(user==null){
			return false;
		}
		return true;
	}
	
	/**
	 * Get user id.
	 * @param name Username
	 * @param password Password
	 * @return User id.
	 * @throws ServiceException - when no such user.
	 */
	public int getUserId(String name, String password) throws ServiceException{
		password = md5(password);
		User user = null;
		try{
			user = usersDAO.findUser(name, password);
		}catch(DatabaseException e){
			handleException(e);
		}
		if(user==null){
			throw new ServiceException("Invalid credentials", ServiceErrorCode.InvalidInput);
		}
		return user.getUserId();
	}
	
	/**
	 * Insert new user to database
	 * @param name User name
	 * @param password Password
	 * @throws ServiceException - When some error occurs.
	 */
	public void insertUser(String name, String password) throws ServiceException{
		try{
			usersDAO.insertUser(name, md5(password));
		}catch(DatabaseException e){
			handleException(e);
		}
	}
	
}
