package ch.uzh.ifi.feedback.repository.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import ch.uzh.ifi.feedback.library.rest.service.ServiceBase;
import ch.uzh.ifi.feedback.library.transaction.DatabaseConfiguration;
import ch.uzh.ifi.feedback.library.transaction.IDatabaseConfiguration;
import ch.uzh.ifi.feedback.repository.model.StatusOption;
import javassist.NotFoundException;
import static java.util.Arrays.asList;

@Singleton
public class StatusOptionService extends ServiceBase<StatusOption> {

	@Inject
	public StatusOptionService(
			StatusOptionResultParser resultParser,
			IDatabaseConfiguration config) 
	{
		super(resultParser, StatusOption.class, "status_options", config.getDatabase());
	}
	
	@Override
	public int Insert(Connection con, StatusOption option) throws SQLException, NotFoundException, UnsupportedOperationException {

		return super.Insert(con, option);
	}
	
	@Override
	public void Update(Connection con, StatusOption option)
			throws SQLException, NotFoundException, UnsupportedOperationException {
	
		StatusOption oldOption = GetById(option.getId());
		
		//switch higher options down if no option is left on the place of the updated option
		List<StatusOption> oldOptions = GetWhere(asList(oldOption.getOrder(), oldOption.isUserSpecific()), "`order` = ?", "user_specific = ?");
		if(oldOptions.size() == 1)
		{
			List<StatusOption> options = GetWhere(asList(oldOption.getOrder(), oldOption.isUserSpecific()), "`order` > ?", "user_specific = ?");
			for(StatusOption other : options)
			{
				if(other.getOrder() == option.getOrder())
					option.setOrder(option.getOrder() - 1);

				other.setOrder(other.getOrder() - 1);
				super.Update(con, other);
			}
		}
		
		super.Update(con, option);
	}

	@Override
	public void Delete(Connection con, int id) throws SQLException, NotFoundException {
		StatusOption oldOption = GetById(id);

		//shift options with higher order when no option with the same order exists
		List<StatusOption> oldOptions = GetWhere(asList(oldOption.getOrder(), oldOption.isUserSpecific()), "`order` = ?", "user_specific = ?");
		if(oldOptions.size() == 1)
		{
			List<StatusOption> higherOptions = GetWhere(asList(oldOption.getOrder(), oldOption.isUserSpecific()), "`order` > ?", "user_specific = ?");
			for(StatusOption op : higherOptions)
			{
				op.setOrder(op.getOrder() - 1);
				super.Update(con, op);
			}
		}

		super.Delete(con, id);
	}

}
