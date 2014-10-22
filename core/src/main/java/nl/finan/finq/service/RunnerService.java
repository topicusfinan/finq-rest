package nl.finan.finq.service;

import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.embeder.StoryRunner;
import nl.finan.finq.entities.*;
import nl.finan.finq.factory.BeanFactory;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;
import java.util.Arrays;
import java.util.List;

@Stateless
public class RunnerService {

    @EJB
    private StoryDao storyDao;

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @Resource
    private ManagedExecutorService runExecutor;

    public RunningStories run(Story story) {
        return run(Arrays.asList(story));
    }

    public RunningStories run(Book book) {
        return run(book.getStories());
    }

    public RunningStories run(List<Story> stories) {
        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(LogStatus.RUNNING);
        runningStoriesDao.persist(runningStories);

        StoryRunner statefulStoryRunner = BeanFactory.getBean(StoryRunner.class);
        statefulStoryRunner.init(stories, runningStories.getId());

        runExecutor.execute(statefulStoryRunner);
        return runningStories;
    }
}
