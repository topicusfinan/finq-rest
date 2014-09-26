package nl.finan.jbehave.embeder;

import nl.finan.jbehave.entities.Story;

import javax.ejb.Local;
import java.util.List;

@Local
public interface StoryRunner extends Runnable {

  void init(List<Story> stories, Long reportId);
}
