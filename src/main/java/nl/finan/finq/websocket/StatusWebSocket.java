package nl.finan.finq.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.finan.finq.dao.RunningStoriesDao;
import nl.finan.finq.entities.RunningStories;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

@ServerEndpoint(value="/api/statusws")
@Stateless
public class StatusWebSocket {

    private static final String SUBSCRIBE = "subscribe";
    private static final String UNSUBSCRIBE = "unsubscribe";

    @EJB
    private RunningStoriesDao runningStoriesDao;
    
    @EJB
    private OpenConnections openConnections;

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
        openConnections.removeSession(session);
    }

    public void sendStatus(Long reportId, Object log, StatusType type){
        if(openConnections.containsKey(reportId)) {
            for (Session session : openConnections.get(reportId)) {
                session.getAsyncRemote().sendText(toJson(new StatusTO(reportId, log, type)));
            }
        }
    }

    private void unsubscribe(Session session, String message) throws IOException{
        Long reportId = getReportId(message);
        openConnections.removeFromConnectionMap(reportId,session);
    }

    private void subscribe(Session session, String message) throws IOException {
        Long reportId = getReportId(message);
        RunningStories runningStories = runningStoriesDao.find(reportId);
        if(runningStories !=null) {
        	openConnections.add(reportId, session);
            session.getAsyncRemote().sendText(toJson(new StatusTO(reportId, runningStories, StatusType.INITIAL_STATUS)));
        }else{
            session.getAsyncRemote().sendText("Could not find the report you're subscribing too.");
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
