package se.bergqvist.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Event.
 *
 * https://github.com/torvalds/linux/blob/master/include/uapi/linux/input-event-codes.h
 * https://www.kernel.org/doc/html/v4.18/input/event-codes.html
 * https://www.kernel.org/doc/Documentation/input/multi-touch-protocol.txt
 * https://kernel.googlesource.com/pub/scm/linux/kernel/git/penberg/linux/+/v2.6.37-rc4/Documentation/input/multi-touch-protocol.txt
 *
 * @author Daniel Bergqvist
 */
public enum EventEnum {

    Separator(EventTypeEnum.Separator, 0x0000),
    ButtonTouch(EventTypeEnum.KeyboardButton, 0x014A),
    AbsoluteX(EventTypeEnum.Absolute, 0x0000),
    AbsoluteY(EventTypeEnum.Absolute, 0x0001),
    AbsoluteMiddleTouchSlot(EventTypeEnum.Absolute, 0x002F),
    AbsoluteMiddleTouchX(EventTypeEnum.Absolute, 0x0035),
    AbsoluteMiddleTouchY(EventTypeEnum.Absolute, 0x0036),
    AbsoluteMiddleTouchTrackingID(EventTypeEnum.Absolute, 0x0039),
    MiscellaneousTimestamp(EventTypeEnum.Miscellaneous, 0x0005);

    private static final Map<EventTypeEnum, Map<Integer, EventEnum>> _map = new HashMap<>();

    private final EventTypeEnum _type;
    private final int _value;

    static {
        for (EventTypeEnum type : EventTypeEnum.values()) {
            _map.put(type, new HashMap<>());
        }
        for (EventEnum event : EventEnum.values()) {
            _map.get(event._type).put(event._value, event);
        }
    }

    private EventEnum(EventTypeEnum type, int value) {
        this._type = type;
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public static EventEnum get(EventTypeEnum type, int value) {
        EventEnum event = _map.get(type).get(value);
        if (event == null) {
            throw new IllegalArgumentException(String.format("No event %02X for type %s", value, type.name()));
        }
        return event;
    }
}
