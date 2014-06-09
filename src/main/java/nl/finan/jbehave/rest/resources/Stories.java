package nl.finan.jbehave.rest.resources;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Stories {

    @XmlElement
    private List<String> stories;

    public Stories() {
    }

    public Stories(List<String> stories) {
        this.stories = stories;
    }

    public List<String> getStories() {
        return stories;
    }

    public void setStories(List<String> stories) {
        this.stories = stories;
    }
}
