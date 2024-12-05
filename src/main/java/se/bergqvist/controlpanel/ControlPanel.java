package se.bergqvist.controlpanel;

import se.bergqvist.controlpanel.icons.LineIcon;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import se.bergqvist.controlpanel.icons.Icon;

/**
 * Control panel.
 *
 * @author Daniel Bergqvist
 */
public class ControlPanel {

    private static final ControlPanel INSTANCE = new ControlPanel();

    // address, masterAddr, display, x1, y1, x2, y2, x3, y3, inverted
    int[][] turnouts = {
        {101, 0, 4, 53, 201, 72, 201, 66, 214, 0},
        {102, 101, 4, 99, 247, 80, 247, 86, 234, 0},
        {105, 0, 4, 139, 201, 158, 201, 152, 188, 0},
        {111, 0, 4, 156, 247, 175, 247, 169, 260, 0},
        {112, 0, 3, 188, 339, 169, 339, 175, 352, 0},
        {113, 0, 3, 235, 293, 216, 293, 222, 306, 0},
        {114, 0, 3, 281, 247, 262, 247, 268, 260, 0},
        {115, 0, 4, 289, 155, 270, 155, 276, 142, 0},
        {121, 0, 2, 40, 201, 59, 201, 53, 188, 0},
        {122, 0, 3, 281, 201, 262, 201, 268, 188, 0},
        {123, 0, 3, 62, 247, 81, 247, 75, 234, 0},
        {124, 123, 3, 108, 201, 89, 201, 95, 214, 0},
        {125, 0, 3, 62, 155, 81, 155, 75, 142, 0},
        {131, 0, 3, 108, 109, 127, 109, 121, 96, 0},
        {134, 0, 3, 96, 431, 115, 431, 109, 418, 0},
        {135, 0, 3, 142, 385, 123, 385, 129, 398, 0},
        {211, 0, 2, 121, 201, 140, 201, 134, 188, 0},
        {212, 0, 2, 40, 247, 59, 247, 53, 260, 0},
        {213, 0, 2, 86, 293, 105, 293, 99, 306, 0},
        {214, 0, 2, 132, 339, 151, 339, 145, 352, 0},
        {215, 0, 2, 144, 98, 163, 98, 157, 85, 0},
        {411, 0, 1, 280, 201, 261, 201, 267, 188, 0},
        {412, 0, 1, 281, 247, 262, 247, 268, 260, 0},
        {413, 0, 1, 235, 293, 216, 293, 222, 306, 0},
        {414, 0, 1, 189, 339, 170, 339, 176, 352, 0},
        {415, 0, 1, 206, 127, 225, 127, 219, 140, 0},
        {421, 0, 1, 177, 98, 158, 98, 164, 85, 0},
        {422, 415, 1, 234, 155, 215, 155, 221, 142, 1},
        {511, 512, 0, 40, 247, 59, 247, 53, 234, 0},
        {512, 0, 0, 86, 201, 67, 201, 73, 214, 0},
        {513, 0, 0, 121, 201, 140, 201, 134, 214, 0},
        {514, 513, 0, 167, 247, 148, 247, 154, 234, 0},
        {711, 712, 0, 235, 314, 235, 333, 248, 327, 0},
        {712, 0, 0, 281, 360, 281, 341, 268, 347, 0},
        {713, 0, 0, 281, 395, 281, 414, 268, 408, 0},
        {714, 713, 0, 235, 441, 235, 422, 248, 428, 1},
    };

