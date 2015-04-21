package nl.finan.finq.common.to;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EventType
{
    SUBSCRIBE("run:subscribe"),
    UNSUBSCRIBE("run:unsubscribe"),
    GIST("run:gist"),
    PROGRESS("run:progress");

    private String value;

    EventType(String value)
    {
        this.value = value;
    }

    @JsonCreator
    public static EventType forValue(String s)
    {
        for (EventType eventType : EventType.values())
        {
            if (eventType.value.equals(s))
            {
                return eventType;
            }
        }
        throw new IllegalArgumentException("EventType " + s + " does not excist");
    }

    @JsonValue
    public String getValue()
    {
        return value;
    }
}
