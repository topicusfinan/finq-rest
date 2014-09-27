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
import java.util.Date;
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
        return doRun(Arrays.asList(story));
    }

    public RunningStories run(Bundle bundle) {
        return doRun(bundle.getStories());
    }

    public RunningStories run(Scenario scenario) {
        //We have to create a new "dummy" story so jmoribus has a story to run.
        Story story = new Story();
        story.setName("Dummy Story " + new Date().getTime());
        story.setDummy(true);
        story.getScenarios().add(scenario);
        storyDao.persist(story);

        return doRun(Arrays.asList(story));
    }

    private RunningStories doRun(List<Story> stories){
        RunningStories runningStories = new RunningStories();
        runningStories.setStatus(RunningStoriesStatus.RUNNING);
        runningStoriesDao.persist(runningStories);

        StoryRunner statefulStoryRunner = BeanFactory.getBean(StoryRunner.class);
        statefulStoryRunner.init(stories, runningStories.getId());

        runExecutor.execute(statefulStoryRunner);
        return runningStories;
    }
}
