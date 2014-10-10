package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.StepDao;
import nl.finan.finq.entities.Step;
import nl.finan.finq.entities.Story;

import javax.ejb.Stateless;

@Stateless
public class StepDaoImpl extends DaoJPAImpl<Step> implements StepDao{
}
