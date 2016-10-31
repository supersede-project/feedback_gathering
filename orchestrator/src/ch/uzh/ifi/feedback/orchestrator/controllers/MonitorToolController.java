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
package ch.uzh.ifi.feedback.orchestrator.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorToolService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorToolValidator;

@RequestScoped
@Controller(MonitorToolController.class)
public class MonitorToolController extends RestController<MonitorTool> {

	public MonitorToolController(MonitorToolService dbService, 
			MonitorToolValidator validator,
			HttpServletRequest request, HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@POST
	@Path("/monitors/{id-type-of-monitor}")
	public MonitorTool InsertMonitorTool(@PathParam("id-type-of-monitor") String id, 
			MonitorTool tool) throws Exception {
		tool.setMonitorTypeName(id);
		return super.Insert(tool);
	}
	
	@GET
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}")
	public MonitorTool GetMonitorTool(@PathParam("id-type-of-monitor") String type,
			@PathParam("id-monitoring-tool") String tool) throws Exception {
		return super.GetById(tool.hashCode());
	}
	
	@DELETE
	@Path("/monitors/{id-type-of-monitor}/{id-monitoring-tool}")
	public void DeleteTool(@PathParam("id-type-of-monitor") String type,
			@PathParam("id-monitoring-tool") String tool) throws Exception {
		super.Delete(tool.hashCode());
	}
	

}
