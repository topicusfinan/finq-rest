package nl.finan.jbehave.steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Step {

    private static final Logger LOGGER = LoggerFactory.getLogger(Step.class);

    @Given("this is a given step")
    public void test(){
        LOGGER.info("Log! ");
    }

    @When("a When step has been run")
    public void fail(){
        Assert.assertEquals("unequal",1,0);
    }

}
