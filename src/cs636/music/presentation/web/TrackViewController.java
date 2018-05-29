package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Product;
import cs636.music.service.CatalogService;

public class TrackViewController implements Controller{
	private CatalogService catalogService;
	private Product productDetails;
	
	public TrackViewController(CatalogService catalogServiceAPI){
		this.catalogService = catalogServiceAPI;
	}
	@Override
	public String handleRequest(HttpServletRequest request, 
			HttpServletResponse response)
	throws IOException, ServletException{
		try{/*  not needed as we got the track out before.
			Long productId =Long.parseLong(request.getParameter("id"));
			productDetails = catalogService.getProduct(productId);
			HttpSession session = request.getSession();
			*/
			String cartStatus = (String)request.getAttribute("cartStatus");
			cartStatus = "done";
		}catch(Exception e){
			System.out.println("err from TrackViewController.hanlerRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from TrackViewController.hanlerRequest");
		}
		return "success";
	}

}
