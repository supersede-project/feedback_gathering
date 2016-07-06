package monitoring.tools;

import org.junit.Test;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;

public class AndroidMarketAPITest {

	private final String authToken = "";
	private final String appPackage = "dev.blunch.blunch";
	
	@Test
	public void test() {

        MarketSession session = new MarketSession();
        session.login("test.student.motger@gmail.com", "Supersede2016");
        AppsRequest appsRequest = AppsRequest.newBuilder() 
        		.setQuery(appPackage)
        		.setStartIndex(0).setEntriesCount(10)
        		.setWithExtendedInfo(true)
        		.build(); 
        
        session.append(appsRequest, new Callback<AppsResponse>() {
        	public void onResult(ResponseContext context, AppsResponse response) {
        		for(int i=0;i<response.getEntriesCount();i++) { 
        			System.out.println(response.getApp(i).getTitle()); 
        		}
    		}
    	}); 
        
        session.flush(); 
	}
}
