package pl.java.spring.gallery.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller class for logging in
 * @author Wojciech Bêdkowski
 *
 */
@Controller
public class LoginController {
	
	// pattern '/' is a pattern for logging in
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpServletRequest request, Model model) {
		if(SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated() && !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) ){
			return "redirect:/home";//redirect if user already logged in
		}
		if(request.getParameter("error")!=null){
			String value = (String)request.getParameter("error");
			if(value.equals("1")){
				model.addAttribute("error", "Invalid username or password.");
			}else if(value.equals("2")){
				model.addAttribute("error", "Access denied, try to log in.");
			}
		}
		return "login";
	}
	
}
