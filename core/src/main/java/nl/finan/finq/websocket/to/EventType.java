package nl.finan.finq.websocket.to;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum EventType {
    SUBSCRIBE("run:subscribe"),
    UNSUBSCRIBE("run:unsubscribe");

    private String value;
    EventType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static EventType forValue(String s){
        for (EventType eventType : EventType.values()) {
            if(eventType.value.equals(s)){
                return eventType;
            }
        }
        throw new IllegalArgumentException("EventType "+s+" does not excist");
    }
}
