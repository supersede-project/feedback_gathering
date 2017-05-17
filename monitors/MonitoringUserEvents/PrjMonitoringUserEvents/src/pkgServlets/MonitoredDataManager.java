package pkgServlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


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
	private static DataProviderProxy proxy;
       
//	private DataProviderProxy proxy;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonitoredDataManager() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public static DataProviderProxy getProxy() {
    	
    	if (proxy == null) {
    		proxy = new DataProviderProxy();
    	}
    	
    	return proxy;
    }

//    public DataProviderProxy getProxy() {
//    	
//    	if (proxy == null) {
//    		proxy = new DataProviderProxy();
//    	}
//    	
//    	return proxy;
//    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	try{
    		response.setContentType("text/html");
    		
    		ServletContext context = getServletContext();
    		
    		//the configuration file is under WEB-INF folder
    		String filename = "/WEB-INF/ConfigurationFile.txt";
    		
    		InputStream inp = context.getResourceAsStream(filename);
    		
    		if (inp != null) {
    			InputStreamReader isr = new InputStreamReader(inp);
    			BufferedReader reader = new BufferedReader(isr);
    			PrintWriter out = response.getWriter();
    			String jsonConfig = "", line = "";
    			
    			while ((line = reader.readLine()) != null) {
    				jsonConfig += line;
    			}
    			
    			JSONObject jsonAux = new JSONObject(jsonConfig);    			
    			JSONObject json = jsonAux.getJSONObject("UserEventsConfProf");
    			            
    			out.println(json);    			
    			out.close();               
    			
    		}
    	}
    	catch(IOException e){
    		e.printStackTrace();
    	}	
    	catch(JSONException e){
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
			e.printStackTrace();
		}
	}
	
	public void generateResponse(HttpServletRequest request, HttpServletResponse response) throws IOException, JSONException
	{	
		//If there is response - specify the code for responding to the client
	    response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		response.getWriter().write("<test>"+ "sucessfull" +"</test>");
		
		BufferedReader reader = request.getReader();		
		String sMonitoredData = "", line = null;
		while ((line = reader.readLine()) != null){
			sMonitoredData += line;
	    }
				
		JSONObject jsonObjAux = new JSONObject(sMonitoredData);
		
		String kafkaTopic = jsonObjAux.getString("kafkaTopic");
		jsonObjAux.remove("kafkaTopic");
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("HTMLEventsMonitoredData", jsonObjAux);
	    
	    System.out.println(jsonObj);
	    
	    //Fragment for the IF	//Without singleton pattern
	    //DataProviderProxy proxy = new DataProviderProxy();
	    //proxy.ingestData(jsonObj, kafkaTopic); //"6d670f20-3fa4-4f8d-9f7f-3001c66d885a"
	    
	    //Fragment for the IF	//With singleton pattern
	    //DataProviderProxy proxy = getProxy();
	    //proxy.ingestData(jsonObj, kafkaTopic); //"6d670f20-3fa4-4f8d-9f7f-3001c66d885a"
	}
	
	
}
