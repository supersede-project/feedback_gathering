package monitoring.tools;

import java.util.List;

import org.junit.Test;

import com.gc.android.market.api.MarketSession;
import com.gc.android.market.api.MarketSession.Callback;
import com.gc.android.market.api.model.Market.App;
import com.gc.android.market.api.model.Market.AppsRequest;
import com.gc.android.market.api.model.Market.AppsResponse;
import com.gc.android.market.api.model.Market.ResponseContext;

public class AndroidMarketAPITest {

	private final String authToken = "dead000beef";
	private final String appPackage = "dev.blunch.blunch";
	
	@Test
	public void test() {

		MarketSession session = new MarketSession();
        session.setAuthSubToken(authToken);

        String query = "pname:" + appPackage;
        AppsRequest appsRequest = AppsRequest.newBuilder()
                .setQuery(query)
                .setStartIndex(0).setEntriesCount(10)
                .setWithExtendedInfo(true)
                .build();

        session.append(appsRequest, new Callback<AppsResponse>() {
            @Override
            public void onResult(ResponseContext context, AppsResponse response) {
                List<App> appList = response.getAppList();
                
            }

        });

            session.flush();
		
	}
}
