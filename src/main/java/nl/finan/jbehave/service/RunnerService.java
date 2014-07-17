package nl.finan.jbehave.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.dao.StoryDao;
import nl.finan.jbehave.embeder.RunStories;
import nl.finan.jbehave.entities.Bundle;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Scenario;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.factory.BeanFactory;

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
		//We have to create a new "dummy" story so jbehave has a story to run.
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

        RunStories runStories = BeanFactory.getBean(RunStories.class);
        runStories.init(stories,runningStories.getId());

        runExecutor.execute(runStories); //TODO: take a look at callable<T>. Maybe we can use this to pause a thread!
        return runningStories;
	}
}
