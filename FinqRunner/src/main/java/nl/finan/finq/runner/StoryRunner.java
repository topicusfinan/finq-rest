package nl.finan.finq.runner;


import nl.finan.finq.common.jms.RunMessage;

import javax.ejb.Local;

@Local
public interface StoryRunner {

    void run(RunMessage object);
}
