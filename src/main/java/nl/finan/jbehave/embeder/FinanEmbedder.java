package nl.finan.jbehave.embeder;


import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.PrintStreamEmbedderMonitor;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.spring.SpringStepsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Component
@Scope("prototype")
public class FinanEmbedder extends Embedder implements ApplicationContextAware {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanEmbedder.class);
    
    private ApplicationContext ctx;

    public FinanEmbedder(){
        configuration().useStoryControls(new StoryControls().doSkipScenariosAfterFailure(false).doDryRun(false));
        configuration().storyReporterBuilder()
                .withFailureTrace(true)
                .withCodeLocation(CodeLocations.codeLocationFromClass(FinanEmbedder.class));
        useStepsFactory(stepsFactory());
        this.embedderMonitor = new PrintStreamEmbedderMonitor();

        this.storyManager =  new StoryManager(configuration(), stepsFactory(), embedderControls(), embedderMonitor(), executorService(), performableTree());
    }

    public void setReportId(final Long id){
        configuration().storyReporterBuilder().withFormats(new Format("webformat") {
            @Override
            public StoryReporter createStoryReporter(FilePrintStreamFactory factory, StoryReporterBuilder storyReporterBuilder) {
                WebStoryReporter reporter = ctx.getBean(WebStoryReporter.class);
                reporter.init(id);
                return reporter;
            }
        });
    }


    public InjectableStepsFactory stepsFactory() {
        return new SpringStepsFactory(configuration(), ctx);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}


