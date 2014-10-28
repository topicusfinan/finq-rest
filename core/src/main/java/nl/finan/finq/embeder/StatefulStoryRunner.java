package nl.finan.finq.embeder;

import nl.eernie.jmoribus.JMoribus;
import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.LogStatus;
import nl.finan.finq.entities.RunningStories;
import nl.finan.finq.entities.Story;
import nl.finan.finq.websocket.StatusType;
import nl.finan.finq.websocket.StatusWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Stateful
public class StatefulStoryRunner implements StoryRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatefulStoryRunner.class);

    private List<nl.eernie.jmoribus.model.Story> stories;
    private Long reportId;

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private StatusWebSocket statusWebSocket;

    @EJB
    private ConfigurationFactory configurationFactory;

    public void init(List<Story> stories, Long reportId) {
        List<ParseableStory> parseableStories = new ArrayList<>(stories.size());
        for (Story story : stories) {
            parseableStories.add(new ParseableStory(new ByteArrayInputStream(story.toStory().getBytes()), story.getId().toString()+"-"+reportId));
        }
        this.stories = StoryParser.parseStories(parseableStories);
        this.reportId = reportId;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void run() {


        JMoribus jMoribus = new JMoribus(configurationFactory.getConfigurationWithReporter(reportId));

        try {
            jMoribus.runStories(this.stories);
            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(LogStatus.SUCCESS);
            statusWebSocket.sendStatus(reportId, runningStories, StatusType.FINAL_STATUS);

        } catch (Exception e) {
            LOGGER.error("exception while running stories {}, {} ", e.getMessage(), e);

            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(LogStatus.FAILED);
            statusWebSocket.sendStatus(reportId, runningStories, StatusType.FINAL_STATUS);
        }
    }
}
