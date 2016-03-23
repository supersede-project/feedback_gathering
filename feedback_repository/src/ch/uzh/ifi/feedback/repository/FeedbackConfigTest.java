package ch.uzh.ifi.feedback.repository;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FeedbackServlet
 */
@WebServlet("/FeedbackConfigTest")
public class FeedbackConfigTest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedbackConfigTest() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String json = "[{\"title\": \"Title\", \"text\": \"Text\"}]";
		
		String config = "[{\"type\": \"textMechanism\", \"parameters\":[{\"key\": \"title\",\"value\": \"textTitle\",\"defaultValue\": \"defaultTextTitle\"},{\"key\": \"hint\",\"value\": \"textHint\",\"defaultValue\": \"defaultTextHint\"},{\"key\": \"maxLength\",\"value\": 200,\"defaultValue\": 140}]},{\"type\": \"rating\",\"parameters\":[{\"key\": \"title\",\"value\": \"ratingTitle\",\"defaultValue\": \"defaultRatingTitle\"},{\"key\": \"ratingIcon\",\"value\": null,\"defaultValue\": \"star\"},{\"key\": \"maxRating\",\"value\": 5},{\"key\": \"defaultRating\",\"value\": 2,\"defaultValue\": 0,\"editableByUser\": true}]},{\"type\": \"screenshot\",\"parameters\":[{\"key\": \"title\",\"value\": null,\"defaultValue\": \"defaultScreenshotTitle\"},{\"key\": \"defaultPicture\",\"value\": \"lastScreenshot\",\"defaultValue\": \"noImage\",\"editableByUser\": true}]}]";
		
		response.setContentType("application/json");            
        response.setCharacterEncoding("UTF-8");
        
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        
        response.getWriter().write(config);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
