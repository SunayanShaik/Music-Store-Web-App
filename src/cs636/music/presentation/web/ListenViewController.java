package cs636.music.presentation.web;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cs636.music.config.MusicSystemConfig;
import cs636.music.domain.Track;
import cs636.music.service.CatalogService;
import cs636.music.service.data.UserData;

/**
 * ListenViewController: get you the song you had selected.
	-handleRequest: it saves necessary Information about selected track then call SalesDispatcherServlet for user Validation 
	-listenRequest: updates the download table and gets you the selected song(buffering of the browser).
 * 
 * **/



public class ListenViewController implements Controller{
	private CatalogService catalogService;
	private static final String salesGettingUser = "/sales/userForCatalog";
	private static final String userRegisteration = "/WEB-INF/jsp/register.jsp";
	private static final String listenUrl = "/catalog/listen";
	
	
	public ListenViewController(CatalogService catalogServiceAPI){
		this.catalogService = catalogServiceAPI; 
	}

	@Override
public String handleRequest(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
			try{
				String trackName = null;
				HttpSession session = request.getSession();
				String trackNo = (String)request.getParameter("trackno");
				if(trackNo != null){
					Long trackId=Long.parseLong(trackNo);
					session.setAttribute("trackSelected",trackId);
					Set<Track> tracks = (Set<Track>)session.getAttribute("track");
					for(Track track: tracks){
						if(track.getId()== trackId){
							trackName = track.getSampleFilename();
							session.setAttribute("trackSampleFileName", trackName);
							session.setAttribute("trackSelectedProductCode",
									track.getProduct().getCode());
						}
						else
							continue;
					}
				}
				
		}catch(Exception e){
			System.out.println("err from ListenViewController.handleRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from ListenViewController.handleRequest");
		}
			return "success";
	}
	
	
	public String listenRequest(HttpServletRequest request,HttpServletResponse response)
			throws IOException, ServletException{
		Track track = new Track();
		try{
			HttpSession session = request.getSession();
			UserData userData =(UserData)session.getAttribute("userData");
			if(userData.getEmailAddress()!= null){
				track.setId(Long.parseLong(session.getAttribute("trackSelected").toString()));
				catalogService.addDownload(userData.getEmailAddress(),track);
				String url ="/sound/"+session.getAttribute("trackSelectedProductCode")+
						"/"+session.getAttribute("trackSampleFileName");
				return url;
				}
			else
				throw new Exception("user still not registered");
		}catch(Exception e){
			System.out.println("err from ListenViewController.listenRequest");
			System.out.println(MusicSystemConfig.exceptionReport(e));
			throw new ServletException("err from ListenViewController.listenRequest");
		}
	}
}
