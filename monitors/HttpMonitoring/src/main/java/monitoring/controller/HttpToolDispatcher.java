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
package monitoring.controller;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import monitoring.controller.ToolDispatcher;
import monitoring.model.HttpParserConfiguration;

@RequestMapping(value = "/")
@RestController
public class HttpToolDispatcher {
	
	private final static Logger logger = Logger.getLogger(HttpToolDispatcher.class);
	private final static String result = "HttpMonitoringConfProfResult";
	
	private ToolDispatcher toolDispatcher = 
			new ToolDispatcher(new HttpParserConfiguration(), result);
	
	@RequestMapping(value = "/configuration", method = RequestMethod.POST, consumes="application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String addConfiguration(@RequestBody String json) {
			return toolDispatcher.addConfiguration(json);
	}
	
	@RequestMapping(value = "/configuration", method = RequestMethod.POST, consumes="multipart/form-data")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String addConfiguration(@RequestParam(value="json") String json, 
			@RequestParam(value="file") MultipartFile file) {
			((HttpParserConfiguration) toolDispatcher.getParser()).setFile(file);
			return toolDispatcher.addConfiguration(json);
	}
	
	@RequestMapping(value = "/configuration/{id}", method = RequestMethod.PUT, consumes="application/json")
	public String updateConfiguration(@PathVariable("id") Integer id, 
			@RequestBody(required=false) String jsonConf) throws Exception {
			return toolDispatcher.updateConfiguration(id, jsonConf);
	}
	
	@RequestMapping(value = "/configuration/{id}", method = RequestMethod.PUT, consumes="multipart/form-data")
	public String updateConfiguration(@PathVariable("id") Integer id, @RequestParam(value="json", required=false) String json, 
			@RequestParam(value="file", required=false) MultipartFile file) throws Exception {
			((HttpParserConfiguration) toolDispatcher.getParser()).setFile(file);
			return toolDispatcher.updateConfiguration(id, json);
	}
	
	@RequestMapping(value = "/configuration/{id}", method = RequestMethod.DELETE)
	public String deleteConfiguration(@PathVariable("id") Integer id) {
		return toolDispatcher.deleteConfiguration(id);
	}

}
