package se.bergqvist.controlpanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A button.
 *
 * @author Daniel Bergqvist
 */
public class Button {

    Font FONT = new Font("Verdana", Font.PLAIN, 30);
//    Font FONT = new Font("Verdana", Font.BOLD, 20);

    private final String _caption;
    private final int _x;
    private final int _y;
    private final int _w;
    private final int _h;
    private final int _cx;
    private final int _cy;

    public Button(Graphics g, String caption, int x, int y, int w, int h) {
        g.setFont(FONT);
        FontMetrics fm = g.getFontMetrics();
        int fontHeight = fm.getAscent() + fm.getDescent();

        this._caption = caption;
        this._x = x;
        this._y = y;
        this._w = w;
        this._h = h;
        this._cx = (int) Math.round(x + (w - fm.stringWidth(caption)) / 2.0);
        this._cy = (int) Math.round(y + (h - fontHeight) / 2.0) + fm.getAscent();
    }

    public void draw(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setFont(FONT);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(_x, _y, _w, _h);
        g.setColor(Color.BLACK);
        g.drawRect(_x, _y, _w, _h);
        g.drawString(_caption, _cx, _cy);
    }

    public boolean isHit(int x, int y) {
        return (x >= _x && x <= _x+_w && y >= _y && y <= _y+_h);
    }
}
