package se.bergqvist.controlpanel.icons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;

/**
 * Line icon on control panel.
 *
 * @author Daniel Bergqvist
 */
public class LineIcon extends Icon {

    private final Type _type;
    private final int _width;
    private final int _height;
    private final int _bits;
    private final int _bitClosed;
    private final int _bitThrown;
    private final int _bitClosed2;
    private final int _bitThrown2;
    private final Graphics2D _graphics;
    private final Image _image;

    public static void initialize(Component c) {

        // Empty icon
        addIcon(mapList, new LineIcon(c, Type.Empty, 0b00000000));

        // Line
        addIcon(mapList, new LineIcon(c, Type.Line, 0b00010001));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b00100010));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b01000100));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b10001000));

        addIcon(mapList, new LineIcon(c, Type.Line, 0b00010010));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b00100100));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b01001000));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b10010000));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b00100001));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b01000010));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b10000100));
        addIcon(mapList, new LineIcon(c, Type.Line, 0b00001001));

        // End bumper
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00000001));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00000010));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00000100));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00001000));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00010000));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00100000));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b01000000));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b10000000));

        // EndBumperAndCrossing
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b00110011));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b01010101));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b10011001));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b01100110));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b11001100));
        addIcon(mapList, new LineIcon(c, Type.EndBumperCrossing, 0b10101010));
    }

    private static void addIcon(Map<Type, List<Icon>> map, LineIcon icon) {
        map.get(icon.getType()).add(icon);
        Icon i = _iconMap.computeIfAbsent(icon._type, (x) -> { return new HashMap<>(); }).put(icon._bits, icon);
        if (i != null) throw new IllegalArgumentException(String.format("Icon already in map. type: %s, bits: %8s", icon._type.name(), Integer.toBinaryString(icon._bits)));
    }

    private LineIcon(Component component, Type type, int bits) {
        this(component, type, bits, 0, 0, 0, 0);
    }

    private LineIcon(Component component, Type type, int bits, int bitClosed, int bitThrown, int bitClosed2, int bitThrown2) {
        this._type = type;
        this._width = 1;
        this._height = 1;
        this._bits = bits | bitClosed | bitThrown | bitClosed2 | bitThrown2;
        this._bitClosed = bitClosed;
        this._bitThrown = bitThrown;
        this._bitClosed2 = bitClosed2;
        this._bitThrown2 = bitThrown2;
        _image = component.createImage(_width * SIZE, _height * SIZE);
        _graphics = (Graphics2D) _image.getGraphics();
//        _graphics.setColor(Color.GREEN);
        _graphics.setColor(Color.WHITE);
        _graphics.fillRect(0, 0, SIZE, SIZE);
        _graphics.setColor(Color.BLACK);
        _graphics.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        drawSections(bits);

        _graphics.setColor(Color.GREEN);
        drawSections(bitClosed);
        _graphics.setColor(Color.RED);
        drawSections(bitThrown);

        _graphics.setColor(Color.YELLOW);
        drawSections(bitClosed2);
        _graphics.setColor(Color.BLUE);
        drawSections(bitThrown2);
    }

    private void drawSections(int bits) {
        for (int i=0; i < 8; i++) {
            if ((bits & 0x01) == 1) drawSection(i);
            bits >>= 1;
        }
    }

    private void drawSection(int bit) {
        int x0 = SIZE / 2 + 1;
        int y0 = SIZE / 2 + 1;
        int x;
        int y;
        switch (bit) {
            case 0 -> { x = x0 + SIZE; y = y0; }
            case 1 -> { x = x0 + SIZE; y = y0 + SIZE; }
            case 2 -> { x = x0; y = y0 + SIZE; }
            case 3 -> { x = x0 - SIZE; y = y0 + SIZE; }
            case 4 -> { x = x0 - SIZE; y = y0; }
            case 5 -> { x = x0 - SIZE; y = y0 - SIZE; }
            case 6 -> { x = x0; y = y0 - SIZE; }
            case 7 -> { x = x0 + SIZE; y = y0 - SIZE; }
            default -> throw new IllegalArgumentException("Invalid bit: "+Integer.toString(bit));
        }
        _graphics.drawLine(x0, y0, x, y);
    }

    private void drawEndBumper() {

    }

    @Override
    public Type getType() {
        return _type;
    }

    @Override
    public int getBits() {
        return _bits;
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
        if (_type != Type.Empty) {
            g.drawImage(_image, x, y, null);
        }
    }

    @Override
    public IconData createIconData() {
        return new LineIconData(this);
    }


    private static class LineIconData implements IconData {

        private final LineIcon _icon;

        private LineIconData(LineIcon icon) {
            this._icon = icon;
        }

        @Override
        public void draw(Graphics2D g, int x, int y) {
            _icon.draw(g, x, y);
        }

        @Override
        public void nextState() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Icon getIcon() {
            return _icon;
        }

    }

}
