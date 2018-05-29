package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.User;
import cs636.music.service.SalesService;
import cs636.music.service.data.UserData;


/**
 * UserViewController:
	-handleRequest: gets user detail if in the cookie or check whether cookie there 
	but user not the in the database(whens if we reload the database but didn't close the browser)
 * 
 * **/
public class UserViewController implements Controller {
	private SalesService salesService;
	public UserViewController(SalesService saleServiceAPI){
		this.salesService = saleServiceAPI;
	}
	
	@Override
	public String handleRequest(HttpServletRequest request,HttpServletResponse response)
	throws IOException, ServletException{
		try{
			User user = (User) request.getAttribute("user");
			if(user.getEmailAddress()!= null){
			UserData userData =	salesService.getUserInfoByEmail(user.getEmailAddress());
			if(userData == null){
				return "not in database";
			}
			HttpSession session = request.getSession();
			session.setAttribute("userData", userData);
			}
		}catch(Exception e){
			System.out.println("err from UserViewController.handleRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from UserViewController.handleRequest");
		}
		return "success";
	}

}
