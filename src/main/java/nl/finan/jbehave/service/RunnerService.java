package nl.finan.jbehave.service;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.concurrent.ManagedExecutorService;

import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.embeder.RunStories;
import nl.finan.jbehave.entities.Bundle;
import nl.finan.jbehave.entities.RunningStories;
import nl.finan.jbehave.entities.RunningStoriesStatus;
import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.factory.BeanFactory;

@Stateless
public class RunnerService {

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
