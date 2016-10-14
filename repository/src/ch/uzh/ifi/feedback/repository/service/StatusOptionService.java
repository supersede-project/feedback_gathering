package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

@Singleton
public class StatusOptionService extends ServiceBase<StatusOption> {

	@Inject
	public StatusOptionService(
			StatusOptionResultParser resultParser,
			DatabaseConfiguration config) 
	{
		super(resultParser, StatusOption.class, "status_options", config.getRepositoryDb());
	}
	
	@Override
	public int Insert(Connection con, StatusOption option) throws SQLException, NotFoundException, UnsupportedOperationException {
		
		//shift options with higher order
		List<StatusOption> options = GetWhere(asList(option.getOrder(), option.isUserSpecific()), "`order` >= ?", "user_specific = ?");
		for(StatusOption op : options)
		{
			op.setOrder(op.getOrder() + 1);
			super.Update(con, op);
		}
		
		return super.Insert(con, option);
	}
	
	@Override
	public void Update(Connection con, StatusOption option)
			throws SQLException, NotFoundException, UnsupportedOperationException {
	
		StatusOption oldStatus = GetById(option.getId());
		
		//Get option that is at the desired position and switch it to the old position of the updated option
		List<StatusOption> options = GetWhere(asList(option.getOrder(), option.isUserSpecific()), "`order` = ?", "user_specific = ?");
		if(options.size() > 0)
		{
			StatusOption other = options.get(0);
			other.setOrder(oldStatus.getOrder());
			super.Update(con, other);	
		}
		
		super.Update(con, option);
	}

}
