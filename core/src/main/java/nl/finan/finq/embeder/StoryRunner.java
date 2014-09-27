package nl.finan.finq.embeder;

import nl.finan.finq.entities.Story;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StoryRunner extends Runnable {

    void init(List<Story> stories, Long reportId);
}
