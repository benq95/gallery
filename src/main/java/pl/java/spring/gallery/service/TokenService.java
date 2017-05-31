package pl.java.spring.gallery.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Service class which provides webservice client tokens management
 * @author Wojciech Bêdkowski
 *
 */
@Service
public class TokenService {
    private static final Cache restApiAuthTokenCache = CacheManager.getInstance().getCache("restApiAuthTokenCache");
    private static final int HALF_AN_HOUR_IN_MILLISECONDS = 30 * 60 * 1000;
    
    @Autowired
    private UserService userService;
    
    //Destroy tokens which lasted more than 30 min
    @Scheduled(fixedRate = HALF_AN_HOUR_IN_MILLISECONDS)
    public void evictExpiredTokens() {
        restApiAuthTokenCache.evictExpiredElements();
    }
    //generate new random token
    private String generateNewToken() {
        return UUID.randomUUID().toString();
    }
    /**
     * Store new token in cache
     * @param username username
     * @param password password
     * @return generated token
     * @throws ServiceException - when invalid user credentials or some internal error
     */
    public String store(String username, String password) throws ServiceException {
        try {
			int userId = userService.getUserId(username,password);//try to get user id
			String token = generateNewToken();
        	restApiAuthTokenCache.put(new Element(token, userId)); //store token with user id
        	return token;
		}  catch (ServiceException e) { //get exception if bad user credentials or some internal error
			throw e;
		}
    }

    public boolean contains(String token) {//check if token exists in cache
        return restApiAuthTokenCache.get(token) != null;
    }
    
    /**
     * Get user id related to token
     * @param token
     * @return user id
     * @throws ServiceException - when invalid token
     */
    public int getId(String token) throws ServiceException{
    	Element element = restApiAuthTokenCache.get(token);
    	if(element==null){
    		throw new ServiceException("Invalid token", ServiceErrorCode.InvalidInput);
    	}
    	return (Integer)element.getValue();
    }

}
