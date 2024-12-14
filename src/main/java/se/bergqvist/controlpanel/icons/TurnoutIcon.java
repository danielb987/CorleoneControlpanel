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
 * Icon on control panel.
 *
 * @author Daniel Bergqvist
 */
public class TurnoutIcon extends Icon {

    private final Type _type;
    private final int _width;
    private final int _height;
    private final int _bits;
    private final int _bitClosed;
    private final int _bitThrown;
    private final int _bitClosed2;
    private final int _bitThrown2;
    private final int _numStates;
//    private final Graphics2D _graphics;
    private final List<Image> _images = new ArrayList<>();

    public static void initialize(Component c) {

        // Turnout
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00000001, 0b00010000, 0b00100000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00000010, 0b00100000, 0b01000000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00000100, 0b01000000, 0b10000000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00001000, 0b10000000, 0b00000001));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00010000, 0b00000001, 0b00000010));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00100000, 0b00000010, 0b00000100));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b01000000, 0b00000100, 0b00001000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b10000000, 0b00001000, 0b00010000));

        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00000001, 0b00010000, 0b00001000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00000010, 0b00100000, 0b00010000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00000100, 0b01000000, 0b00100000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00001000, 0b10000000, 0b01000000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00010000, 0b00000001, 0b10000000));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b00100000, 0b00000010, 0b00000001));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b01000000, 0b00000100, 0b00000010));
        addIcon(mapList, new TurnoutIcon(c, Type.Turnout, 0b10000000, 0b00001000, 0b00000100));

        // Wye
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b00000001, 0b00001000, 0b00100000));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b00000010, 0b00010000, 0b01000000));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b00000100, 0b00100000, 0b10000000));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b00001000, 0b01000000, 0b00000001));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b00010000, 0b10000000, 0b00000010));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b00100000, 0b00000001, 0b00000100));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b01000000, 0b00000010, 0b00001000));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0b10000000, 0b00000100, 0b00010000));

        // Slip
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0, 0b00010000, 0b00100000, 0b00000001, 0b00000010, 4));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0, 0b00100000, 0b01000000, 0b00000010, 0b00000100, 4));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0, 0b01000000, 0b10000000, 0b00000100, 0b00001000, 4));
        addIcon(mapList, new TurnoutIcon(c, Type.WyeSlip, 0, 0b10000000, 0b00000001, 0b00001000, 0b00010000, 4));
    }

    private static void addIcon(Map<Type, List<Icon>> map, TurnoutIcon icon) {
        map.get(icon.getType()).add(icon);
        Icon i = _iconMap.computeIfAbsent(icon._type, (x) -> { return new HashMap<>(); }).put(icon._bits, icon);
        if (i != null) throw new IllegalArgumentException(String.format("Icon already in map. type: %s, bits: %8s", icon._type.name(), Integer.toBinaryString(icon._bits)));
//        System.out.format("Bits: 0b%s, %s%n", String.format("%8s",Integer.toBinaryString(icon._bits)).replace(" ", "0"), icon._type.name());
    }

    private TurnoutIcon(Component component, Type type, int bits, int bitClosed, int bitThrown) {
        this(component, type, bits, bitClosed, bitThrown, 0, 0, 2);
    }

    private TurnoutIcon(Component component, Type type, int bits, int bitClosed, int bitThrown, int bitClosed2, int bitThrown2, int numStates) {
        this._type = type;
        this._width = 1;
        this._height = 1;
        this._bits = bits | bitClosed | bitThrown | bitClosed2 | bitThrown2;
        this._bitClosed = bitClosed;
        this._bitThrown = bitThrown;
        this._bitClosed2 = bitClosed2;
        this._bitThrown2 = bitThrown2;
        this._numStates = numStates;
        for (int state=-1; state < _numStates; state++) {
            Image image = component.createImage(_width * SIZE, _height * SIZE);
            _images.add(image);
            Graphics2D graphics = (Graphics2D) image.getGraphics();
    //        _graphics.setColor(Color.GREEN);
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, SIZE, SIZE);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            drawSections(graphics, bits, true);

            if (state == -1) graphics.setColor(Color.GREEN);
            drawSections(graphics, bitClosed, state == -1 || state == 0);
            if (state == -1) graphics.setColor(Color.RED);
            drawSections(graphics, bitThrown, state == -1 || state == 1);

            if (state == -1) graphics.setColor(Color.YELLOW);
            drawSections(graphics, bitClosed2, true);
            if (state == -1) graphics.setColor(Color.BLUE);
            drawSections(graphics, bitThrown2, true);
        }
    }

    private void drawSections(Graphics2D graphics, int bits, boolean full) {
        for (int i=0; i < 8; i++) {
            if ((bits & 0x01) == 1) drawSection(graphics, i, full);
            bits >>= 1;
        }
    }

    private void drawSection(Graphics2D graphics, int bit, boolean full) {
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
        if (!full) {
            x0 = (int) Math.round((x+x0*2.0) / 3);
            y0 = (int) Math.round((y+y0*2.0) / 3);
        }
        graphics.drawLine(x0, y0, x, y);
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
    public void draw(Graphics2D g, int x, int y, int state) {
        g.drawImage(_images.get(state), x, y, null);
    }

    @Override
    public void drawFrame(Graphics2D g, int x, int y) {
        g.drawRect(x-1, y-1, SIZE+1, SIZE+1);
    }

    @Override
    public boolean isHit(int x, int y, int hitX, int hitY) {
        return hitX >= x && hitX <= x + SIZE && hitY >= y && hitY <= y + SIZE;
    }

    @Override
    public IconData createIconData() {
        return new TurnoutIconData(this);
    }


    private static class TurnoutIconData extends AbstractIconData {

        private final TurnoutIcon _icon;

        private int _state;

        private TurnoutIconData(TurnoutIcon icon) {
            this._icon = icon;
        }

        @Override
        public void draw(Graphics2D g, int x, int y) {
            _icon.draw(g, x, y, _state+1);
        }

        @Override
        public int getState() {
            return _state;
        }

        @Override
        public void nextState() {
            _state++;
            if (_state >= _icon._numStates) {
                _state = 0;
            }
        }

        @Override
        public Icon getIcon() {
            return _icon;
        }

    }

}
