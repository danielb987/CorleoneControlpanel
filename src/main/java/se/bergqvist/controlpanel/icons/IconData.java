package se.bergqvist.controlpanel.icons;

import java.awt.Graphics2D;
import org.jdom2.Element;

/**
 * Data for an icon.
 *
 * @author Daniel Bergqvist
 */
public interface IconData {

    Icon getIcon();

    void draw(Graphics2D g, int x, int y);

    void nextState();

    int getAddress();

    void setAddress(int address);

    Element getXml(int x, int y);

    void loadXml(Element iconData);

}
