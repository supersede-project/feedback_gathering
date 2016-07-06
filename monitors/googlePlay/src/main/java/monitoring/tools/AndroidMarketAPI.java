package monitoring.tools;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.ResponseContext;

import kafka.javaapi.producer.Producer;
import monitoring.params.MonitoringParams;
import monitoring.services.ToolInterface;

public class AndroidMarketAPI implements ToolInterface {
	
	private final String email = "test.student.motger@gmail.com";
	private final String password = "Supersede2016";

	@Override
	public void addConfiguration(MonitoringParams params, Producer<String, String> producer) throws Exception {
		
		MarketSession session = new MarketSession(); 
		session.login(email,password); 

		AppsRequest appsRequest = AppsRequest.newBuilder().
				setQuery(params.getPackageName()).setStartIndex(0).setEntriesCount(10).
				setWithExtendedInfo(true).build();

		session.append(appsRequest, new MarketSession.Callback() { 
			@Override
			public void onResult(ResponseContext arg0, Object arg1) {
				System.out.print(arg0.toString());
			}
		}); 
		
		session.flush();
		
	}

}