    // display, x1, y1, x2, y2
    int[][] lines = {
        {4, 0, 201, 321, 201},
        {3, 0, 201, 321, 201},
        {2, 0, 201, 321, 201},
        {1, 0, 201, 321, 201},
        {0, 0, 201, 321, 201},
        {4, 0, 300, 53, 247},
        {4, 53, 247, 321, 247},
        {3, 0, 247, 321, 247},
        {2, 0, 247, 321, 247},
        {1, 0, 247, 321, 247},
        {0, 0, 247, 202, 247},
        {0, 202, 247, 235, 280},
        {0, 235, 280, 235, 480},
        {0, 281, 0, 281, 480},
        {4, 53, 201, 99, 247},
        {3, 62, 247, 108, 201},
        {0, 40, 247, 86, 201},
        {0, 121, 201, 167, 247},
        {0, 235, 314, 281, 360},
        {0, 281, 395, 235, 441},
        {4, 139, 201, 185, 155},
        {4, 185, 155, 321, 155},
        {3, 0, 155, 235, 155},
        {3, 235, 155, 281, 201},
        {4, 289, 155, 243, 109},
        {4, 145, 109, 243, 109},
        {3, 62, 155, 154, 63},
        {3, 108, 109, 293, 109},
        {3, 154, 63, 308, 63},
        {2, 40, 201, 190, 52},
        {2, 190, 52, 321, 52},
        {1, 0, 52, 131, 52},
        {1, 131, 52, 280, 201},
        {2, 144, 98, 321, 98},
        {2, 121, 201, 167, 155},
        {2, 167, 155, 321, 155},
        {1, 0, 98, 177, 98},
        {1, 0, 155, 234, 155},
        {1, 206, 127, 298, 127},
        {2, 40, 247, 178, 385},
        {2, 179, 385, 321, 385},
        {1, 0, 385, 142, 385},
        {1, 143, 385, 281, 247},
        {2, 86, 293, 321, 293},
        {2, 132, 339, 321, 339},
        {1, 0, 293, 235, 293},
        {1, 0, 339, 189, 339},
        {4, 156, 247, 202, 293},
        {4, 202, 293, 321, 293},
        {3, 96, 431, 281, 247},
        {3, 0, 293, 235, 293},
        {3, 0, 339, 188, 339},
        {4, 252, 339, 321, 339},
        {3, 0, 385, 142, 385},
        {4, 212, 385, 321, 385},
        {3, 0, 431, 258, 431},
        {4, 212, 431, 321, 431},
    };

