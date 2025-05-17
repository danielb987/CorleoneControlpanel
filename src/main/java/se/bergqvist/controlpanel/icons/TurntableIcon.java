package se.bergqvist.controlpanel.icons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.QuadCurve2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;
import se.bergqvist.corleonecontrolpanel.CorleoneControlpanel;
import se.bergqvist.turntable.Turntable;
import se.bergqvist.turntable.Turntable.TurntableListener;

/**
 * Icon on control panel.
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
public class TurntableIcon extends Icon {

    // 24 positions
    // 360 degrees
    // 360/24 = 15
    private static final int[] POSITIONS = new int[]{
          0,  20,  35,  45,  55,  70,
         90, 110, 124, 135, 146, 160,
        180, 200, 214, 225, 236, 250,
        270, 290, 305, 315, 326, 340,
    };

    private static final int NUM = 3;
    private static final int MY_SIZE = NUM * Icon.RASTER_SIZE - Icon.RASTER_MARGIN;
    private static final int SUB = 10;

    private final Component _component;
    private final Type _type;
    private final int _connectingBits;
    private final int _width;
    private final int _height;
    private final Graphics2D _graphics;
    private final Image _image;


    public static void initialize(Component c) {
        addIcon(mapList, new TurntableIcon(c, Type.WyeSlip));
    }

    private static void addIcon(Map<Type, List<Icon>> map, TurntableIcon icon) {
        map.get(icon._type).add(icon);
        Icon i = _iconMap.computeIfAbsent(icon._type, (x) -> { return new HashMap<>(); }).put(0, icon);
        if (i != null) throw new IllegalArgumentException(String.format("Icon already in map. type: %s, bits: 0", icon._type.name()));
    }

    private TurntableIcon(Component component, Type type) {
        this(component, type, 0x00FFFFFF);
    }

    private TurntableIcon(Component component, Type type, int connectingBits) {
        this._component = component;
        this._type = type;
        this._connectingBits = connectingBits;
        this._width = 3;
        this._height = 3;
        _image = component.createImage(MY_SIZE, MY_SIZE);
        _graphics = (Graphics2D) _image.getGraphics();
//        _graphics.setColor(Color.GREEN);
        _graphics.setColor(Color.WHITE);
        _graphics.fillRect(0, 0, MY_SIZE, MY_SIZE);
        _graphics.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        _graphics.setColor(Color.RED);
//        _graphics.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
//        int angle = 144;    // 14,4 degrees
        double w = MY_SIZE;
        double h = MY_SIZE;
        double w2 = MY_SIZE / 2.0;
        double h2 = MY_SIZE / 2.0;

        int x1 = (int) Math.round(w2);
        int y1 = (int) Math.round(h2);

        _graphics.setColor(Color.BLACK);
        for (int i=0; i < POSITIONS.length; i++) {
            System.out.format("%2d: %8x, %8x, %8x, %b%n", i, connectingBits, 1 << i, connectingBits & (1 << i), (connectingBits & (1 << i)) != 0);
            if ((connectingBits & (1 << i)) != 0) {
                int x = (int) Math.round(w2 + Math.cos(Math.toRadians(POSITIONS[i]))*w);
                int y = (int) Math.round(h2 + Math.sin(Math.toRadians(POSITIONS[i]))*h);
                _graphics.drawLine(x, y, x1, y1);
            }
        }

        _graphics.setColor(Color.WHITE);
        _graphics.fillOval(SUB, SUB, _width*RASTER_SIZE-SUB*2, _height*RASTER_SIZE-SUB*2);
        _graphics.setColor(Color.BLACK);
        _graphics.drawOval(SUB, SUB, _width*RASTER_SIZE-SUB*2, _height*RASTER_SIZE-SUB*2);
    }

    @Override
    public Icon createIcon(int connectingBits) {
        return new TurntableIcon(_component, _type, connectingBits);
    }

    @Override
    public Type getType() {
        return _type;
    }

    @Override
    public int getBits() {
        return 0;   // We only have one icon of this type
    }

    @Override
    public void draw(Graphics2D g, int x, int y, int state) {
        g.drawImage(_image, x, y, null);
    }

    @Override
    public void drawFrame(Graphics2D g, int x, int y) {
        g.drawRect(x-1, y-1, MY_SIZE+1, MY_SIZE+1);
    }

    @Override
    public boolean isHit(int x, int y, int hitX, int hitY) {
        return hitX >= x && hitX <= x + MY_SIZE && hitY >= y && hitY <= y + MY_SIZE;
    }

    @Override
    public IconData createIconData() {
        return new TurntableIconData(this);
    }


    private enum Status {
        Track,
        NoTrack,
        Moving
    }


    private static class TurntableIconData extends AbstractIconData
            implements TurntableListener {

        private final TurntableIcon _icon;
        private final Map<Integer, Integer> _headTrackNoMap = new HashMap<>();
        private final Map<Integer, Integer> _tailTrackNoMap = new HashMap<>();
        private Status _status = Status.NoTrack;
        private int _currentTrack;
        private boolean _headOrTail = true;


        private TurntableIconData(TurntableIcon icon) {
            this._icon = icon;
            Turntable.get().addListener(this);
        }

        private Font _noTrackFont;
        private Font _movingFont;

        @Override
        public void draw(Graphics2D g, int x, int y) {
            _icon.draw(g, x, y, -1);
            Font oldFont = g.getFont();
            switch (_status) {
                case NoTrack -> {
                    if (_noTrackFont == null) {
                        _noTrackFont = new Font("Cantarell", Font.BOLD, 100);
                    }   g.setFont(_noTrackFont);
                    g.drawString("?", x+50, y+105);
                }
                case Moving -> {
                    if (_movingFont == null) {
                        _movingFont = new Font("Cantarell", Font.BOLD, 40);
                    }   g.setFont(_movingFont);
                    g.drawString("Move", x+20, y+80);
                }
                case Track -> {
                    int head = -1;
                    int tail = -1;
                    for (int i=0; i < POSITIONS.length; i++) {
//                        System.out.format("%2d: %8x, %8x, %8x, %b%n", i, connectingBits, 1 << i, connectingBits & (1 << i), (connectingBits & (1 << i)) != 0);
                        Integer headTrack = _headTrackNoMap.get(i);
                        Integer tailTrack = _tailTrackNoMap.get(i);
                        System.err.format("track: %s, _currentTrack: %d%n", headTrack, _currentTrack);
                        if (headTrack != null && headTrack == _currentTrack) {
                            head = i;
                        }
                        if (tailTrack != null && tailTrack == _currentTrack) {
                            tail = i;
                        }
                    }
                    if (head != -1 && tail != -1) {
                        System.out.format("Head: %d, Tail: %d, Track: %d%n", head, tail, _currentTrack);
                        double w = MY_SIZE;
                        double h = MY_SIZE;
                        double w2 = MY_SIZE / 2.0;
                        double h2 = MY_SIZE / 2.0;
                        int x1 = x + (int) Math.round(w2 + Math.cos(Math.toRadians(POSITIONS[head]))*(w2-SUB));
                        int y1 = y + (int) Math.round(h2 + Math.sin(Math.toRadians(POSITIONS[head]))*(h2-SUB));
                        int xx = x + (int) Math.round(w2 + Math.cos(Math.toRadians(POSITIONS[tail]))*(w2-SUB));
                        int yy = y + (int) Math.round(h2 + Math.sin(Math.toRadians(POSITIONS[tail]))*(h2-SUB));
                        g.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        // Draw a beziercurve
                        Shape s = new QuadCurve2D.Double(x1, y1, x+w2, y+h2, xx, yy);
                        g.draw(s);
                        int r = 20;
                        g.setColor(Color.red);
                        if (_headOrTail) {
                            g.fillOval(x1-r/2,y1-r/2,r,r);
                        } else {
                            g.fillOval(xx-r/2,yy-r/2,r,r);
                        }
                        System.err.format("DrawLine: %d, %d, %d, %d%n", xx, yy, x1, y1);
                    }
                }
                default -> {
                    throw new RuntimeException(String.format("_status has unknown value: %s", _status));
                }
            }
            g.setFont(oldFont);

            if (1 == 1) return;


            String ss = String.format("%s: %d", _status.name(), _currentTrack);
//            g.setColor(Color.WHITE);
//            g.fillRect(0, 0, 1600, 1200);
            g.setFont(new Font("Cantarell", Font.BOLD, 22));
            g.drawString(String.format("%s: %d", _status.name(), _currentTrack), x+50, y+MY_SIZE/2);

            if (1 == 1) return;

            String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

            g.setColor(Color.BLACK);
            int xx = 0;
            int yy = 0;
            for (String f : fonts) {
                if ("Droid Sans Fallback".equals(f)) continue;

                System.out.println(f);
//                g.setFont(new Font(f, Font.BOLD, 22));
                g.setFont(new Font(f, Font.BOLD, 30));
                g.drawString(ss+" "+f, xx, yy + 100);
                yy += 50;
                if (yy > 800) {
                    yy = 0;
                    xx += 600;
                }
            }
        }

        @Override
        public int getState() {
            return 0;
        }

        @Override
        public void nextState() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Icon getIcon() {
            return _icon;
        }

        @Override
        public Element getXml(int x, int y) {
            Element e = super.getXml(x, y);
            e.setAttribute("connectingBits", Integer.toHexString(_icon._connectingBits));

            Map<Integer, Map.Entry<Integer, Integer>> trackMap = new HashMap<>();
            for (var entry : _headTrackNoMap.entrySet()) {
                trackMap.put(entry.getValue(), new HashMap.SimpleEntry<>(entry.getKey(), -1));
            }
            for (var entry : _tailTrackNoMap.entrySet()) {
                Map.Entry<Integer, Integer> headTailEntry = trackMap.get(entry.getValue());
                headTailEntry.setValue(entry.getKey());
            }

//            for (var entry : _headTrackNoMap.entrySet()) {
            for (var entry : trackMap.entrySet()) {
                Element track = new Element("Track");
                track.setAttribute("head", Integer.toString(entry.getValue().getKey()));
                track.setAttribute("tail", Integer.toString(entry.getValue().getValue()));
                track.setAttribute("trackNo", Integer.toString(entry.getKey()));
                e.addContent(track);
            }
            return e;
        }

        @Override
        public void loadXml(Element iconData) {
            super.loadXml(iconData);
            for (Element track : iconData.getChildren("Track")) {
                int head = Integer.parseInt(track.getAttributeValue("head"));
                int tail = Integer.parseInt(track.getAttributeValue("tail"));
                int trackNo = Integer.parseInt(track.getAttributeValue("trackNo"));
                _headTrackNoMap.put(head, trackNo);
                _tailTrackNoMap.put(tail, trackNo);
            }
        }

        @Override
        public void info(int speed, boolean direction, int pos, int track, boolean head) {
            System.out.format("Speed: %d, Track: %d, Head: %b%n", speed, track, head);
            Status oldStatus = _status;
            int oldTrack = _currentTrack;
            boolean oldHeadOrTail = _headOrTail;
            if (speed == 0 && track != -1) {
                // Turntable at a track
                _status = Status.Track;
                _currentTrack = track;
                _headOrTail = head;
            } else if (speed != 0) {
                // Turntable moving
                _status = Status.Moving;
            } else {
                // Turntable stopped but not at a track
                _status = Status.NoTrack;
            }

            if (_status != oldStatus || _currentTrack != oldTrack || oldHeadOrTail) {
                CorleoneControlpanel.repaint();
            }
        }

        public void setHeadTrackNo(int head, int trackNo) {
            _headTrackNoMap.put(head, trackNo);
        }

        public int getHeadTrackNo(int head) {
            return _headTrackNoMap.get(head);
        }

        public void setTailTrackNo(int tail, int trackNo) {
            _tailTrackNoMap.put(tail, trackNo);
        }

        public int getTailTrackNo(int tail) {
            return _tailTrackNoMap.get(tail);
        }

    }

}
