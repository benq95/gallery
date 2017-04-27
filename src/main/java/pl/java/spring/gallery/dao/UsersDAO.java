package pl.java.spring.gallery.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DAO class for user db operations
 * @author Wojciech Bêdkowski
 *
 */
@Repository
public class UsersDAO {
	
	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Insert new user
	 * @param name username
	 * @param password hashed password
	 * @throws DatabaseException
	 */
	@Transactional
	public void insertUser(String name, String password) throws DatabaseException{
		if((name==null)||(password==null)){
			throw new DatabaseException("Null argument", DatabaseErrorCode.NullArgument);
		}
		if(findUser(name, password)!=null){
			throw new DatabaseException("User already exists", DatabaseErrorCode.InvalidArgument);
		}
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		User user = new User(name,password,timestamp);
		try{
			entityManager.merge(user);
		}catch(IllegalArgumentException e){
			throw new DatabaseException("Invalid input", DatabaseErrorCode.InvalidArgument);
		}
	}
	
	/**
	 * Find user by its credentials
	 * @param name username
	 * @param password hashed password
	 * @return User entity object or null if user does not exists.
	 * @throws DatabaseException - when some error occurs or invalid input 
	 */
	@Transactional
	public User findUser(String name, String password) throws DatabaseException{
		if((name==null)||(password==null)){
			throw new DatabaseException("Null argument", DatabaseErrorCode.NullArgument);
		}
		try{
			TypedQuery<User> query = entityManager.createQuery("from User where name = :username and pass = :password", User.class);
			query.setParameter("username", name);
			query.setParameter("password", password);
			List <User> users = query.getResultList();
			if((users == null)||(users.size()==0)){
				return null;
			}
			return users.get(0);
		}catch (IllegalArgumentException e) {
			throw new DatabaseException("Invalid input", DatabaseErrorCode.InvalidArgument);
		}
	}
}
