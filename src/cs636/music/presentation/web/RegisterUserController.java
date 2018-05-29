package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.User;
import cs636.music.service.SalesService;
import cs636.music.service.data.UserData;


/**
 * RegisterUserController: Validates User 
	-CheckUser: checks three condition user could be in the session, in the cookie(1. in the cookie and also in the databse 2.in the cookie but not in database) or totally new user.
	-handleRequest: updates the user in the database.
 * 
 * */
public class RegisterUserController implements Controller{
	private SalesService salesService;
	private static String downloadUrl = null;
	private static final String salesGettingUser = "/sales/userForCatalog";
	private static final String userRegisteration = "/WEB-INF/jsp/register.jsp";
	public RegisterUserController(SalesService salesServiceAPI){
		this.salesService = salesServiceAPI;
	}
	@Override
	public String handleRequest(HttpServletRequest request,HttpServletResponse response)
	throws IOException, ServletException{
		try{
			String firstName = null;
			String lastName = null;
			String email = null;
			UserData userData = null;
			HttpSession session = request.getSession();
			firstName = request.getParameter("firstName");
			lastName = request.getParameter("lastName");
			email = request.getParameter("email");
			if(firstName!= null && lastName != null && email!= null){
				/* in case user has registered before hence is present in databse
				 * but the cookie and session has expired then first checking the user 
				 * in the database before registering*/
				
				userData = salesService.getUserInfoByEmail(email);
				if(userData == null){
					salesService.registerUser(firstName, lastName, email);
					userData = salesService.getUserInfoByEmail(email);
					session.setAttribute("userData", userData);
					Cookie cookie = new Cookie("userEmailId",email);
					cookie.setMaxAge(-1);
					cookie.setPath("/");
					response.addCookie(cookie);
					downloadUrl = "success";
				}
				else{
					session.setAttribute("userData", userData);
					Cookie cookie = new Cookie("userEmailId",email);
					cookie.setMaxAge(-1);
					cookie.setPath("/");
					response.addCookie(cookie);
					downloadUrl = "success";
			}
				}
			/*else{
				mostly redundant because of html 5 validation
				String message = "Please enter all the fields";
				request.setAttribute("message", message);
				downloadUrl="/WEB-INF/jsp/register.jsp";
			}*/
		}catch(Exception e){
			System.out.println("err from RegisterUserController.handleRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from RegisterUserController.handleRequest");
		}
		return downloadUrl;
	}
	
	
	public String CheckUser(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		try{
			String userEmailId = "userEmailId" ;
			Cookie userCookie = null;
			HttpSession session = request.getSession();
			UserData userData =(UserData)session.getAttribute("userData");
			if(userData == null){
				Cookie[] cookies = request.getCookies();
				for(Cookie cookie: cookies){
					if(userEmailId.equals(cookie.getName())){
						User user = new User();
						userCookie = cookie;
						user.setEmailAddress(cookie.getValue());
						request.setAttribute("user", user);
						break;
				}
			}
				if(userCookie != null){
					return salesGettingUser;
				}
				if(userCookie == null){
					
					return userRegisteration;
				}
		}
	}catch(Exception e){
			System.out.println("err from RegisterUserController.CheckUser");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from RegisterUserController.CheckUser");			
		}
		return "success";
	}
}
