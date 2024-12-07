package se.bergqvist.controlpanel.icons;

import java.awt.Graphics2D;

/**
 * Data for an icon.
 *
 * @author Daniel Bergqvist
 */
public interface IconData {

    Icon getIcon();

    void draw(Graphics2D g, int x, int y);

    void nextState();

}
