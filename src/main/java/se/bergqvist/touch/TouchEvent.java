package se.bergqvist.touch;

/**
 * Touch event.
 *
 * @author Daniel Bergqvist
 */
public class TouchEvent {

    private final TouchEnum _type;
    private final int _x;
    private final int _y;

    TouchEvent(TouchEnum type, int x, int y) {
        this._type = type;
        this._x = x;
        this._y = y;
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

}
