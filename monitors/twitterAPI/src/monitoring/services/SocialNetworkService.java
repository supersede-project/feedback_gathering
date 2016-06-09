package monitoring.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import monitoring.params.MonitoringData;
import monitoring.params.MonitoringParams;
import twitter4j.ConnectionLifeCycleListener;
import twitter4j.FilterQuery;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.util.function.Consumer;

@Path("SocialNetworkMonitoring")
public class SocialNetworkService extends ServiceWrapper {
	
	//The streaming instance to call the Stream Twitter API
	TwitterStream stream;
	
	//A list with the tweets info
	List<Status> tweetInfo;
	//Indicates if connection has been already initialized
	boolean firstConnection;

	@Override
	@POST
	@Path("/configuration")
	public String addConfiguration(@QueryParam("configurationJson") String jsonConf) {
		
		firstConnection = true;
		tweetInfo = new ArrayList<>();
		
		try {
			
			parseConfigurationParams(jsonConf);
			initKafka();
			
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
					if (params.getAccounts() != null && !params.getAccounts().isEmpty()) {
						if (params.getAccounts().contains(arg0.getUser().getScreenName())) { 
							tweetInfo.add(arg0);
						}
					}
					else tweetInfo.add(arg0);
				}
			});
			
			stream.addConnectionLifeCycleListener(new ConnectionLifeCycleListener() {

				@Override
				public void onCleanUp() {
					
				}

				@Override
				public void onConnect() {
					Timer timer = new Timer();
					timer.schedule(new TimerTask() {
					    public void run() {
					    	if (firstConnection) {
					    		firstConnection = false;
					    		System.out.println("First connection stablished");
					    	} else {
					    		generateData((new Timestamp((new Date()).getTime()).toString()));
					    	}
					    }

					}, 0, Integer.parseInt(params.getTimeSlot())*1000);
				}

				@Override
				public void onDisconnect() {
					System.out.println("Connection closed");
				}
				
			});
						
			
			FilterQuery filterQuery = new FilterQuery();
			if (params.getKeywordExpression() != null) {
				filterQuery.track(generateKeywordExp(params.getKeywordExpression()));
			}
			
			System.out.println(filterQuery);
			
			//filterQuery.follow(user.getId());
			stream.filter(filterQuery);

		} catch (Exception e1) {
			e1.printStackTrace();
			return getResponse("error").toString();
		}

		return getResponse("success").toString();
		
	}

	private String[] generateKeywordExp(String keywordExpression) {
		
		String[] blocks = keywordExpression.split(" AND ");
		for (int i = 0; i < blocks.length; ++i) {
			blocks[i] = blocks[i].replace("(", "");
			blocks[i] = blocks[i].replace(")", "");
		}
		
		List<List<String>> cnfCombinations = new ArrayList<>();
		
		for (int i = 0; i < blocks.length; ++i) {
			cnfCombinations.add(getKeyElements(blocks[i]));
		}
		
		List<List<String>> dnfCombinations = new ArrayList<>();
		
		for (int i = 0; i < cnfCombinations.get(0).size(); ++i) {
			dnfCombinations.addAll(getDnfCombination(cnfCombinations, 0, i));
		}
		
		String[] keywordDNFExpression = new String[dnfCombinations.size()];
		
		for (int i = 0; i < dnfCombinations.size(); ++i) {
			String keyword = "";
			for (String s : dnfCombinations.get(i)) {
				keyword += s + " ";
			}
			keywordDNFExpression[i] = keyword;
		}
		
		return keywordDNFExpression;
		
	}
	
	/**
	 * Backtracking method for getting all combinations in DNF
	 */
	private List<List<String>> getDnfCombination(List<List<String>> cnfCombination, int k, int j) {
		List<List<String>> dnf = new ArrayList<>();
		if (k == cnfCombination.size() -1 ) {
			List<String> l = new ArrayList<>();
			l.add(cnfCombination.get(k).get(j));
			dnf.add(l);
		}
		else {
			for (int i = 0; i < cnfCombination.get(k+1).size(); ++i) {
				List<List<String>> lists = getDnfCombination(cnfCombination, k + 1, i);
				for (List<String> l : lists) l.add(cnfCombination.get(k).get(j));
				dnf.addAll(lists);
			}
		}
		return dnf;
	}
	
	private List<String> getKeyElements(String block) {
		String[] elements = block.split(" OR ");
		List<String> keyElements = new ArrayList<>();
		for (int i = 0; i < elements.length; ++i) {
			keyElements.add(elements[i]);
		}
		return keyElements;
	}

	private void generateData(String searchTimeStamp) {
		
		List<MonitoringData> data = new ArrayList<>();
		for (Status s : tweetInfo) {
			String id = String.valueOf(s.getId());
			String timeStamp = String.valueOf(s.getCreatedAt());
			String message = s.getText();
			String author = "@" + s.getUser().getScreenName();
			String link = "https://twitter.com/" + s.getUser().getName().replace(" ", "")+ "/status/" + s.getId();
			MonitoringData dataObj = new MonitoringData(id, timeStamp, message, author, link);
			data.add(dataObj);
		}
		tweetInfo = new ArrayList<>();
		getData(searchTimeStamp, data);
		
	}
	
}
