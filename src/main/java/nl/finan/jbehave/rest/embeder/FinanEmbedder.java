package nl.finan.jbehave.rest.embeder;

import nl.finan.jbehave.RunnerStepMonitor;

import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.StoryRunner;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.PrintStreamStepMonitor;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class FinanEmbedder extends Embedder implements ApplicationContextAware {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanEmbedder.class);
    
    private ApplicationContext ctx;

    public FinanEmbedder() {
        super.useConfiguration(new MostUsefulConfiguration().useStepMonitor(new RunnerStepMonitor()));
        useStoryRunner(new StoryRunner()); // this could maybe be session specific
    }

    public List<String> storyPaths() {
        String codeLocation = CodeLocations.codeLocationFromClass(this.getClass()).getFile();
        List<String> list = new StoryFinder().findPaths(codeLocation, Arrays.asList("**/*.story"), Arrays.asList("**/disabled*.story", "**/selenium/**/*.story"));
        return list;
    }

    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), ctx);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}


