package nl.finan.jbehave.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.finan.jbehave.dao.RunningStoriesDao;
import nl.finan.jbehave.entities.Log;
import nl.finan.jbehave.entities.RunningStories;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ServerEndpoint(value="/api/statusws")
@Singleton
public class StatusWebSocket {

    private static final String SUBSCRIBE = "subscribe";
    private static final String UNSUBSCRIBE = "unsubscribe";

    private static final Map<Long,List<Session>> CONNECTION_MAP = new HashMap<>();

    @EJB
    private RunningStoriesDao runningStoriesDao;

    @OnMessage
    public void message(Session session, String message) throws IOException {
        if(message.startsWith(SUBSCRIBE)){
            subscribe(session,message);
        }
        if(message.startsWith(UNSUBSCRIBE)){
            unsubscribe(session,message);
        }

    }

    @OnClose
    public void close(Session session){
        for (Map.Entry<Long, List<Session>> entry : CONNECTION_MAP.entrySet()) {
            entry.getValue().remove(session);
            if(entry.getValue().isEmpty()){
                CONNECTION_MAP.remove(entry.getKey());
            }
        }
    }

    public void sendStatus(Long reportId, Log log){
        if(CONNECTION_MAP.containsKey(reportId)) {
            for (Session session : CONNECTION_MAP.get(reportId)) {
                session.getAsyncRemote().sendText(toJson(log));
            }
        }
    }

    private void unsubscribe(Session session, String message) throws IOException{
        Long reportId = getReportId(message);
        removeFromConnectionMap(reportId,session);
    }

    private void subscribe(Session session, String message) throws IOException {
        Long reportId = getReportId(message);
        RunningStories runningStories = runningStoriesDao.find(reportId);
        if(runningStories !=null) {
            addToConnectionMap(reportId, session);
            session.getAsyncRemote().sendText(toJson(runningStories));
        }else{
            session.getAsyncRemote().sendText("Could not find the report you're subscribing too.");
        }
    }

    private void addToConnectionMap(Long reportId, Session remote) {
        if(!CONNECTION_MAP.containsKey(reportId)){
            CONNECTION_MAP.put(reportId,new ArrayList<Session>());
        }
        CONNECTION_MAP.get(reportId).add(remote);
    }

    private void removeFromConnectionMap(Long reportId, Session session){
        if(CONNECTION_MAP.containsKey(reportId)){
            CONNECTION_MAP.get(reportId).remove(session);
            if(CONNECTION_MAP.get(reportId).isEmpty()){
                CONNECTION_MAP.remove(reportId);
            }
        }
    }

    private Long getReportId(String message){
        String report = message.substring(message.indexOf(':')+1);
        report = report.trim();
        return Long.valueOf(report);
    }

    private String toJson(Object object){
        try {
            ObjectMapper mapper = new ObjectMapper();

            Writer writer = new StringWriter();
            mapper.writeValue(writer,object);

            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
