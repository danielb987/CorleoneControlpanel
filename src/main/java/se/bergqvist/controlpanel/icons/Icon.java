package se.bergqvist.controlpanel.icons;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An icon.
 *
 * Bits are counted clockwise from east.
 * Bit 0: East
 * Bit 1: South east
 * Bit 2: South
 * Bit 3: South west
 * Bit 4: West
 * Bit 5: North west
 * Bit 6: North
 * Bit 7: North east
 *
 * @author Daniel Bergqvist
 */
public abstract class Icon {

    public enum Type {

        Empty("Empty"),
        Line("Lines"),
        EndBumperCrossing("End bumpers and crossings"),
        Turnout("Turnouts"),
        WyeSlip("Wyes and slips");

        private final String _descr;

        private Type(String descr) {
            this._descr = descr;
        }

        @Override
        public String toString() {
            return _descr;
        }
    }

    public static final int SIZE = 45; // pixels
//    public static final int SIZE = 47; // pixels
    public static final int RASTER_MARGIN = 1;
    public static final int RASTER_SIZE = SIZE + RASTER_MARGIN; // pixels

    protected static final Map<Type, Map<Integer, Icon>> _iconMap = new HashMap<>();
    protected static final Map<Type, List<Icon>> _iconByTypeList = new HashMap<>();
    protected static final Map<Type, List<Icon>> mapList = new HashMap<>();

    static {
        for (Type t: Type.values()) {
            List<Icon> list = new ArrayList<>();
            mapList.put(t, list);
            _iconByTypeList.put(t, Collections.unmodifiableList(list));
        }
    }

    public static Icon get(Type type, int bits) {
        return _iconMap.get(type).get(bits);
    }

    public static List<Icon> get(Type type) {
        return _iconByTypeList.get(type);
    }

    public abstract Type getType();

    public abstract void draw(Graphics2D g, int x, int y);

}
