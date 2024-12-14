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

    int getState();

    void nextState();

    int getAddress();

    void setAddress(int address);

    int getMasterAddress();

    void setMasterAddress(int address);

    boolean isInverted();

    void setInverted(boolean inverted);

    Element getXml(int x, int y);

    void loadXml(Element iconData);

}
