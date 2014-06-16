package nl.finan.jbehave.rest.embeder;

import nl.finan.jbehave.rest.RunningContextLocal;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.*;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.reporters.*;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class FinanEmbedder extends Embedder implements ApplicationContextAware {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanEmbedder.class);
    
    private ApplicationContext ctx;

    @Autowired
    private RunningContextLocal runningContext = new RunningContextLocal();

    @Autowired
    private WebStoryReporter webStoryReporter;

    @Autowired
    private WebFormat format;

    @PostConstruct
    public void init(){
        configuration().useStoryControls(new StoryControls().doSkipScenariosAfterFailure(false).doDryRun(false));
        configuration().storyReporterBuilder()
                .withFailureTrace(true)
                .withCodeLocation(CodeLocations.codeLocationFromClass(FinanEmbedder.class))
                .withFormats(format);
        useStepsFactory(stepsFactory());
        this.embedderMonitor = new PrintStreamEmbedderMonitor();

        this.storyManager =  new StoryManager(configuration(), stepsFactory(), embedderControls(), embedderMonitor(), executorService(), performableTree());
    }

    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), ctx);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }

    public RunningContextLocal getRunningContext() {
        return runningContext;
    }

}


