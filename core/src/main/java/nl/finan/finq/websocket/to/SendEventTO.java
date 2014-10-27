package nl.finan.finq.websocket.to;


public class SendEventTO {

    private EventType event;
    private Object data;

    public SendEventTO(EventType event, Object log) {
        this.event = event;
        this.data = log;
    }

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType eventType) {
        this.event = eventType;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
