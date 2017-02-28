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
 *  Jordi Marco (UPC) - make the class interface to reuse KafkaCommunication
 * 	
 * Initially developed in the context of SUPERSEDE EU project
 * www.supersede.eu
 *******************************************************************************/
package monitoring.model;

import org.json.JSONObject;

public interface MonitoringData {

	public JSONObject toJsonObject();
	
}