    public static ControlPanel get() {
        return INSTANCE;
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
        Stroke capButtStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        Stroke capRoundStroke = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setColor(Color.BLACK);
        g.setStroke(capRoundStroke);
        for (int[] line : lines) {
            int offset = 1500 - line[0] * 330;
            g.drawLine(offset+line[1], line[2], offset+line[3], line[4]);
        }
//        for (int display=0; display < 5; display++) {
//        }

//        g.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
        // address, masterAddr, display, x1, y1, x2, y2, x3, y3, inverted
        for (int[] turnout : turnouts) {
            boolean thrown = Math.random() < 0.5;
            int offset = 1500 - turnout[2] * 330;
            if (thrown) {
                g.setColor(Color.WHITE);
                g.setStroke(capButtStroke);
                g.drawLine(offset+turnout[3], turnout[4], offset+turnout[5], turnout[6]);
                g.setColor(Color.BLACK);
                g.setStroke(capRoundStroke);
                g.drawLine(offset+turnout[3], turnout[4], offset+turnout[7], turnout[8]);
            } else {
                g.setColor(Color.WHITE);
                g.setStroke(capButtStroke);
                g.drawLine(offset+turnout[3], turnout[4], offset+turnout[7], turnout[8]);
                g.setColor(Color.BLACK);
                g.setStroke(capRoundStroke);
                g.drawLine(offset+turnout[3], turnout[4], offset+turnout[5], turnout[6]);
            }
/*
            g.setColor(Color.RED);
            g.drawLine(offset+turnout[3], turnout[4], offset+turnout[5], turnout[6]);
            g.setColor(Color.GREEN);
            g.drawLine(offset+turnout[3], turnout[4], offset+turnout[7], turnout[8]);
*/
        }

/*
        g.setColor(Color.GREEN);
        g.setStroke(oldStroke);
        for (int[] turnout : turnouts) {
            int offset = 1500 - turnout[2] * 330;

            int[] a = {101,102};
            boolean contains = java.util.stream.IntStream.of(a).anyMatch(x -> x == turnout[0]);
            if (contains) {
                g.drawLine(0, turnout[4], 1920, turnout[4]);
                g.drawLine(offset+turnout[3], 0, offset+turnout[3], 1080);
                System.out.format("Turnout %d: %d, %d%n", turnout[0], turnout[3], turnout[4]);
                System.out.format("Width: %d, Height: %d%n", 99-53, 247-201);
            }
        }
*/


        int count = 0;
/*
        for (int i=0; i <= 255; i++) {
            Icon icon = Icon.get(i);
            if (icon != null) {
                int x = 100 + count++ * Icon.RASTER_SIZE;
                int y = 600;
                icon.draw(g, x, y);
            }
        }
*/
        int y = 500;
        for (LineIcon.Type type : LineIcon.Type.values()) {
            count = 0;
//            y += 50;
            y += Icon.RASTER_SIZE;
//            int i = 0;
            for (Icon icon : Icon.get(type)) {
                int x = 100 + count++ * Icon.RASTER_SIZE;
                icon.draw(g, x, y);
//                System.out.format("Type: %s, i: %d, class: %s%n", type.name(), i++, icon.getClass());
            }
        }

        Icon turntable = Icon.get(Icon.Type.WyeSlip, 0);
        turntable.draw(g, 1200, 700);

        Icon.get(Icon.Type.Line, 0b00010010).draw(g, 1200-Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200-Icon.RASTER_SIZE, 700+0*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200-Icon.RASTER_SIZE, 700+1*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200-Icon.RASTER_SIZE, 700+2*Icon.RASTER_SIZE);

        Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+0*Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+1*Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+2*Icon.RASTER_SIZE, 700-1*Icon.RASTER_SIZE);

        Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+0*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+1*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b00010001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+2*Icon.RASTER_SIZE);

        Icon.get(Icon.Type.WyeSlip, 0b00101001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+0*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.WyeSlip, 0b00101001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+1*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.WyeSlip, 0b00101001).draw(g, 1200+3*Icon.RASTER_SIZE, 700+2*Icon.RASTER_SIZE);


        Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+0*Icon.RASTER_SIZE, 700+3*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+1*Icon.RASTER_SIZE, 700+3*Icon.RASTER_SIZE);
        Icon.get(Icon.Type.Line, 0b01000100).draw(g, 1200+2*Icon.RASTER_SIZE, 700+3*Icon.RASTER_SIZE);


        drawControlPanel(g);

        g.setColor(oldColor);
        g.setStroke(oldStroke);
    }

    private void drawControlPanel(Graphics2D g) {
        Stroke capButtStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND);
        Stroke capRoundStroke = new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
        g.setColor(Color.BLACK);
        g.setStroke(capRoundStroke);

        drawRaster(g);
    }

    private static final int RASTER_X0 = 20;
    private static final int RASTER_Y0 = 40 - Icon.RASTER_SIZE;
    private static final int RASTER_NUM_X = 41;
    private static final int RASTER_NUM_Y = 11;
    private static final int RASTER_MAX_X = RASTER_X0 + RASTER_NUM_X * Icon.RASTER_SIZE;
    private static final int RASTER_MAX_Y = RASTER_Y0 + RASTER_NUM_Y * Icon.RASTER_SIZE;

    private void drawRaster(Graphics2D g) {
        Stroke stroke = new BasicStroke(1.0f);
        g.setColor(Color.GREEN);
        g.setStroke(stroke);

        for (int x=0; x <= RASTER_NUM_X; x++) {
            g.drawLine(RASTER_X0 + x * Icon.RASTER_SIZE, RASTER_Y0, RASTER_X0 + x * Icon.RASTER_SIZE, RASTER_MAX_Y);
        }

        for (int y=0; y <= RASTER_NUM_Y; y++) {
            g.drawLine(RASTER_X0, RASTER_Y0 + y * Icon.RASTER_SIZE, RASTER_MAX_X, RASTER_Y0 + y * Icon.RASTER_SIZE);
        }
    }

}
