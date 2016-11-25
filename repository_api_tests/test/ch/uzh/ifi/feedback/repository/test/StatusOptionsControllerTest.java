package ch.uzh.ifi.feedback.repository.test;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import ch.uzh.ifi.feedback.repository.model.StatusOption;

import static java.util.Arrays.asList;

public class StatusOptionsControllerTest extends RepositoryServletTest {

	public void testRetrievingAllOptions() throws ClientProtocolException, IOException {
		StatusOption[] options = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);

		assertEquals(options.length, 6);
	}
	
	public void testDeleteAnOption() throws ClientProtocolException, IOException {
		DeleteSuccess("http://localhost:8080/feedback_repository/en/status_options/1");

		StatusOption[] options = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);

		assertEquals(options.length, 5);
		ValidateOrder(asList(options));
	}
	
	public void testInsertNewOption() throws IOException
	{
		InputStream stream = this.getClass().getResourceAsStream("options_insert.json");
		String jsonString = IOUtils.toString(stream); 
		
		StatusOption createdOption = PostSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				jsonString, 
				StatusOption.class);
		
		assertEquals(createdOption.getName(), "new_state");
		assertEquals(createdOption.getOrder(), 2);
		assertEquals(createdOption.isUserSpecific(), false);
		
		StatusOption[] allOptions = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);
		
		assertTrue(asList(allOptions).stream().filter(o -> o.getOrder() == 2 && o.isUserSpecific() == false).count() == 3);
		ValidateOrder(asList(allOptions));
	}
	
	public void testUpdateOfOption() throws IOException
	{	
		StatusOption createdOption = PutSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				"{ 'id': 3, 'order': 3}", 
				StatusOption.class);
		
		assertEquals(createdOption.getName(), "new");
		//expected order is 2 since options have to be shifted down
		assertEquals(createdOption.getOrder(), 2);
		assertEquals(createdOption.isUserSpecific(), false);
		
		StatusOption[] allOptions = GetSuccess(
				"http://localhost:8080/feedback_repository/en/status_options", 
				StatusOption[].class);
		
		assertTrue(asList(allOptions).stream().filter(o -> o.getOrder() == 2 && o.isUserSpecific() == false).count() == 2);
		ValidateOrder(asList(allOptions));
	}
	
	private void ValidateOrder(List<StatusOption> options)
	{
		List<StatusOption> userSpecific = options.stream().filter(o -> o.isUserSpecific() == true)
				.sorted((a, b) -> Integer.compare(a.getOrder(), b.getOrder())).collect(Collectors.toList());
		
		List<StatusOption> general = options.stream().filter(o -> o.isUserSpecific() == false)
				.sorted((a, b) -> Integer.compare(a.getOrder(), b.getOrder())).collect(Collectors.toList());
		
		ValidateListOrder(userSpecific);
		ValidateListOrder(general);
	}
	
	private void ValidateListOrder(List<StatusOption> options)
	{
		for(int i=0; i<options.size(); i++)
		{
			StatusOption option = options.get(i);
			int order = option.getOrder();
			if(i == 0)
			{
				assertEquals(order, 1);
			}else if(i == options.size() - 1)
			{
				assertTrue(order == options.get(i - 1).getOrder() || order == options.get(i - 1).getOrder() + 1);
			}
			else{
				assertTrue(
						(order == options.get(i - 1).getOrder() || order == options.get(i - 1).getOrder() + 1) 
						&& 
						(order == options.get(i + 1).getOrder() || order == options.get(i + 1).getOrder() - 1));
			}
		}
	}
	
}
