package se.bergqvist.config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;
import se.bergqvist.corleonecontrolpanel.MainJFrame;
import se.bergqvist.input.InputDevices;
import se.bergqvist.touch.TouchManager.EventListener;
import se.bergqvist.touch.TouchManager.TouchListener;

/**
 * The configuration.
 *
 * @author Daniel Bergqvist
 */
public class Config {

    private static final String FILENAME = "/home/pi/Controlpanel/controlpanel.xml";

    private static final Config INSTANCE = new Config();

    private final List<ScreenConfig> _screenConfigs = new ArrayList<>();

    private final List<TouchscreenConfig> _touchscreenConfigs =
            new InputDevices().getInputDevices();


    public static Config get() {
        return INSTANCE;
    }

    public String getFilename() {
        return FILENAME;
    }

    public void addScreenConfig(ScreenConfig sc) {
        _screenConfigs.add(sc);
    }

    public List<ScreenConfig> getScreenConfigs() {
        return Collections.unmodifiableList(_screenConfigs);
    }

    public Path getPathForTouchscreen(int pos) {
        for (TouchscreenConfig tc : _touchscreenConfigs) {
            if (tc._position == pos) {
                return tc._path;
            }
        }
        for (TouchscreenConfig tc : _touchscreenConfigs) {
            if (tc._position == -1) {
                tc._position = pos;
                return tc._path;
            }
        }
        return null;    // Couldn't find path for touchscreen
    }

    public void setPathForTouchScreen(Path path, int pos) {
        for (TouchscreenConfig tc : _touchscreenConfigs) {
            if (tc._path.equals(path)) {
                tc._position = pos;
            }
        }
    }

    public boolean switchTouchscreen() {
        Map<Integer, Map.Entry<Integer, TouchListener>> listeners = new HashMap<>();
        int count = 0;
        for (TouchscreenConfig tc : _touchscreenConfigs) {
            listeners.put(count++, new HashMap.SimpleEntry<>(tc._position, tc._listener.getTouchListener()));
        }
        if (_touchscreenConfigs.size() > 1) {
            _touchscreenConfigs.get(0)._position = listeners.get(1).getKey();
            _touchscreenConfigs.get(0)._listener.setTouchListener(listeners.get(1).getValue());
            _touchscreenConfigs.get(1)._position = listeners.get(0).getKey();
            _touchscreenConfigs.get(1)._listener.setTouchListener(listeners.get(0).getValue());
            return false;
        } else {
            return true;
        }
    }

    public void setTouchscreen(EventListener listener, int x) {
        Map<Integer, TouchListener> listeners = new HashMap<>();
        int count = 0;
        for (TouchscreenConfig tc : _touchscreenConfigs) {
            if (tc._position == x && tc._listener == listener) {
                return;
            }
            listeners.put(count++, tc._listener.getTouchListener());
        }
        _touchscreenConfigs.get(0)._listener.setTouchListener(listeners.get(1));
        _touchscreenConfigs.get(1)._listener.setTouchListener(listeners.get(0));
    }

    public void setListenerForTouchscreen(Path path, EventListener listener) {
        for (TouchscreenConfig tc : _touchscreenConfigs) {
            if (tc._path.equals(path)) {
                tc._listener = listener;
            }
        }
    }

    public Element getXml() {
        Element configuration = new Element("Configuration");
        Element screenConfigs = new Element("TouchscreenConfigs");
        for (TouchscreenConfig sc : _touchscreenConfigs) {
            Element screenConfig = new Element("TouchscreenConfig");
            screenConfig.addContent(new Element("Position").addContent(Integer.toString(sc._position)));
            screenConfig.addContent(new Element("DevPath").addContent(sc._devPath));
            screenConfigs.addContent(screenConfig);
        }
        configuration.addContent(screenConfigs);
        return configuration;
    }

    public void loadXml(Element configuration) {
        Element touchscreenConfigs = configuration.getChild("TouchscreenConfigs");
        List<Element> touchscreenConfig = touchscreenConfigs.getChildren("TouchscreenConfig");
        for (Element tcElement : touchscreenConfig) {
            int position = Integer.parseInt(tcElement.getChildTextTrim("Position"));
            String devPath = tcElement.getChildTextTrim("DevPath");
            for (TouchscreenConfig tc : _touchscreenConfigs) {
                if (tc._devPath.equals(devPath)) {
                    tc._position = position;
                }
                System.out.format("Old: Pos: %d, devPath: %s%n", tc._position, tc._devPath);
            }
            System.out.format("Pos: %d, devPath: %s%n", position, devPath);
        }
    }


    public static class ScreenConfig {
        private final int _position;
        private final MainJFrame _frame;
        private final TouchListener _touchListener;
        private String _devPath;    // For example /sys/devices/platform/axi/1000120000.pcie/1f00300000.usb/xhci-hcd.1/usb3/3-1/3-1.2/3-1.2:1.0/0003:27C0:0858.000B/input/input22/event5

        public ScreenConfig(int position, MainJFrame frame, TouchListener touchListener) {
            this._position = position;
            this._frame = frame;
            this._touchListener = touchListener;
        }

        public int getPosition() {
            return _position;
        }

        public MainJFrame getFrame() {
            return _frame;
        }

        public TouchListener getTouchListener() {
            return _touchListener;
        }

        public String getDevPath() {
            return _devPath;
        }

        public void setDevPath(String devPath) {
            this._devPath = devPath;
        }
    }


    public static class TouchscreenConfig {
        private final Path _path;       // For example /dev/input/event5
        private final String _devPath;  // For example /sys/devices/platform/axi/1000120000.pcie/1f00300000.usb/xhci-hcd.1/usb3/3-1/3-1.2/3-1.2:1.0/0003:27C0:0858.000B/input/input22/event5
        private int _position;
        private EventListener _listener;

        public TouchscreenConfig(Path path, String devPath) {
            this._path = path;
            this._devPath = devPath;
            this._position = -1;
        }
    }
}
