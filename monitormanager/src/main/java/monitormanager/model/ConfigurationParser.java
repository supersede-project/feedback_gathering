/*******************************************************************************
 * Copyright (c) 2016 Universitat Politécnica de Catalunya (UPC)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * 	Quim Motger (UPC) - main development
 * 	
 * Initially developed in the context of SUPERSEDE EU project
 * www.supersede.eu
 *******************************************************************************/
package monitormanager.model;

import eu.supersede.integration.api.monitoring.manager.types.AppStoreMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.GooglePlayMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.HttpMonitorConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.MonitorSpecificConfiguration;
import eu.supersede.integration.api.monitoring.manager.types.TwitterMonitorConfiguration;

public class ConfigurationParser {

	public TwitterMonitorConfiguration getTwitterConfiguration(TwitterMonitorManagerConfiguration conf) throws Exception {
		TwitterMonitorConfiguration newConf = new TwitterMonitorConfiguration();
		setMonitorConfigurationParams(newConf, conf);
		newConf.setAccounts(conf.getAccounts());
		newConf.setKeywordExpression(conf.getKeywordExpression());
		return newConf;
	}
	
	public GooglePlayMonitorConfiguration getGooglePlayConfiguration(GooglePlayMonitorManagerConfiguration conf) throws Exception {
		GooglePlayMonitorConfiguration newConf = new GooglePlayMonitorConfiguration();
		setMonitorConfigurationParams(newConf, conf);
		newConf.setPackageName(conf.getPackageName());
		return newConf;
	}
	
	public AppStoreMonitorConfiguration getAppStoreConfiguration(AppStoreMonitorManagerConfiguration conf) throws Exception {
		AppStoreMonitorConfiguration newConf = new AppStoreMonitorConfiguration();
		setMonitorConfigurationParams(newConf, conf);
		newConf.setAppId(conf.getappId());
		return newConf;
	}
	
	public HttpMonitorConfiguration getHttpConfiguration(HttpMonitorManagerConfiguration conf) throws Exception {
		HttpMonitorConfiguration newConf = new HttpMonitorConfiguration();
		setMonitorConfigurationParams(newConf, conf);
		newConf.setUrl(conf.getUrl());
		return newConf;
	}
	
	private void setMonitorConfigurationParams(MonitorSpecificConfiguration newConfiguration, MonitorManagerSpecificConfiguration conf) throws Exception {
		newConfiguration.setId(conf.getId());
		newConfiguration.setKafkaEndpoint(conf.getKafkaEndpoint());
		newConfiguration.setKafkaTopic(conf.getKafkaTopic());
		newConfiguration.setTimeSlot(conf.getTimeSlot());
		newConfiguration.setToolName(conf.getToolName());
	}
	
}
