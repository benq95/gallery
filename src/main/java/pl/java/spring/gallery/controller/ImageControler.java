package pl.java.spring.gallery.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.userdetails.User;

import pl.java.spring.gallery.service.PhotoService;
import pl.java.spring.gallery.service.ServiceException;
import pl.java.spring.gallery.service.UserService;

/**
 * Controller class for getting photos
 * @author Wojciech Bêdkowski
 *
 */
@Controller
public class ImageControler {
	@Autowired
	PhotoService photoService;
	
	@Autowired
	UserService userService;
	
	@Secured({"ROLE_USER"})
	@RequestMapping("/home")
	public String getPhotos(Model model){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		try{
			User user = (User)authentication.getPrincipal();
			int id = userService.getUserId(user.getUsername(), (String)authentication.getCredentials());
			List<String> list = photoService.getPhotos(id);
			model.addAttribute("photoList", list);//add list of photo paths to model
		}catch (ServiceException e){
			model.addAttribute("error", "Internal server error. Try again later.");//add error to model if some error occured
		}
		return "home";
	}
}
