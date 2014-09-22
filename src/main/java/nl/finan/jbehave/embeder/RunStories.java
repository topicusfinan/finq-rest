package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.websocket.StatusType;
import nl.finan.jbehave.websocket.StatusWebSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateful;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Local(RunStories.class)
@Stateful
public class RunStories implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(RunStories.class);

    private List<org.jbehave.core.model.Story> stories;
    private Long reportId;

    @EJB
    private FinanEmbedder embedder;

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @EJB
    private StatusWebSocket statusWebSocket;

    public RunStories() {
    }

    public void init(List<Story> stories, Long reportId){
        this.stories = new ArrayList<>();
        for(Story story: stories) {
            this.stories.add(embedder.storyManager().storyOfText(story.toStory(), String.valueOf(story.getId())));
        }
        this.reportId = reportId;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void run() {
        embedder.setReportId(reportId);

        try{
            embedder.runStories(this.stories);
            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(RunningStoriesStatus.SUCCESS);
            statusWebSocket.sendStatus(reportId,runningStories,StatusType.FINAL_STATUS);

        }catch (Exception e){
            LOGGER.error("exception while running stories {}, {} ", e.getMessage(), e);

            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(RunningStoriesStatus.FAILED);
            statusWebSocket.sendStatus(reportId,runningStories,StatusType.FINAL_STATUS);
        }
    }
}
