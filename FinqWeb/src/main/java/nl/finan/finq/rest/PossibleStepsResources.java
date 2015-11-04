package nl.finan.finq.rest;

import nl.eernie.jmoribus.JMoribus;
import nl.eernie.jmoribus.configuration.Configuration;
import nl.eernie.jmoribus.to.PossibleStepTO;
import nl.finan.finq.common.configuration.ConfigurationFactory;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path(PathConstants.POSSIBLESTEPS)
@Stateless
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PossibleStepsResources
{

	@EJB
	private ConfigurationFactory configurationFactory;

	@GET
	public List<PossibleStepTO> getPossibleSteps()
	{
		Configuration config = configurationFactory.getConfiguration();
		JMoribus jMoribus = new JMoribus(config);
		return jMoribus.getPossibleSteps();
	}

}
