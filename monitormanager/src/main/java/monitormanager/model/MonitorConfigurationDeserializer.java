package monitormanager.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class MonitorConfigurationDeserializer extends StdDeserializer<MonitorManagerSpecificConfiguration> {

	public MonitorConfigurationDeserializer() {
		this(null);
	}
	
	protected MonitorConfigurationDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public MonitorManagerSpecificConfiguration deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = p.getCodec().readTree(p);
		JsonNode conf = null;
		if (node.get("SocialNetworks") != null) {
			conf = node.get("SocialNetworks");
			TwitterMonitorManagerConfiguration c = new TwitterMonitorManagerConfiguration();
			List<String> accounts = new ArrayList<>();
			if (conf.get("accounts") != null) {
				for (JsonNode obj : conf.get("accounts")) {
					accounts.add(obj.asText());
				}
			}
			setData(c,conf);
			c.setAccounts(accounts);
			c.setKeywordExpression(conf.get("keywordExpression").asText());
			return c;
		} else if (node.get("AppStore") != null) {
			conf = node.get("AppStore");
			AppStoreMonitorManagerConfiguration c = new AppStoreMonitorManagerConfiguration();
			setData(c,conf);
			c.setAppId(conf.get("appId").asText());
			return c;
		} else if (node.get("GooglePlay") != null) {
			conf = node.get("GooglePlay");
			GooglePlayMonitorManagerConfiguration c = new GooglePlayMonitorManagerConfiguration();
			setData(c,conf);
			c.setPackageName(conf.get("packageName").asText());
			return c;
		} else if (node.get("Http") != null) {
			conf = node.get("Http");
			HttpMonitorManagerConfiguration c = new HttpMonitorManagerConfiguration();
			setData(c,conf);
			c.setUrl(conf.get("url").asText());
			return c;
		} else throw new IOException();
	}

	private void setData(MonitorManagerSpecificConfiguration c, JsonNode conf) throws MalformedURLException {
		c.setKafkaEndpoint(new URL(conf.get("kafkaEndpoint").asText()));
		c.setKafkaTopic(conf.get("kafkaTopic").asText());
		c.setTimeSlot(Integer.valueOf(conf.get("timeSlot").asText()));
		c.setToolName(conf.get("toolName").asText());
	}

}
