package nl.finan.finq.rest;

import nl.eernie.jmoribus.configuration.Configuration;
import nl.eernie.jmoribus.model.StepType;
import nl.eernie.jmoribus.to.PossibleStepTO;
import nl.finan.finq.DefaultConfiguration;
import nl.finan.finq.embeder.ConfigurationFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Arrays;
import java.util.List;

import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class PossibleStepsResourcesTest {

    @Mock
    private ConfigurationFactory configurationFactory;

    @InjectMocks
    private PossibleStepsResources possibleStepsResources;

    @Before
    public void setUp() {
        Configuration config = new DefaultConfiguration();
        config.addSteps(Arrays.<Object>asList(new PossibleSteps()));


        when(configurationFactory.getConfiguration()).thenReturn(config);
    }

    @Test
    public void testGetPossibleSteps() {
        List<PossibleStepTO> possibleSteps = possibleStepsResources.getPossibleSteps();

        Assert.assertEquals(1, possibleSteps.size());
        Assert.assertEquals(StepType.WHEN, possibleSteps.get(0).getStepType());
        Assert.assertEquals("Test", possibleSteps.get(0).getCategories()[0]);
        Assert.assertEquals("this is a step", possibleSteps.get(0).getStep());


    }

}
