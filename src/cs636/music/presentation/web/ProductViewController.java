package cs636.music.presentation.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Product;
import cs636.music.service.CatalogService;


/**
 * ProductViewController: gets you details of the product after you select it in the catalog page.
	-handleRequest
 * */
public class ProductViewController implements Controller {
 CatalogService catalogService ;
 
 public ProductViewController(CatalogService catalogserviceAPI){
	 catalogService = catalogserviceAPI;
 }
 
	@Override
public String handleRequest(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		try{
			Long productId = Long.parseLong(request.getParameter("id"));
			Product product = catalogService.getProduct(productId);
			HttpSession session = request.getSession();
			session.setAttribute("track",product.getTracks());
			request.setAttribute("product", product);
			String cartStatus = (String)session.getAttribute("cartStatus");
			if(cartStatus == null){
				cartStatus = "";
			}
			session.setAttribute("cartStatus", cartStatus);
		}catch(Exception e){
			System.out.println("err from productViewController.handleRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from productViewController.handleRequest");
		}
		return "success";
	}
 
 
}
