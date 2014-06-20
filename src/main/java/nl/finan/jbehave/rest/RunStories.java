package nl.finan.jbehave.rest;

import nl.finan.jbehave.entities.Story;
import nl.finan.jbehave.embeder.FinanEmbedder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component(value = "RunStories")
@Scope("prototype")
@Transactional
public class RunStories implements Runnable{

    private List<Story> stories;
    private Long reportId;

    @Autowired
    private FinanEmbedder embedder;

    public RunStories() {
    }

    public void init(List<Story> stories, Long reportId){
        this.stories = stories;
        this.reportId = reportId;
        embedder.setReportId(reportId);
    }

    @Override
    public void run() {
        List<org.jbehave.core.model.Story> jbehaveStories = new ArrayList<org.jbehave.core.model.Story>();
        for(Story story: stories) {
            jbehaveStories.add(embedder.storyManager().storyOfText(story.toStory(), "db/" + story.getId()));
        }

        embedder.runStories(jbehaveStories);

    }
}
