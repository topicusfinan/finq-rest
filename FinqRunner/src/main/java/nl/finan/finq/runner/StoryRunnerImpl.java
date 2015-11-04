package nl.finan.finq.runner;

import nl.eernie.jmoribus.JMoribus;
import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.finq.common.configuration.ConfigurationFactory;
import nl.finan.finq.common.configuration.FinqConfiguration;
import nl.finan.finq.common.jms.RunMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Stateless
public class StoryRunnerImpl implements StoryRunner
{
	private static final Logger LOGGER = LoggerFactory.getLogger(StoryRunnerImpl.class);

	@EJB
	private ConfigurationFactory configurationFactory;

	@EJB
	private WebStoryReporter webStoryReporter;

	@Override
	public void run(RunMessage object)
	{
		List<ParseableStory> parseableStories = new ArrayList<>(object.getStories().size());
		for (Map.Entry<String, String> story : object.getStories().entrySet())
		{
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(story.getValue().getBytes());
			ParseableStory parseableStory = new ParseableStory(byteArrayInputStream, story.getKey());
			parseableStories.add(parseableStory);
		}

		List<nl.eernie.jmoribus.model.Story> stories = StoryParser.parseStories(parseableStories);
		FinqConfiguration configuration = configurationFactory.getConfiguration();
		configuration.setEnvironment(object.getEnvironment());
		JMoribus jMoribus = new JMoribus(configuration);

		try
		{
			jMoribus.runStories(stories);
			webStoryReporter.afterSuccessRun(object.getRunningProcessId());
		}
		catch (Exception e)
		{
			LOGGER.error("exception while running stories [{}]", e.getMessage());
			webStoryReporter.afterErrorRun(object.getRunningProcessId());
		}
	}
}
