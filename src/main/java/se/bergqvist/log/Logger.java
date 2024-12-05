package se.bergqvist.log;

/**
 * Logger.
 * @author Daniel Bergqvist
 */
public class Logger {

    private final Class<?> _clazz;

    public Logger(Class<?> clazz) {
        this._clazz = clazz;
    }

    public void error(String msg) {
        LoggerManager.get().log(_clazz, LoggerManager.Level.Error, msg);
    }

}
