package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.ApplicationDao;
import nl.finan.finq.entities.Application;

import javax.ejb.Stateless;

@Stateless
public class ApplicationDaoImpl extends DaoJPAImpl<Application> implements ApplicationDao{
}
