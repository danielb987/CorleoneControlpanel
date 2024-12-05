package se.bergqvist.touch;

import java.nio.file.Path;
import static se.bergqvist.event.EventEnum.AbsoluteMiddleTouchX;
import static se.bergqvist.event.EventEnum.AbsoluteMiddleTouchY;
import se.bergqvist.event.EventManager;
import se.bergqvist.event.EventManager.Event;
import se.bergqvist.event.EventManager.EventListener;

/**
 * Touch manager.
 *
 * @author Daniel Bergqvist
 */
public class TouchManager {

//    public static final int DRAG_DELAY = 10; // 300 us
    public static final int DRAG_DELAY = 200; // 200 us
//    public static final int DRAG_DELAY = 300; // 300 us


    public interface TouchListener {
        void event(TouchEvent event);
    }


    public enum TouchState {
        None,
        Click,
        Drag;
    }


    public static void create(Path path, TouchListener touchListener) {
        MyListener eventListener = new MyListener(touchListener);
        TouchManager tm = new TouchManager(path, eventListener);
    }

    private TouchManager(Path path, EventListener eventListener) {
        EventManager.create(path, eventListener);
    }


    private static class MyListener implements EventListener {

        private final TouchListener _touchListener;

        private TouchState _touchState = TouchState.None;
        private boolean _isButtonDownEvent;
        private boolean _isButtonUpEvent;
        private boolean _isButtonDown;
        private int _x;
        private int _y;
        private boolean _hasMoved;
        private long _firstEventTime = System.currentTimeMillis();
        private long _lastEventTime = System.currentTimeMillis();

        private MyListener(TouchListener touchListener) {
            this._touchListener = touchListener;
        }

        @Override
        public synchronized void event(Event event) {
            switch (event.event) {
                case Separator -> {
                    if (!_isButtonDown && !_isButtonDownEvent && !_isButtonUpEvent) {
                        return;
                    }
                    if (_isButtonDownEvent) {
                        _isButtonDown = true;
                        _touchState = TouchState.Click;
                        _firstEventTime = System.currentTimeMillis();
                        _isButtonDownEvent = false;
                    } else if (_isButtonUpEvent) {
                        TouchEvent evt = new TouchEvent(_touchState == TouchState.Drag ? TouchEnum.EndDrag : TouchEnum.Click, _x, _y);
                        _touchListener.event(evt);
                        _isButtonDown = false;
                        _touchState = TouchState.None;
                        _isButtonUpEvent = false;
                    } else if (_touchState == TouchState.Click && DRAG_DELAY <= (_lastEventTime - _firstEventTime)) {
                        _touchState = TouchState.Drag;
                        TouchEvent evt = new TouchEvent(TouchEnum.StartDrag, _x, _y);
                        _touchListener.event(evt);
                    } else if (_touchState == TouchState.Drag && _hasMoved) {
                        TouchEvent evt = new TouchEvent(TouchEnum.Drag, _x, _y);
                        _touchListener.event(evt);
                    }
                    _lastEventTime = System.currentTimeMillis();
                    _hasMoved = false;
                }
                case ButtonTouch -> {
                    if (event.value == 1) {
                        _isButtonDownEvent = true;
                    } else if (event.value == 0) {
                        _isButtonUpEvent = true;
                    }
                }
                case AbsoluteX -> {
                    _x = (int) event.value;
                    _hasMoved = true;
                }
                case AbsoluteY -> {
                    _y = (int) event.value;
                    _hasMoved = true;
                }
                case AbsoluteMiddleTouchSlot -> {
                    // Happens when using two fingers at once
                }
            }
        }

    }

}
