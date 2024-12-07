package se.bergqvist.config;

/**
 * The configuration.
 *
 * @author Daniel Bergqvist
 */
public class Config {

    private static final String FILENAME = "/home/pi/Controlpanel/controlpanel.xml";

    private static final Config INSTANCE = new Config();

    public static Config get() {
        return INSTANCE;
    }

    public String getFilename() {
        return FILENAME;
    }

}
