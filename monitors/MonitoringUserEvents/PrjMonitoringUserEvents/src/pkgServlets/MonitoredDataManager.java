package pkgServlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.json.simple.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;
import eu.supersede.integration.api.analysis.proxies.DataProviderProxy;
import java.io.BufferedReader;


/**
 * Servlet implementation class MonitoredDataManager
 */
@WebServlet("/MonitoredDataManager")
public class MonitoredDataManager extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonitoredDataManager() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			generateResponse(request, response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			generateResponse(request, response);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException
	{	
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObjAux = new JSONObject();
		
		jsonObjAux.put("idOutput", request.getParameter("OutputID"));
		jsonObjAux.put("confId", request.getParameter("ConfigurationID"));
		jsonObjAux.put("idUser", request.getParameter("UserID"));
		
		BufferedReader reader = request.getReader();
	    String sCurrentURLPage = "";
	    while ((sCurrentURLPage = reader.readLine()) != null)
	    {
	    	jsonObjAux.put("currentPage", sCurrentURLPage);
	    }
		
		jsonObjAux.put("elementType", request.getParameter("Element"));
		jsonObjAux.put("idElement", request.getParameter("idElement"));
		jsonObjAux.put("eventType", request.getParameter("EventType"));
		jsonObjAux.put("elementText", request.getParameter("Text"));
		jsonObjAux.put("elementValue", request.getParameter("Value"));
		jsonObjAux.put("timeStamp", request.getParameter("Timestamp"));
		
		jsonObj.put("HTMLEventsMonitoredData", jsonObjAux);
	    
	    System.out.println(jsonObj);
	    
	    //Fragment for the IF
	    DataProviderProxy proxy = new DataProviderProxy();
	    proxy.ingestData(jsonObj, "snf");
		
	    //If there is response - specify the code for responding to the client
	    response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("<test>"+ "sucessfull" +"</test>");
	}
	
	/**** with json simle
	public void generateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		JSONObject jsonObj = new JSONObject();
		JSONObject jsonObjAux = new JSONObject();
		
		jsonObjAux.put("idOutput", request.getParameter("OutputID"));
		jsonObjAux.put("confId", request.getParameter("ConfigurationID"));
		jsonObjAux.put("idUser", request.getParameter("UserID"));
		
		BufferedReader reader = request.getReader();
	    String sCurrentURLPage = "";
	    while ((sCurrentURLPage = reader.readLine()) != null)
	    {
	    	jsonObjAux.put("currentPage", sCurrentURLPage);
	    }
		
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
	*/
}
