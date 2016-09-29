package ch.uzh.ifi.feedback.orchestrator.validation.test;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.mockito.Mockito;

import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.Configuration;
import ch.uzh.ifi.feedback.orchestrator.model.ConfigurationType;
import ch.uzh.ifi.feedback.orchestrator.services.ConfigurationService;
import ch.uzh.ifi.feedback.orchestrator.validation.ConfigurationValidator;
import junit.framework.TestCase;

public class ConfigurationValidatorTest extends TestCase {
	
	private ConfigurationValidator testee;
	private Configuration testConfig;
	private ConfigurationService configurationService;
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        ValidationSerializer serializer = Mockito.mock(ValidationSerializer.class);
        configurationService = Mockito.mock(ConfigurationService.class);
        testee = new ConfigurationValidator(null, null, configurationService, serializer);
        testConfig = new Configuration();
        testConfig.setId(1);
        testConfig.setApplicationId(1);
        testConfig.setUserGroupsId(1);
        testConfig.setType(ConfigurationType.PUSH);
    }
    
    public void testValidator_WhenMultiplePushConfigurationsPerUSerGroupAndApplication_ThenHasErrors() throws Exception
    {
    	//arrange
    	Configuration other = new Configuration();
    	other.setId(2);
    	other.setApplicationId(1);
    	other.setUserGroupsId(1);
    	other.setType(ConfigurationType.PUSH);
    	
    	List<Configuration> resultList = new ArrayList<>();
    	resultList.add(other);
    	
    	when(configurationService.GetWhere(
				asList(testConfig.getUserGroupsId(), testConfig.getApplicationId(), testConfig.getType().toString()),
				"user_groups_id = ?", "applications_id = ?", "type = ?"))
    	.thenReturn(resultList);
    	
    	//act
    	ValidationResult result = testee.Validate(testConfig);
    	
    	//assert
    	assertTrue(result.hasErrors());
    }
}
