package nl.finan.finq.websocket;

import javax.ejb.Singleton;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class OpenConnections {

	private static final Map<Long,List<Session>> CONNECTION_MAP = new HashMap<>();
	
    public void removeFromConnectionMap(Long reportId, Session session){
        if(CONNECTION_MAP.containsKey(reportId)){
            CONNECTION_MAP.get(reportId).remove(session);
            if(CONNECTION_MAP.get(reportId).isEmpty()){
                CONNECTION_MAP.remove(reportId);
            }
        }
    }

	public void removeSession(Session session) {
        for (Map.Entry<Long, List<Session>> entry : CONNECTION_MAP.entrySet()) {
            entry.getValue().remove(session);
            if(entry.getValue().isEmpty()){
                CONNECTION_MAP.remove(entry.getKey());
            }
        }		
	}

	public boolean containsKey(Long reportId) {
		return CONNECTION_MAP.containsKey(reportId);
	}

	public List<Session> get(Long reportId) {
		return CONNECTION_MAP.get(reportId);
	}
	
    public void add(Long reportId, Session remote) {
        if(!CONNECTION_MAP.containsKey(reportId)){
            CONNECTION_MAP.put(reportId,new ArrayList<Session>());
        }
        CONNECTION_MAP.get(reportId).add(remote);
    }

	
}
