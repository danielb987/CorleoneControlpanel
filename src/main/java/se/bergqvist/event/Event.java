package se.bergqvist.event;

/**
 *
 * @author Daniel Bergqvist
 */
public class Event {

    private final EventTypeEnum _eventType;
    private final EventEnum _event;
    private final long _value;

    public Event(EventTypeEnum eventType, EventEnum event, long value) {
        this._eventType = eventType;
        this._event = event;
        this._value = value;
    }

    public EventTypeEnum getEventType() {
        return _eventType;
    }

    public EventEnum getEvent() {
        return _event;
    }

    public long getValue() {
        return _value;
    }

}
