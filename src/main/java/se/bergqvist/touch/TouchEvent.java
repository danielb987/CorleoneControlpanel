package se.bergqvist.touch;

import se.bergqvist.touch.TouchManager.EventListener;

/**
 * Touch event.
 *
 * @author Daniel Bergqvist
 */
public class TouchEvent {

    private final TouchEnum _type;
    private final int _x;
    private final int _y;
    private final EventListener _listener;

    TouchEvent(TouchEnum type, int x, int y, EventListener listener) {
        this._type = type;
        this._x = x;
        this._y = y;
        this._listener= listener;
    }

    public TouchEnum getType() {
        return _type;
    }

    public int getX() {
        return _x;
    }

    public int getY() {
        return _y;
    }

    public EventListener getListener() {
        return _listener;
    }

}
