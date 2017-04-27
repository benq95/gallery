package pl.java.spring.gallery.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.java.spring.gallery.dao.DatabaseErrorCode;
import pl.java.spring.gallery.dao.DatabaseException;
import pl.java.spring.gallery.dao.Photo;
import pl.java.spring.gallery.dao.PhotosDAO;

/**
 * Service class which provides photos operations
 * @author Wojciech Bêdkowski
 *
 */
@Service
public class PhotoService {
	@Autowired
	private PhotosDAO photosDAO;
	
	private final String path = "C:\\images\\";//path where photos are saved
	
	/**
	 * Get list of paths related to given user id
	 * @param userId User id
	 * @return List of photos paths
	 * @throws ServiceException - when invalid user id given.
	 */
	public List <String> getPhotos(int userId) throws ServiceException{
		try{
			List<Photo> photos = photosDAO.getPhotos(userId);
			if(photos==null){
				return null;
			}
			ArrayList <String> photosPath = new ArrayList<String>();
			for(Photo i : photos){
				photosPath.add(i.getPath());
			}
			return photosPath;
		}catch(DatabaseException e){
			throw new ServiceException(e.getMessage(), ServiceErrorCode.InvalidInput);
		}
	}
	
	/**
	 * Insert new photo to db and save on disk
	 * @param image image binary data
	 * @param userId photo's user's id
	 * @throws ServiceException - when invalid input or internal error
	 */
	public void instertPhoto(byte[] image, int userId) throws ServiceException{
		if(image==null){
			throw new ServiceException("Null image", ServiceErrorCode.NullReference);
		}
		try{
			Photo lastPhoto = photosDAO.getLastPhoto();//get last photo
			//count new photo id
			int newId;
			if(lastPhoto==null){
				newId = 1;
			}else{
				newId = lastPhoto.getPhotoId()+1;
			}
			//save photo
			FileOutputStream fos = new FileOutputStream(path + Integer.toString(newId) + ".jpeg");
			try {
			    fos.write(image);
			}
			finally {
			    fos.close();
			}
			//save path to db
			photosDAO.insertPhoto(userId, Integer.toString(newId));
		}catch(DatabaseException e){
			throw new ServiceException(e.getMessage(), ServiceErrorCode.InvalidInput);
		}catch(IOException e){
			throw new ServiceException("Write data error", ServiceErrorCode.InternalError);
		}
	}
}
