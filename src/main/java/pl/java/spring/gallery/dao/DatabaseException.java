package pl.java.spring.gallery.dao;

/**
 * Exception for database
 * @author Wojciech B�dkowski
 *
 */
public class DatabaseException extends Exception {
	
	public DatabaseErrorCode getErrorCode() {
		return errorCode;
	}

	private DatabaseErrorCode errorCode;
	
	public DatabaseException(String message, DatabaseErrorCode code){
		super(message);
		errorCode = code;
	}

}
