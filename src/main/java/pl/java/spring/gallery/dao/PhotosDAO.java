package pl.java.spring.gallery.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Dao for photos db insertion
 * @author Wojciech Bêdkowski
 *
 */
@Repository
public class PhotosDAO {
	
	@PersistenceContext
	EntityManager entityManager;
	
	/**
	 * Get photos paths from db by user id
	 * @param userId user id
	 * @return list of photos
	 * @throws DatabaseException - if invalid input or internal error
	 */
	@Transactional
	public List <Photo> getPhotos(int userId) throws DatabaseException {
		User user = entityManager.find(User.class, userId);//check if user exists
		if(user==null){
			throw new DatabaseException("No such user", DatabaseErrorCode.InvalidArgument);
		}
		return new ArrayList<Photo>(user.getPhotos());
	}
	
	/**
	 * Insert photo into db
	 * @param userId user id
	 * @param path path of photo to be inserted
	 * @throws DatabaseException - when some error occurs
	 */
	@Transactional
	public void insertPhoto(int userId, String path) throws DatabaseException{
		if((path==null)||(path.length()==0)){
			throw new DatabaseException("Null path", DatabaseErrorCode.NullArgument);
		}
		User user = entityManager.find(User.class, userId); //check if user exists
		if(user==null){
			throw new DatabaseException("Invalid user id", DatabaseErrorCode.InvalidArgument);
		}
		Date date = new Date();
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		Photo photo = new Photo(user,path,timestamp);
		entityManager.merge(photo);//insert new photo
	}
	
	/**
	 * Get last photo from db
	 * @return last photo entity object or null if empty db
	 */
	@Transactional
	public Photo getLastPhoto(){
		TypedQuery<Photo> query = entityManager.createQuery("from Photo p order by p.photoId desc)",Photo.class);
		Photo photo;
		try{
			List<Photo> results = query.getResultList();
			if((results==null)||(results.isEmpty())){
				photo = null;
			}else{
				photo = results.get(0);
			}
		} catch (NoResultException e){
			photo = null;
		}
		return photo;
	}
}
