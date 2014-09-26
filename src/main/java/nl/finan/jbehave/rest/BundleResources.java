package nl.finan.jbehave.rest;

import nl.finan.jbehave.dao.BundleDao;
import nl.finan.jbehave.entities.Bundle;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("bundles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Stateless
public class BundleResources {

	
	@EJB
	private BundleDao bundleDao;
	
	@GET
	public List<Bundle> getBundles(){
		List<Bundle> all = bundleDao.listAll();
		
		return all;
	}
}
