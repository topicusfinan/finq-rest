package nl.finan.finq.embeder;


import javax.ejb.Local;

@Local
public interface StoryRunner {

    void run(RunMessage object);
}
