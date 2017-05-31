package pl.java.spring.gallery.service;

import pl.java.spring.gallery.dao.DatabaseErrorCode;

/**
 * Service exception class
 * @author Wojciech Bêdkowski
 *
 */
public class ServiceException extends Exception {
	public ServiceErrorCode getErrorCode() {
		return errorCode;
	}

	private ServiceErrorCode errorCode;
	
	public ServiceException(String message, ServiceErrorCode code){
		super(message);
		errorCode = code;
	}
}
