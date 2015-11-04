package nl.finan.finq.websocket.to;

import nl.finan.finq.common.to.EventType;

public class ReceivingEventTO {

    private EventType event;
    private DataTO data;

    public EventType getEvent() {
        return event;
    }

    public void setEvent(EventType event) {
        this.event = event;
    }

    public DataTO getData() {
        return data;
    }

    public void setData(DataTO data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "EventTO{" +
            "event='" + event + '\'' +
            ", data=" + data +
            '}';
    }
}
