package monitoring.services;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import monitoring.params.MonitoringParams;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;

@Path("SocialNetworkMonitoring")
public class SocialNetworkService extends ServiceWrapper {
	
	MonitoringParams params;
	TwitterStream stream;

	@Override
	@POST
	@Path("/configuration")
	public String addConfiguration(@QueryParam("configurationJson") String jsonConf) {
		
		int id = 1;
		
		try {
			
			params = parseConfigurationParams(jsonConf);
			
			ConfigurationBuilder cb = new ConfigurationBuilder();
			cb.setDebugEnabled(true)
			  .setOAuthConsumerKey("bp7l14kRNiAL4ulI8kVxAwOji")
			  .setOAuthConsumerSecret("OvsQl9Q4BgCmmSnhwxZSSoan49AICThiSBIUiBF98Xpjp5OKPw")
			  .setOAuthAccessToken("124831353-OejsO8VStzjMvx0oT1qqydD2DcdiIcwpFOa0FIv8")
			  .setOAuthAccessTokenSecret("HAhXZLgAqEq6OWmScQEndmCMLQwfeNsMkNMdq5BKCpvFE");
			
			Configuration conf = cb.build();
			
			stream = new TwitterStreamFactory(conf).getInstance();

			stream.onStatus(new Consumer<Status>() {
				@Override
				public void accept(Status arg0) {
					User u = arg0.getUser();
					boolean userCheck = false;
					for (int i = 0; i < params.getAccounts().size(); ++i) {
						System.out.println(u.getScreenName() + "//" + params.getAccounts().get(i));
						if (u.getScreenName().equals(params.getAccounts().get(i))) userCheck = true;
					}
					if (userCheck) System.out.println(u.getName()+ "(@"+u.getScreenName()+"): "+arg0.getText());		
				}
			});
						
			
			FilterQuery filterQuery = new FilterQuery();
			filterQuery.track(params.getKeywordExpression());
			System.out.println(filterQuery);
			
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
			    public void run() {
			    	System.out.println("Ending...");
			    }
			}, 0, Integer.parseInt(params.getTimeSlot())*1000);
			
			//filterQuery.follow(user.getId());
			stream.filter(params.getKeywordExpression());

		} catch (Exception e1) {
			e1.printStackTrace();
			return getResponse(id, "error").toString();
		}

		return getResponse(id, "success").toString();
		
	}
	
}
