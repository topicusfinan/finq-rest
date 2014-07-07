package nl.finan.jbehave.embeder;


import nl.finan.jbehave.factory.BeanFactory;
import nl.finan.jbehave.steps.Step;
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
import org.jbehave.core.steps.InstanceStepsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateful;
import javax.naming.InitialContext;
import javax.naming.NamingException;

@Stateful
public class FinanEmbedder extends Embedder {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(FinanEmbedder.class);
    


    public FinanEmbedder(){
        configuration().useStoryControls(new StoryControls().doSkipScenariosAfterFailure(false).doDryRun(false));
        configuration().storyReporterBuilder()
                .withFailureTrace(true)
                .withCodeLocation(CodeLocations.codeLocationFromClass(FinanEmbedder.class));
        useStepsFactory(stepsFactory());
        this.embedderMonitor = new PrintStreamEmbedderMonitor();
        this.embedderControls.doGenerateViewAfterStories(false);

        this.storyManager =  new StoryManager(configuration(), stepsFactory(), embedderControls(), embedderMonitor(), executorService(), performableTree());
    }

    public void setReportId(final Long id){
        configuration().storyReporterBuilder().withFormats(new Format("webformat") {
            @Override
            public StoryReporter createStoryReporter(FilePrintStreamFactory factory, StoryReporterBuilder storyReporterBuilder) {
                WebStoryReporter reporter = BeanFactory.getBean(WebStoryReporter.class);
                reporter.init(id);
                return reporter;
            }
        });
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(),new Step());
    }
}


