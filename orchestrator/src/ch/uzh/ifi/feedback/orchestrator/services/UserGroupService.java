package ch.uzh.ifi.feedback.orchestrator.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;

import ch.uzh.ifi.feedback.library.rest.Service.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.rest.Service.DbResultParser;
import ch.uzh.ifi.feedback.orchestrator.model.User;
import ch.uzh.ifi.feedback.orchestrator.model.UserGroup;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

public class UserGroupService extends OrchestratorService<UserGroup>{

	private UserService userService;
	
	@Inject
	public UserGroupService(
			UserGroupResultParser resultParser, 
			DatabaseConfiguration config,
			UserService userService) 
	{
		super(resultParser, UserGroup.class, "user_groups", config.getOrchestratorDb());
		
		this.userService = userService;
	}
	
	@Override
	public List<UserGroup> GetAll() throws SQLException, NotFoundException {
		List<UserGroup> groups = super.GetAll();
		
		for(UserGroup group : groups)
		{
			group.setUsers(userService.GetWhere(asList(group.getId()), "user_groups_id = ?"));
		}
		
		return groups;
	}
	
	@Override
	public UserGroup GetById(int id) throws SQLException, NotFoundException {
		
		UserGroup group = super.GetById(id);
		group.setUsers(userService.GetWhere(asList(group.getId()), "user_groups_id = ?"));
		return group;
		
	}
	
	@Override
	public int Insert(Connection con, UserGroup group)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		int groupId = super.Insert(con, group);
		
		for(User user : group.getUsers())
		{
			user.setGroupId(groupId);
			userService.Insert(con, user);
		}
		
		return groupId;
	}
	
	@Override
	public void Update(Connection con, UserGroup group)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		super.Update(con, group);
		
		for(User user : group.getUsers())
		{
			userService.Update(con, user);
		}
	}
}
