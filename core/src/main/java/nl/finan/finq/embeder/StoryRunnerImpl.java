package nl.finan.finq.embeder;

import nl.eernie.jmoribus.JMoribus;
import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
import nl.finan.finq.websocket.StatusWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class StoryRunnerImpl implements StoryRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StoryRunnerImpl.class);

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private StatusWebSocket statusWebSocket;

    @EJB
    private ConfigurationFactory configurationFactory;

    @Override
    public void run(RunMessage object) {
        List<ParseableStory> parseableStories = new ArrayList<>(object.getStories().size());
        for (Story story : object.getStories()) {
            parseableStories.add(new ParseableStory(new ByteArrayInputStream(story.toStory().getBytes()), story.getId().toString()+"-"+object.getRunId()));
        }
        List<nl.eernie.jmoribus.model.Story> stories = StoryParser.parseStories(parseableStories);

        JMoribus jMoribus = new JMoribus(configurationFactory.getConfiguration());

        try {
            jMoribus.runStories(stories);
            RunningStories runningStories = runningStoriesDao.find(object.getRunId());
            if(runningStories.getStatus().equals(LogStatus.RUNNING)){
                runningStories.setStatus(LogStatus.SUCCESS);
            }
            statusWebSocket.sendStatus(runningStories);

        } catch (Exception e) {
            LOGGER.error("exception while running stories {}, {} ", e.getMessage(), e);

            RunningStories runningStories = runningStoriesDao.find(object.getRunId());
            runningStories.setStatus(LogStatus.FAILED);
            statusWebSocket.sendStatus(runningStories);
        }
    }
}
