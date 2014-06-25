package nl.finan.jbehave.embeder;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Story;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class RunStories implements Runnable{

    private static final Logger LOGGER = LoggerFactory.getLogger(RunStories.class);

    private List<org.jbehave.core.model.Story> stories;
    private Long reportId;

    @Autowired
    private FinanEmbedder embedder;

    @Autowired
    private RunningStoriesDao runningStoriesDao;

    public RunStories() {
    }

    public void init(List<Story> stories, Long reportId){
        this.stories = new ArrayList<org.jbehave.core.model.Story>();
        for(Story story: stories) {
            this.stories.add(embedder.storyManager().storyOfText(story.toStory(), String.valueOf(story.getId())));
        }
        this.reportId = reportId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run() {
        embedder.setReportId(reportId);

        try{
            embedder.runStories(this.stories);
            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(RunningStoriesStatus.SUCCESS);

        }catch (Exception e){
            LOGGER.error("exception while running stories {}, {} ", e.getMessage(), e);

            RunningStories runningStories = runningStoriesDao.find(reportId);
            runningStories.setStatus(RunningStoriesStatus.FAILED);
        }

    }
}
