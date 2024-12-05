package se.bergqvist.log;

/**
 * Logger manager.
 *
 * @author Daniel Bergqvist
 */
public class LoggerManager {

    public static enum Level {
        Error,
        Warning,
        Info,
    }

    private static final LoggerManager _instance = new LoggerManager();

    static LoggerManager get() {
        return _instance;
    }

    void log(Class<?> clazz, Level level, String msg) {
        System.out.format("%s: %s  %s%n", level, msg, clazz.getName());
    }

}
