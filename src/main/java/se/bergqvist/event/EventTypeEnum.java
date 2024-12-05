package se.bergqvist.event;

import java.util.HashMap;
import java.util.Map;

/**
 * Event type.
 *
 * https://github.com/torvalds/linux/blob/master/include/uapi/linux/input-event-codes.h
 * https://www.kernel.org/doc/html/v4.18/input/event-codes.html
 *
 * @author Daniel Bergqvist
 */
public enum EventTypeEnum {

    Separator(0x00),
    KeyboardButton(0x01),
    Relative(0x02),
    Absolute(0x03),
    Miscellaneous(0x04),
    BinaryStateInputSwitch(0x05),
    LED(0x11),
    Sound(0x12),
    AutoRepeating(0x14),
    ForceFeedback(0x15),
    PowerButton(0x16),
    ForceFeedbackDeviceStatus(0x17);

    private static final Map<Integer, EventTypeEnum> _map = new HashMap<>();

    private final int _value;

    static {
        for (EventTypeEnum type : EventTypeEnum.values()) {
            _map.put(type._value, type);
        }
    }

    private EventTypeEnum(int value) {
        this._value = value;
    }

    public int getValue() {
        return _value;
    }

    public static EventTypeEnum get(int value) {
        EventTypeEnum type = _map.get(value);
        if (type == null) {
            throw new IllegalArgumentException(String.format("No event type: %02X", value));
        }
        return type;
    }
}
