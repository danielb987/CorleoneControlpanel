package se.bergqvist.controlpanel;

import se.bergqvist.controlpanel.icons.LineIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import org.jdom2.Element;
import se.bergqvist.controlpanel.icons.Icon;
import se.bergqvist.controlpanel.icons.IconData;
import se.bergqvist.log.Logger;

/**
 * Control panel.
 *
 * @author Daniel Bergqvist
 */
public final class ControlPanel {

    private static final int RASTER_X0 = 20;
//    private static final int RASTER_Y0 = 40 - Icon.RASTER_SIZE;
    private static final int RASTER_Y0 = 60;
    private static final int RASTER_NUM_X = 41;
    private static final int RASTER_NUM_Y = 11;
    private static final int RASTER_MAX_X = RASTER_X0 + RASTER_NUM_X * Icon.RASTER_SIZE;
    private static final int RASTER_MAX_Y = RASTER_Y0 + RASTER_NUM_Y * Icon.RASTER_SIZE;

    private final IconData[][] iconData = new IconData[RASTER_NUM_X][RASTER_NUM_Y];

    private final List<IconWithPosition> _iconPalette = new ArrayList<>();
    private IconWithPosition _selectedIcon;

    private boolean _drawOldControlPanel = false;
    private boolean _drawOldControlPanelAfter = false;
    private boolean _drawNewControlPanel = true;
    private boolean _editControlPanel = false;

    public static ControlPanel get() {
        return GET_INSTANCE.INSTANCE;
    }

    public ControlPanel() {
        for (int y=0; y < RASTER_NUM_Y; y++) {
            for (int x=0; x < RASTER_NUM_X; x++) {
                iconData[x][y] = Icon.get(Icon.Type.Empty).get(0).createIconData();
            }
        }
    }

    private void drawOldControlpanel(Graphics2D g) {
        Stroke capButtStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        Stroke capRoundStroke = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setColor(Color.BLACK);
        g.setStroke(capRoundStroke);
    }

    private Point getIconPosition(int x, int y) {
        int px = RASTER_X0 + Icon.RASTER_MARGIN + x * Icon.RASTER_SIZE;
        int py = RASTER_Y0 + Icon.RASTER_MARGIN + y * Icon.RASTER_SIZE;
        return new Point(px,py);
    }

    public void draw(Graphics2D g) {

        Color oldColor = g.getColor();
        Stroke oldStroke = g.getStroke();
/*
        g.setColor(Color.BLUE);
        for (int i=0; i < 40; i++) {
            int offset = 25;
            g.drawLine(i*Icon.RASTER_SIZE+offset, 0, i*Icon.RASTER_SIZE+offset, 1080);
        }
        for (int i=0; i < 20; i++) {
//            int offset = 17 + 23;
            int offset = 40;
            g.drawLine(0, i*Icon.RASTER_SIZE+offset, 1920, i*Icon.RASTER_SIZE+offset);
        }
*/
        if (_drawOldControlPanel) {
            drawOldControlpanel(g);
        }


        int count;
        if (_editControlPanel) {
            Stroke stroke = new BasicStroke(1.0f);
            g.setStroke(stroke);

            int y = 500;
            for (LineIcon.Type type : LineIcon.Type.values()) {
                count = 0;
    //            y += 50;
                y += Icon.RASTER_SIZE;
    //            int i = 0;
                for (Icon icon : Icon.get(type)) {
                    int x = 100 + count++ * Icon.RASTER_SIZE;
                    icon.drawFrame(g, x, y);
                    icon.draw(g, x, y, 0);
                    _iconPalette.add(new IconWithPosition(icon, x, y));
    //                System.out.format("Type: %s, i: %d, class: %s%n", type.name(), i++, icon.getClass());
                }
            }

            if (_selectedIcon != null) {
                g.setColor(Color.RED);
                Stroke stroke2 = new BasicStroke(3.0f);
                g.setStroke(stroke2);
                _selectedIcon._icon.drawFrame(g, _selectedIcon._x, _selectedIcon._y);
            }



            Icon turntable = Icon.get(Icon.Type.WyeSlip, 0);
            turntable.draw(g, 1200, 700, 0);

            Icon.get(Icon.Type.Line, 0b00010010).draw(g, 1200-Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200-Icon.RASTER_SIZE, 700+0*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200-Icon.RASTER_SIZE, 700+1*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200-Icon.RASTER_SIZE, 700+2*Icon.RASTER_SIZE, 0);

            Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+0*Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+1*Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+2*Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE, 0);

            Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+0*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+1*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+2*Icon.RASTER_SIZE, 0);

