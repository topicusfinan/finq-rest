package nl.finan.jbehave.dao;


import nl.finan.jbehave.entities.StepLog;
import nl.finan.jbehave.entities.Story;
import org.springframework.stereotype.Repository;

@Repository
public class StepDaoImpl extends DaoJPAImpl<StepLog> implements StepLogDao {


}
