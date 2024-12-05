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
        0,
        20,
        35,
        45,
        55,
        70,
        90,
        110,
        124,
        135,
        146,
        160,
        180,
        200,
        214,
        225,
        236,
        250,
        270,
        290,
        305,
        315,
        326,
        340,
    };

    private static final int NUM = 3;
    private static final int MY_SIZE = NUM * Icon.RASTER_SIZE - RASTER_MARGIN;

    private final Type _type;
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
        this._type = type;
        this._width = 3;
        this._height = 3;
        _image = component.createImage(MY_SIZE, MY_SIZE);
        _graphics = (Graphics2D) _image.getGraphics();
//        _graphics.setColor(Color.GREEN);
        _graphics.setColor(Color.WHITE);
        _graphics.fillRect(0, 0, MY_SIZE, MY_SIZE);
        _graphics.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
/*
        _graphics.setColor(Color.BLACK);
        for (int x=0; x < 3; x++) {
            _graphics.drawLine(x*SIZE, 0, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(x*SIZE+SIZE/2, 0, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(x*SIZE+SIZE-1, 0, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(x*SIZE, _height*SIZE-1, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(x*SIZE+SIZE/2, _height*SIZE-1, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(x*SIZE+SIZE-1, _height*SIZE-1, _width*SIZE/2+1, _height*SIZE/2+1);
        }
        for (int y=0; y < 3; y++) {
            _graphics.drawLine(0, y*SIZE, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(0, y*SIZE+SIZE/2, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(0, y*SIZE+SIZE-1, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(_width*SIZE-1, y*SIZE, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(_width*SIZE-1, y*SIZE+SIZE/2, _width*SIZE/2+1, _height*SIZE/2+1);
            _graphics.drawLine(_width*SIZE-1, y*SIZE+SIZE-1, _width*SIZE/2+1, _height*SIZE/2+1);
        }
*/
        int sub = 10;
//        int sub = 60;
        _graphics.setColor(Color.WHITE);
        _graphics.fillOval(sub, sub, _width*RASTER_SIZE-sub*2, _height*RASTER_SIZE-sub*2);
        _graphics.setColor(Color.BLACK);
        _graphics.drawOval(sub, sub, _width*RASTER_SIZE-sub*2, _height*RASTER_SIZE-sub*2);

//        _graphics.drawLine(0, _height*RASTER_SIZE-RASTER_MARGIN, _width*RASTER_SIZE-RASTER_MARGIN, 0);

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
            int x = (int) Math.round(w2 + Math.cos(Math.toRadians(POSITIONS[i]))*w);
            int y = (int) Math.round(h2 + Math.sin(Math.toRadians(POSITIONS[i]))*h);
            _graphics.drawLine(x, y, x1, y1);
        }

    }

    @Override
    public Type getType() {
        return _type;
    }

    @Override
    public void draw(Graphics2D g, int x, int y) {
        g.drawImage(_image, x, y, null);
    }

}
