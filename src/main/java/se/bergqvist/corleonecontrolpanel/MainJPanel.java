package se.bergqvist.corleonecontrolpanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JPanel;
import se.bergqvist.controlpanel.ControlPanel;
import se.bergqvist.touch.TouchEnum;
import se.bergqvist.touch.TouchEvent;
import se.bergqvist.touch.TouchManager;
import se.bergqvist.touch.TouchManager.TouchListener;

/**
 * Main panel
 *
 * @author Daniel Bergqvist
 */
public class MainJPanel extends JPanel implements MouseListener {

    private static final int TOUCH_WIDTH = 16380;
    private static final int TOUCH_HEIGHT = 9600;

//    private static final ControlPanel controlPanel = ControlPanel.get();

    private final MainJFrame _frame;

    private int x, y;
    private int mx, my;
    private int ex, ey;
    private int maxX, maxY;
    private Graphics2D bufferGraphics;
    private Image offscreenImage;
    private Rectangle bounds;


    public MainJPanel(MainJFrame frame) {
        this._frame = frame;
        this.addMouseListener(this);
//        this.repaint();
//        graphics.setStroke(new BasicStroke(0.1f));
//        graphics.setColor(Color.black);
    }

    public TouchListener getTouchListener() {
        return this::touchEvent;
    }

    public void touchEvent(TouchEvent event) {
        System.out.format("%s: %d, %d%n", event.getType(), event.getX(), event.getY());
        System.out.flush();
//        if (event.getType() == TouchEnum.StartDrag
//                || event.getType() == TouchEnum.Drag
//                || event.getType() == TouchEnum.EndDrag) {

            if (bounds != null) {
                this.x = (int) (((double)event.getX()) * bounds.width / TOUCH_WIDTH);
                this.y = (int) (((double)event.getY()) * bounds.height / TOUCH_HEIGHT);
                this.ex = event.getX();
                this.ey = event.getY();
                ControlPanel.get().event(x,y, this);
            }
            if (maxX < event.getX()) maxX = event.getX();
            if (maxY < event.getY()) maxY = event.getY();
            this.repaint();
//        }
//        touchEvent(event);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Rectangle newBounds = this.getBounds();

        if ((bounds == null) || (! bounds.equals(newBounds)))
        {
            // Force creation of new double buffer
            bufferGraphics = null;
            bounds = newBounds;
        }

        // Init double buffering
        if (bufferGraphics == null)
        {
            offscreenImage = createImage(bounds.width, bounds.height);
            bufferGraphics = (Graphics2D) offscreenImage.getGraphics();
        }


//      bufferGraphics.drawRect(0, 0, bounds.width-1, bounds.height-1);

        // Vit bakgrundsfärg
        bufferGraphics.setColor(Color.WHITE);
        bufferGraphics.fillRect(0, 0, bounds.width, bounds.height);

        bufferGraphics.setColor(Color.BLACK);
//        bufferGraphics.drawLine(0, 0, bounds.width, bounds.height);

        ControlPanel.get().draw(bufferGraphics);
/*
        bufferGraphics.setColor(Color.RED);
        bufferGraphics.drawLine(0, my, bounds.width, my);
        bufferGraphics.drawLine(mx/2, 0, mx/2, bounds.height);

        bufferGraphics.setColor(Color.BLACK);
        bufferGraphics.drawLine(0, y, bounds.width, y);
        bufferGraphics.drawLine(x, 0, x, bounds.height);
*/

////        bufferGraphics.drawRect(2, 2, 1916, 1076);

////        draw(bufferGraphics, false, null, 0, 0, 0, 0);

        Font font = new Font("Verdana", Font.PLAIN, 12);
        bufferGraphics.setFont(font);
//        String str = String.format("%1.2f", scaleFactor);
        String str = String.format("%d, %d", x, y);
        bufferGraphics.drawString(str, 2, 10);
        str = String.format("%d, %d", mx, my);
        bufferGraphics.drawString(str, 2, 30);
        str = String.format("%d, %d", ex, ey);
        bufferGraphics.drawString(str, 2, 50);

        str = String.format("%d, %d", maxX, maxY);
        bufferGraphics.drawString(str, 2, 70);

        double touchWidth = 1.0*ex/mx*bounds.width*2;
        double touchHeight = 1.0*ey/my*bounds.height;
        str = String.format("TOUCH_WIDTH: %1.0f, TOUCH_HEIGHT: %1.0f", touchWidth, touchHeight);
        bufferGraphics.drawString(str, 2, 90);
//        System.out.format("ey: %d, my: %d, height: %d, TH: %1.0f%n", ey, my, bounds.height, touchHeight);

        g.drawImage(offscreenImage, 0, 0, this);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (TouchManager.isTouchActive()) {
            // System.out.format("Mouse pressed but touch active%n");
            return;     // Touch so don't handle this as a mouse event
        }
        mx = me.getX();
        my = me.getY();
//        mx = me.getXOnScreen();
//        my = me.getYOnScreen();
        ControlPanel.get().mousePressed(me, this);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }

}
