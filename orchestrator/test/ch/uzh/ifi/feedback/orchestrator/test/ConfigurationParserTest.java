package ch.uzh.ifi.feedback.orchestrator.test;

import static org.junit.Assert.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.uzh.ifi.feedback.orchestrator.ConfigurationParser;
import ch.uzh.ifi.feedback.orchestrator.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.FeedbackParameter;

public class ConfigurationParserTest {

	private ResultSet resultSet;
	private ConfigurationParser testee;
	private List<FeedbackMechanism> testMechanisms;
	
	@Before
	public void setUp() throws Exception {
		testMechanisms = GetTestMechanism();
		FeedbackMechanism m1 = testMechanisms.get(0);
		resultSet = Mockito.mock(ResultSet.class);
		Mockito.when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
		Mockito.when(resultSet.getString("mechanism_name")).thenReturn(m1.getType());
		Mockito.when(resultSet.getInt("active")).thenReturn(m1.isActive() ? 1 : 0);
		Mockito.when(resultSet.getInt("order")).thenReturn(m1.getOrder());
		Mockito.when(resultSet.getInt("can_be_activated")).thenReturn(m1.isCanBeActivated() ? 1 : 0);
		Mockito.when(resultSet.getString("key"))
		.thenReturn(m1.getParameters().get(0).getKey())
		.thenReturn(m1.getParameters().get(1).getKey());
		Mockito.when(resultSet.getString("value"))
		.thenReturn(String.valueOf(m1.getParameters().get(0).getValue()))
		.thenReturn(String.valueOf(m1.getParameters().get(1).getValue()));
		Mockito.when(resultSet.getString("default_value"))
		.thenReturn(String.valueOf(m1.getParameters().get(0).getDefaultValue()))
		.thenReturn(null);
		Mockito.when(resultSet.getString("editable_by_user"))
		.thenReturn(String.valueOf(m1.getParameters().get(0).getEditableByUser()))
		.thenReturn(null);
		Mockito.when(resultSet.getInt("editable_by_user")).thenReturn(0);
	}

	@Test
	public void testParser() throws SQLException {
		//Arrange
		testee = new ConfigurationParser();
		
		//Act
		testee.Parse(resultSet);
		
		//Assert
		List<FeedbackMechanism> resultList = testee.GetResult();
		FeedbackMechanism result = resultList.get(0);
		FeedbackMechanism toCompare = testMechanisms.get(0);
		assertEquals(result, toCompare);
	}
	
	private List<FeedbackMechanism> GetTestMechanism()
	{
		FeedbackMechanism m1 = new FeedbackMechanism();
		m1.setActive(true);
		m1.setCanBeActivated(true);
		m1.setOrder(1);
		m1.setType("TEXT");
		List<FeedbackParameter> params = new ArrayList<>();
		FeedbackParameter param1 = new FeedbackParameter();
		param1.setKey("max_length");
		param1.setValue(100);
		param1.setDefaultValue(200);
		param1.setEditableByUser(false);
		FeedbackParameter param2 = new FeedbackParameter();
		param2.setKey("title");
		param2.setValue("Title for text feedback");
		param2.setDefaultValue(null);
		param2.setEditableByUser(null);
		params.add(param1);
		params.add(param2);
		m1.setParameters(params);
		List<FeedbackMechanism> mechanisms = new ArrayList<>();
		mechanisms.add(m1);
		return mechanisms;
	}

}
