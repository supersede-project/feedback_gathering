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
package monitoring.model;

import java.util.List;

import monitoring.model.MonitoringParams;

public class DiskMonitoringParams extends MonitoringParams {
	
	private String user;
	private String host;
	private List<Instruction> instructions;

	public String getUser() {
		return this.user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}

	public String getHost() {
		return this.host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}

	public List<Instruction> getInstructions() {
		return this.instructions;
	}
	
	public void setInstructions(List<Instruction> instructions) {
		this.instructions = instructions;
	}
	
}
