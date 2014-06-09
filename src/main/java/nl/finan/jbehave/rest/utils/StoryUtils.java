package nl.finan.jbehave.rest.utils;

import org.jbehave.core.embedder.StoryManager;
import org.jbehave.core.model.Story;

import java.util.ArrayList;
import java.util.List;

public final class StoryUtils {

    private StoryUtils() {
    }

    public static List<Story> getStoriesFromPath(List<String> paths, StoryManager manager) {
        List<Story> stories = new ArrayList<Story>();
        for (String path : paths) {
            stories.add(manager.storyOfPath(path));
        }
        return stories;
    }
}
