package cs636.music.presentation.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Product;
import cs636.music.service.CatalogService;
import cs636.music.service.ServiceException;

/** 
 * CatalogViewController: it is used to build catalog.
	-handleRequest: gets data from catalogservice and build the Catalog page via jsp.
 * **/

public class CatalogViewController implements Controller{
	private CatalogService catalogService;
	
	public CatalogViewController(CatalogService catalogServiceApi){
		this.catalogService = catalogServiceApi;
	}
	
	@Override
	public String handleRequest(HttpServletRequest request,HttpServletResponse response)
	throws IOException, ServletException{

		Set<Product> productlist;
		try{
			productlist = catalogService.getProductList();
			}catch(ServiceException e){
				System.out.println("err from the service layer: catalogService.getProductList()");
				System.out.println(MusicSystemConfig.exceptionReport(e));
				throw new ServletException("err from the service layer: catalogViewController.handleRequest") ;
			}
		request.setAttribute("productSet", productlist);
		return "success";
	}

}
