package nl.finan.finq.steps;

import nl.eernie.jmoribus.annotation.Category;
import nl.eernie.jmoribus.annotation.Given;
import nl.eernie.jmoribus.annotation.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Step {

    private static final Logger LOGGER = LoggerFactory.getLogger(Step.class);

    @Given("this is a given step")
    public void test() throws InterruptedException {
        Thread.sleep(5000);

        LOGGER.info("Log! ");
    }

    @When("a When step has been run")
    @Category({"Test", "Failing"})
    public void fail() {
        Assert.assertEquals("unequal", 1, 0);
    }

}
