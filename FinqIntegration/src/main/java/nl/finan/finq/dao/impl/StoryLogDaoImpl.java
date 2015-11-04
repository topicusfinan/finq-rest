package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.StoryLogDao;
import nl.finan.finq.entities.StoryLog;

import javax.ejb.Stateless;

@Stateless
public class StoryLogDaoImpl extends DaoJPAImpl<StoryLog> implements StoryLogDao
{}
