package ch.uzh.ifi.feedback.orchestrator.validation.test;

import org.mockito.Mockito;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import ch.uzh.ifi.feedback.orchestrator.model.FeedbackMechanism;
import ch.uzh.ifi.feedback.orchestrator.services.MechanismService;
import ch.uzh.ifi.feedback.orchestrator.validation.MechanismValidator;
import junit.framework.TestCase;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

public class MechanismValidatorTest extends TestCase {
	
	private MechanismValidator testee;
	private FeedbackMechanism testMechanism;
	private MechanismService mechanismService;
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        ValidationSerializer serializer = Mockito.mock(ValidationSerializer.class);
        mechanismService = Mockito.mock(MechanismService.class);
        testee = new MechanismValidator(null, mechanismService, serializer);
        testMechanism = new FeedbackMechanism();
        testMechanism.setActive(true);
        testMechanism.setType("TEXT_TYPE");
        testMechanism.setId(1);
        testMechanism.setConfigurationsid(1);
    }
    
    public void testValidator_WhenMultipleActiveMechanismsOfSameType_ThenHasErrors() throws Exception
    {
    	//arrange
    	FeedbackMechanism other = new FeedbackMechanism();
    	other.setId(2);
    	other.setActive(true);
    	List<FeedbackMechanism> resultList = new ArrayList<>();
    	resultList.add(other);
    	
    	when(mechanismService.GetWhere(
				asList(testMechanism.getConfigurationsid(), testMechanism.getType(), testMechanism.isActive()), 
				"configurations_id = ?", "`name` = ?", "active = ?"))
    	.thenReturn(resultList);
    	
    	//act
    	ValidationResult result = testee.Validate(testMechanism);
    	
    	//assert
    	assertTrue(result.hasErrors());
    }
}
