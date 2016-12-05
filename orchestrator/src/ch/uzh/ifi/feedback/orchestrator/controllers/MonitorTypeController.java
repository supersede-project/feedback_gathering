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

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;

import ch.uzh.ifi.feedback.library.rest.RestController;
import ch.uzh.ifi.feedback.library.rest.annotations.Controller;
import ch.uzh.ifi.feedback.library.rest.annotations.DELETE;
import ch.uzh.ifi.feedback.library.rest.annotations.GET;
import ch.uzh.ifi.feedback.library.rest.annotations.POST;
import ch.uzh.ifi.feedback.library.rest.annotations.Path;
import ch.uzh.ifi.feedback.library.rest.annotations.PathParam;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorConfiguration;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorTool;
import ch.uzh.ifi.feedback.orchestrator.model.MonitorType;
import ch.uzh.ifi.feedback.orchestrator.services.MonitorTypeService;
import ch.uzh.ifi.feedback.orchestrator.validation.MonitorTypeValidator;
import javassist.NotFoundException;

@RequestScoped
@Controller(MonitorType.class)
public class MonitorTypeController extends RestController<MonitorType>{

	@Inject
	public MonitorTypeController(MonitorTypeService dbService, MonitorTypeValidator validator, HttpServletRequest request,
			HttpServletResponse response) {
		super(dbService, validator, request, response);
	}
	
	@GET
	public List<MonitorType> GetAll() throws Exception {
		return super.GetAll();
	}
	
	@POST
	public MonitorType InsertMonitorType(MonitorType monitorType) throws Exception {
		return super.Insert(monitorType);
	}
	
	@GET
	@Path("/{id-type-of-monitor}")
	public MonitorType GetById(@PathParam("id-type-of-monitor") String id) throws Exception {
		List<MonitorType> monitorType = this.dbService.GetWhere(Arrays.asList(id), "name = ?");
		if(monitorType.isEmpty()) {
				throw new NotFoundException("There is no monitor type with this name");
		}
		return monitorType.get(0);
	}
	
	@DELETE
	@Path("/{id-type-of-monitor}")
	public void DeleteMonitorTool(@PathParam("id-type-of-monitor") String id) throws Exception {
		List<MonitorType> monitorType = this.dbService.GetWhere(Arrays.asList(id), "name = ?");
		if(monitorType.isEmpty()) {
				throw new NotFoundException("There is no monitor type with this name");
		}
		super.Delete(monitorType.get(0).getId());
	}
	
	
}