            Icon.get(Icon.Type.WyeSlip, 0b00101001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+0*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.WyeSlip, 0b00101001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+1*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.WyeSlip, 0b00101001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+2*Icon.RASTER_SIZE, 0);


            Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+0*Icon.RASTER_SIZE, 700+3*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+1*Icon.RASTER_SIZE, 700+3*Icon.RASTER_SIZE, 0);
            Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+2*Icon.RASTER_SIZE, 700+3*Icon.RASTER_SIZE, 0);
        }

        drawControlPanel(g);

        if (_drawOldControlPanelAfter) {
            drawOldControlpanel(g);
        }

        g.setColor(oldColor);
        g.setStroke(oldStroke);
    }

    private void drawControlPanel(Graphics2D g) {
        Stroke capButtStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        Stroke capRoundStroke = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
//        g.setColor(Color.BLACK);
        g.setStroke(capRoundStroke);
        drawRaster(g);
        g.setColor(Color.BLACK);

        for (int y=0; y < RASTER_NUM_Y; y++) {
            for (int x=0; x < RASTER_NUM_X; x++) {
                int px = RASTER_X0 + Icon.RASTER_MARGIN + x * Icon.RASTER_SIZE;
                int py = RASTER_Y0 + Icon.RASTER_MARGIN + y * Icon.RASTER_SIZE;
                if (this._editControlPanel) {
                    iconData[x][y].getIcon().draw(g, px, py, 0);
                } else {
                    iconData[x][y].draw(g, px, py);
                }
                if (iconData[x][y].getAddress() != 0) {
                    g.drawString(Integer.toString(iconData[x][y].getAddress()), px, py);
                }
            }
        }
    }

    private void drawRaster(Graphics2D g) {
        Stroke stroke = new BasicStroke(1.0f);
        g.setColor(new Color(200,200,200));
        g.setStroke(stroke);

        for (int x=0; x <= RASTER_NUM_X; x++) {
            g.drawLine(RASTER_X0 + x * Icon.RASTER_SIZE, RASTER_Y0, RASTER_X0 + x * Icon.RASTER_SIZE, RASTER_MAX_Y);
        }

        for (int y=0; y <= RASTER_NUM_Y; y++) {
            g.drawLine(RASTER_X0, RASTER_Y0 + y * Icon.RASTER_SIZE, RASTER_MAX_X, RASTER_Y0 + y * Icon.RASTER_SIZE);
        }
    }

    public void handleEditControlPanel(int ex, int ey, JPanel panel) {
        if (ex > RASTER_X0 && ex < RASTER_MAX_X && ey > RASTER_Y0 && ey < RASTER_MAX_Y) {
            if (_selectedIcon != null) {
                int x = (ex - RASTER_X0) / Icon.RASTER_SIZE;
                int y = (ey - RASTER_Y0) / Icon.RASTER_SIZE;
                System.out.format("x: %d, y: %d, xx: %d, yy: %d%n", x, y, ex, ey);
                iconData[x][y] = _selectedIcon._icon.createIconData();
                panel.repaint();
            }
        } else {
            for (IconWithPosition ip : _iconPalette) {
                if (ip._icon.isHit(ip._x, ip._y, ex, ey)) {
                    _selectedIcon = ip;
                    panel.repaint();
                    return;
                }
            }
            // If here, no hit
           _selectedIcon = null;
            panel.repaint();
        }
    }

    public void handleControls(int ex, int ey, JPanel panel) {
        if (ex > RASTER_X0 && ex < RASTER_MAX_X && ey > RASTER_Y0 && ey < RASTER_MAX_Y) {
            int x = (ex - RASTER_X0) / Icon.RASTER_SIZE;
            int y = (ey - RASTER_Y0) / Icon.RASTER_SIZE;
            System.out.format("x: %d, y: %d, xx: %d, yy: %d%n", x, y, ex, ey);
            iconData[x][y].nextState();
            panel.repaint();
        }
    }

    public void event(int ex, int ey, JPanel panel) {
        if (_editControlPanel) {
            handleEditControlPanel(ex, ey, panel);
        } else {
            handleControls(ex, ey, panel);
        }
    }

    public Element getXml() {
        Element controlpanel = new Element("Controlpanel");
        Element icons = new Element("Icons");

        for (int y=0; y < RASTER_NUM_Y; y++) {
            for (int x=0; x < RASTER_NUM_X; x++) {
                IconData id = iconData[x][y];
                if (id.getIcon().getType() == Icon.Type.Empty) continue;   // Don't store empty icons
                icons.addContent(id.getXml(x, y));
            }
        }

        controlpanel.addContent(icons);
        return controlpanel;
    }

    public void loadXml(Element controlpanel) {
        Element icons = controlpanel.getChild("Icons");
        List<Element> iconList = icons.getChildren("Icon");
        for (Element iconElement : iconList) {
            int x = Integer.parseInt(iconElement.getAttributeValue("x"));
            int y = Integer.parseInt(iconElement.getAttributeValue("y"));
            Icon.Type type = Icon.Type.valueOf(iconElement.getAttributeValue("type"));
            int bits = Integer.parseInt(iconElement.getAttributeValue("bits"));
/*
            int x = Integer.parseInt(iconElement.getAttributeValue("x"));
            int x = Integer.parseInt(iconElement.getAttributeValue("x"));
            int x = Integer.parseInt(iconElement.getAttributeValue("x"));
            int x = Integer.parseInt(iconElement.getAttributeValue("x"));
            int x = iconElement.getAttributeValue("x");
            int x = iconElement.getAttributeValue("x");
*/
            Icon i = Icon.get(type, bits);
            IconData id = i.createIconData();
            id.loadXml(iconElement);
            iconData[x][y] = id;
        }
    }


    private static class GET_INSTANCE {

        private static ControlPanel INSTANCE = new ControlPanel();

    }


    private static final class IconWithPosition {

        private final Icon _icon;
        private final int _x;
        private final int _y;

        private IconWithPosition(Icon icon, int x, int y) {
            this._icon = icon;
            this._x = x;
            this._y = y;
        }
    }

    private static final Logger LOG = new Logger(ControlPanel.class);
}
