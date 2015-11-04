package nl.finan.finq.service;

import nl.eernie.jmoribus.model.Story;
import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Scenario;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.InputStream;

@RunWith(PowerMockRunner.class)
public class StoryServicesTest
{

	@Mock
	private StoryDao storyDao;

	@InjectMocks
	private StoryService storyService;

	@Test
	public void testConvertAndSaveStory()
	{
		InputStream fileInputStream = getClass().getResourceAsStream("/StoryServices.story");
		ParseableStory parseableStory = new ParseableStory(fileInputStream, "/StoryServices.story");

		Story story = StoryParser.parseStory(parseableStory);

		nl.finan.finq.entities.Story storyEntity = storyService.convertAndSaveStory(story);

		Mockito.verify(storyDao).persist(Mockito.any(nl.finan.finq.entities.Story.class));

		Assert.assertEquals("Story title", storyEntity.getTitle());
		Assert.assertEquals(1, storyEntity.getScenarios().size());
		Assert.assertEquals(3, storyEntity.getScenarios().get(0).getSteps().size());
		Assert.assertEquals("scenario description", storyEntity.getScenarios().get(0).getTitle());
	}

	@Test
	public void testAddParentToChilds()
	{
		InputStream fileInputStream = getClass().getResourceAsStream("/StoryServices.story");
		ParseableStory parseableStory = new ParseableStory(fileInputStream, "/StoryServices.story");

		Story story = StoryParser.parseStory(parseableStory);

		nl.finan.finq.entities.Story storyEntity = storyService.convertAndSaveStory(story);

		storyService.addParentsToChilds(storyEntity);

		Scenario scenario = storyEntity.getScenarios().get(0);
		Assert.assertSame(storyEntity, scenario.getStory());
		Assert.assertSame(scenario, scenario.getSteps().get(0).getScenario());
	}

}
