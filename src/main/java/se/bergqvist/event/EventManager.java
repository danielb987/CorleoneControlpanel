package se.bergqvist.event;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import se.bergqvist.log.Logger;

/**
 * Event manager.
 *
 * @author Daniel Bergqvist
 */
public class EventManager implements Runnable {

    public interface LowLevelEventListener {
        void event(Event event);
    }


    private final Path _path;
    private final LowLevelEventListener _listener;

    public static void create(Path path, LowLevelEventListener listener) {
        EventManager em = new EventManager(path, listener);
        new Thread(em).start();
    }

    private EventManager(Path path, LowLevelEventListener listener) {
        this._path = path;
        this._listener = listener;
    }

    @Override
    public void run() {
        try(InputStream inputStream = new FileInputStream(_path.toFile())) {
            byte[] buffer = new byte[256];
            while (true) {
                int num = inputStream.read(buffer);
                    int index = 0;
                    while (index+16 <= num) {
                        try {
                            Event e = parseEvent(buffer, index);
                            _listener.event(e);
                        } catch (IllegalArgumentException e) {
                            LOG.error(e.getMessage());
                        }

                        index += 16;
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private long getByte(byte[] buffer, int index, int shift)
    {
        return (buffer[index] & 0xFF) << shift;
    }

    private long getWord(byte[] buffer, int index)
    {
        return getByte(buffer, index + 1, 8) + getByte(buffer, index + 0, 0);
    }

    private long getLong(byte[] buffer, int index)
    {
        return getByte(buffer, index + 1, 8) + getByte(buffer, index + 0, 0)
                + getByte(buffer, index + 3, 24) + getByte(buffer, index + 2, 16);
    }

    private Event parseEvent(byte[] buffer, int index) {
        long seconds = getLong(buffer, index);
        long usec = getLong(buffer, index+4);
        EventTypeEnum type = EventTypeEnum.get((int) getWord(buffer, index+8));
        EventEnum event = EventEnum.get(type, (int) getWord(buffer, index+10));
        long value = getLong(buffer, index+12);
        return new Event(seconds, usec, type, event, value);
    }

    public static class Event {
        public final long seconds;
        public final long usec;
        public final EventTypeEnum type;
        public final EventEnum event;
        public final long value;

        private Event(long seconds, long usec, EventTypeEnum type, EventEnum event, long value) {
            this.seconds = seconds;
            this.usec = usec;
            this.type = type;
            this.event = event;
            this.value = value;
        }
    }

    private static final Logger LOG = new Logger(EventManager.class);
}
