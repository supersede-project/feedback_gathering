package ch.uzh.ifi.feedback.repository.service;

import static java.util.Arrays.asList;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.authorization.ApiUser;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserPermission;
import ch.uzh.ifi.feedback.library.rest.authorization.ApiUserResultParser;
import ch.uzh.ifi.feedback.library.rest.authorization.IApiUserService;
import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import javassist.NotFoundException;

@Singleton
public class ApiUserService extends ServiceBase<ApiUser> implements IApiUserService {

	private ApiUserPermissionService permissionService;

	@Inject
	public ApiUserService(
			ApiUserResultParser resultParser, 
			IDatabaseConfiguration config,
			ApiUserPermissionService permissionService) 
	{
		super(resultParser, ApiUser.class, "api_users", config.getDatabase());
		this.permissionService = permissionService;
	}
	
	@Override
	public List<ApiUser> GetAll() throws SQLException {
		
		List<ApiUser> users = super.GetAll();
		for(ApiUser user : users)
		{
			user.getPermissions().addAll(permissionService.GetWhere(asList(user.getId()), "user_id = ?"));
		}
		
		return users;
	}
	
	@Override
	public List<ApiUser> GetWhere(List<Object> values, String... conditions) throws SQLException {
		List<ApiUser> users = super.GetWhere(values, conditions);
		for(ApiUser user : users)
		{
			user.getPermissions().addAll(permissionService.GetWhere(asList(user.getId()), "user_id = ?"));
		}
		
		return users;
	}
	
	@Override
	public ApiUser GetById(int id) throws SQLException, NotFoundException {
		ApiUser user = super.GetById(id);
		user.getPermissions().addAll(permissionService.GetWhere(asList(user.getId()), "user_id = ?"));
		
		return user;
	}
	
	@Override
	public int Insert(Connection con, ApiUser user)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		int userId = super.Insert(con, user);
		
		for(ApiUserPermission permission : user.getPermissions())
		{
			permission.setUserId(userId);
			permissionService.Insert(con, permission);
		}
		
		return userId;
	}
	
	@Override
	public void Update(Connection con, ApiUser user)
			throws SQLException, NotFoundException, UnsupportedOperationException {
		super.Update(con, user);
		
		for(ApiUserPermission permission : user.getPermissions())
		{
			permission.setUserId(user.getId());
			permissionService.Update(con, permission);
		}
	}
}
