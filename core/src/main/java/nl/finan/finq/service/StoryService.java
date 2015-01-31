package nl.finan.finq.service;


import nl.eernie.jmoribus.model.Scenario;
import nl.eernie.jmoribus.model.Step;
import nl.eernie.jmoribus.model.StepContainer;
import nl.finan.finq.dao.StoryDao;
import nl.finan.finq.entities.Prologue;
import nl.finan.finq.entities.Story;

import javax.ejb.EJB;
import javax.ejb.Stateless;

@Stateless
public class StoryService {

    @EJB
    private StoryDao storyDao;

    public Story convertAndSaveStory(nl.eernie.jmoribus.model.Story story) {
        Story storyEntity = new Story();
        storyEntity.setTitle(story.getTitle());

        if(story.getPrologue() !=null) {
            Prologue prologue = new Prologue();
            convertSteps(story.getPrologue(),prologue);
        }

        for (Scenario scenario : story.getScenarios()) {
            nl.finan.finq.entities.Scenario scenarioEntity = new nl.finan.finq.entities.Scenario();
            scenarioEntity.setTitle(scenario.getTitle());
            scenarioEntity.setStory(storyEntity);

            convertSteps(scenario, scenarioEntity);

            storyEntity.getScenarios().add(scenarioEntity);
        }

        storyDao.persist(storyEntity);

        return storyEntity;
    }

    private void convertSteps(StepContainer scenario, nl.finan.finq.entities.StepContainer stepContainerEntity) {
        for (Step step : scenario.getSteps()) {
            nl.finan.finq.entities.Step stepEntity = new nl.finan.finq.entities.Step();
            String key = step.getStepType().name();
            stepEntity.setTitle(key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase() + " " + step.getCombinedStepLines());
            stepEntity.setStepContainer(stepContainerEntity);
            stepContainerEntity.getSteps().add(stepEntity);
        }
    }

    public void addParentsToChilds(Story story) {
        if(story.getPrologue() !=null){
            story.getPrologue().setStory(story);
        }
        for (nl.finan.finq.entities.Scenario scenario : story.getScenarios()) {
            scenario.setStory(story);
            addParentsToChilds(scenario);
        }
    }

    private void addParentsToChilds(nl.finan.finq.entities.Scenario scenario) {
        for (nl.finan.finq.entities.Step step : scenario.getSteps()) {
            step.setStepContainer(scenario);
        }
    }
}
