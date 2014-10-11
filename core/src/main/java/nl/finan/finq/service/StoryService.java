package nl.finan.finq.service;


import nl.eernie.jmoribus.model.*;
import nl.eernie.jmoribus.model.Scenario;
import nl.eernie.jmoribus.model.Step;
import nl.finan.finq.dao.ScenarioLogDao;
import nl.finan.finq.dao.StepLogDao;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.dao.StoryLogDao;
import nl.finan.finq.entities.*;
import nl.finan.finq.entities.Story;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class StoryService {

    @EJB
    private StoryDao storyDao;

    public Story convertAndSaveStory(nl.eernie.jmoribus.model.Story story)
    {
        Story storyEntity = new Story();
        storyEntity.setTitle(story.getTitle());

        for(Scenario scenario : story.getScenarios())
        {
            nl.finan.finq.entities.Scenario scenarioEntity = new nl.finan.finq.entities.Scenario();
            scenarioEntity.setTitle(scenario.getTitle());
            scenarioEntity.setStory(storyEntity);

            for(Step step : scenario.getSteps())
            {
                nl.finan.finq.entities.Step stepEntity = new nl.finan.finq.entities.Step();
                stepEntity.setTitle(step.getStepType().name() + " " + step.getCombinedStepLines());
                stepEntity.setScenario(scenarioEntity);
                scenarioEntity.getSteps().add(stepEntity);
            }

            storyEntity.getScenarios().add(scenarioEntity);
        }

        storyDao.persist(storyEntity);

        return storyEntity;
    }
}
