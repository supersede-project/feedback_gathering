package ch.uzh.ifi.feedback.library.rest.validation.test;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import ch.uzh.ifi.feedback.library.rest.Service.ServiceBase;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationException;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationResult;
import ch.uzh.ifi.feedback.library.rest.validation.ValidationSerializer;
import javassist.NotFoundException;
import junit.framework.TestCase;
import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;

public class ValidatorBaseTest extends TestCase {
	
	private TestItemValidator testee;
	private TestItem testItem;
	private ServiceBase<TestItem> testItemService;
	
	  @Rule
	  public final ExpectedException exception = ExpectedException.none();
	
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        ValidationSerializer serializer = Mockito.mock(ValidationSerializer.class);
        testItemService = Mockito.mock(ServiceBase.class);
        testee = new TestItemValidator(testItemService, serializer);
        testItem = new TestItem(1, "test", "test");
    }
    
   @Override
	protected void tearDown() throws Exception 
    {
		super.tearDown();
    }
   
   public void testNotNullConstraint_WhenNull_ThenHasErrors() throws ValidationException, Exception
   {
	   //arrange
	   testItem.setNotNullField(null);
	   
		//act
		ValidationResult result = testee.Validate(testItem);
		//assert
		assertEquals(result.hasErrors(), true);
   }
   
   public void testNotNullConstraint_WhenNotNull_ThenOk() throws ValidationException, Exception
   {	  
		//act
		ValidationResult result = testee.Validate(testItem);
		//assert
		assertEquals(result.hasErrors(), false);
   }
   
   public void testUniqueConstraint_WhenNotUniqueAndPostRequest_ThenHasErrors() throws ValidationException, Exception
   {
	   //arrange
	   List<TestItem> itemsInDb = new ArrayList<>();
	   itemsInDb.add(new TestItem(2, "test", "test"));
	   
	   when(testItemService.GetWhere(asList("test"), "uniqueField = ?")).thenReturn(itemsInDb);
	  
		//act
		ValidationResult result = testee.Validate(testItem);
		//assert
		assertEquals(result.hasErrors(), true);
   }
   
   public void testUniqueConstraint_WhenNotUniqueAndPutRequestAndSameId_ThenOk() throws ValidationException, Exception
   {
	   //arrange
	   testItem.setId(1);
	   List<TestItem> itemsInDb = new ArrayList<>();
	   TestItem storedItem = new TestItem();
	   storedItem.setId(1);
	   itemsInDb.add(storedItem);
	   
	   when(testItemService.GetWhere(asList("test"), "uniqueField = ?")).thenReturn(itemsInDb);
	  
		//act
		ValidationResult result = testee.Validate(testItem);
		//assert
		assertEquals(result.hasErrors(), false);
   }
   
   public void testUniqueConstraint_WhenNotUniqueAndPutRequestAndNotSameId_ThenHasErrors() throws ValidationException, Exception
   {
	   //arrange
	   testItem.setId(1);
	   List<TestItem> itemsInDb = new ArrayList<>();
	   TestItem storedItem = new TestItem();
	   storedItem.setId(2);
	   itemsInDb.add(storedItem);
	   
	   when(testItemService.GetWhere(asList("test"), "uniqueField = ?")).thenReturn(itemsInDb);
	  
		//act
		ValidationResult result = testee.Validate(testItem);
		//assert
		assertEquals(result.hasErrors(), true);
   }
   
   public void testUniqueConstraint_WhenUnique_ThenOk() throws ValidationException, Exception
   {
	   //arrange
	   List<TestItem> itemsInDb = new ArrayList<>();
	   
	   when(testItemService.GetWhere(asList("test"), "uniqueField = ?")).thenReturn(itemsInDb);
	  
		//act
		ValidationResult result = testee.Validate(testItem);
		//assert
		assertEquals(result.hasErrors(), false);
   }
   
   public void testMerge_WhenWrongId_ThenNotFoundException() throws ValidationException, Exception
   {
	   //arrange
	   testItem.setId(1);
	   when(testItemService.CheckId(1)).thenReturn(false);
	  
		//act
	   boolean thrown = false;
	   try{
		   testee.Merge(testItem);
	   }catch(NotFoundException e)
	   {
		   thrown = true;
	   }
	   
	   assertTrue(thrown);
   }
   
   public void testMerge_WhenIdNotSet_ThenNotSupportedException() throws ValidationException, Exception
   {
	   //arrange
	   testItem.setId(null);
	   
		//act
	   boolean thrown = false;
	   try{
		   testee.Merge(testItem);
	   }catch(UnsupportedOperationException e)
	   {
		   thrown = true;
	   }
	   
	   assertTrue(thrown);
   }
   
   public void testMerge_WhenSameValues_ThenNotChanged() throws ValidationException, Exception
   {
	   //arrange
	   when(testItemService.CheckId(1)).thenReturn(true);
	   when(testItemService.GetById(1)).thenReturn(new TestItem(1, "test", "test"));
	   
		//act
	   testee.Merge(testItem);
	   
	   assertFalse(testItem.hasChanges());
   }
   
   public void testMerge_WhenValuesChanged_ThenChanged() throws ValidationException, Exception
   {
	   //arrange
	   when(testItemService.CheckId(1)).thenReturn(true);
	   when(testItemService.GetById(1)).thenReturn(new TestItem(1, "test2", "test2"));
	   
		//act
	   testee.Merge(testItem);
	   
	   assertTrue(testItem.hasChanges());
   }
}
