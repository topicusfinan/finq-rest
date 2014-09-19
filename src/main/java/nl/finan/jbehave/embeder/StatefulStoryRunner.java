package nl.finan.jbehave.embeder;

import nl.eernie.jmoribus.JMoribus;
import nl.eernie.jmoribus.configuration.Configuration;
import nl.eernie.jmoribus.configuration.DefaultConfiguration;
import nl.eernie.jmoribus.parser.ParseableStory;
import nl.eernie.jmoribus.parser.StoryParser;
import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.factory.BeanFactory;
import nl.finan.jbehave.steps.Step;
import nl.finan.jbehave.websocket.StatusWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Stateful
public class StatefulStoryRunner implements StoryRunner{

    private static final Logger LOGGER = LoggerFactory.getLogger(StatefulStoryRunner.class);

    private List<nl.eernie.jmoribus.model.Story> stories;
    private Long reportId;

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private StatusWebSocket statusWebSocket;

    public void init(List<Story> stories, Long reportId){
        List<ParseableStory> parseableStories = new ArrayList<>(stories.size());
        for (Story story : stories) {
            parseableStories.add(new ParseableStory(new ByteArrayInputStream(story.toStory().getBytes()),story.getId().toString()));
        }
        this.stories = StoryParser.parseStories(parseableStories);
        this.reportId = reportId;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void run() {

        Configuration configuration = new DefaultConfiguration();
        configuration.addSteps(Arrays.<Object>asList(new Step()));
        WebStoryReporter reporter = BeanFactory.getBean(WebStoryReporter.class);
        reporter.init(reportId);
        configuration.addReporter(reporter);
        JMoribus jMoribus = new JMoribus(configuration);

        try{
            jMoribus.playAct(this.stories);
            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(RunningStoriesStatus.SUCCESS);
            statusWebSocket.sendStatus(reportId,runningStories);

        }catch (Exception e){
            LOGGER.error("exception while running stories {}, {} ", e.getMessage(), e);

            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(RunningStoriesStatus.FAILED);
            statusWebSocket.sendStatus(reportId,runningStories);
        }
    }
}
