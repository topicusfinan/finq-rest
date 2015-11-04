package nl.finan.finq.dao.impl;

import nl.finan.finq.dao.TagDao;
import nl.finan.finq.entities.Tag;

import javax.ejb.Stateless;

@Stateless
public class TagDaoImpl extends DaoJPAImpl<Tag> implements TagDao {
}
