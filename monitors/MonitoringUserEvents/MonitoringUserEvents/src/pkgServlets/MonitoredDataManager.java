package pkgServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

/**
 * Servlet implementation class MonitoredDataManager
 */

@WebServlet("/MonitoredDataManager")
public class MonitoredDataManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MonitoredDataManager() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		generateResponse(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		generateResponse(request, response);
	}
	
	public void generateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObjAux = new JSONObject();
		
		jsonObjAux.put("idOutput", request.getParameter("OutputID"));
		jsonObjAux.put("confId", request.getParameter("ConfigurationID"));
		jsonObjAux.put("idUser", request.getParameter("UserID"));
		jsonObjAux.put("currentPage", request.getParameter("CurrentPage"));
		jsonObjAux.put("elementType", request.getParameter("Element"));
		jsonObjAux.put("idElement", request.getParameter("idElement"));
		jsonObjAux.put("eventType", request.getParameter("EventType"));
		jsonObjAux.put("elementText", request.getParameter("Text"));
		jsonObjAux.put("elementValue", request.getParameter("Value"));
		jsonObjAux.put("timeStamp", request.getParameter("Timestamp"));
		
		jsonObj.put("HTMLEventsMonitoredData", jsonObjAux);
	    
	    System.out.println(jsonObj);
		
	    //If there is response - specify the code for responding to the client 
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("<numeroCaracteres>"+ "" +"</numeroCaracteres>");
	}
}
