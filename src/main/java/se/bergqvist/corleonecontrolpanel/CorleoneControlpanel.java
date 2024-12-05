package se.bergqvist.corleonecontrolpanel;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import se.bergqvist.controlpanel.icons.LineIcon;
import se.bergqvist.controlpanel.icons.TurnoutIcon;
import se.bergqvist.controlpanel.icons.TurntableIcon;
import se.bergqvist.input.InputDevices;
import se.bergqvist.touch.TouchManager;

/**
 * Main program
 *
 * @author Daniel Bergqvist
 */
public class CorleoneControlpanel {

    public static void main(String[] args) {

        Set<Path> touchScreens = new InputDevices().getInputDevices();
        Map<Path, Integer> touchScreenMap = new HashMap<>();

        for (Path p : touchScreens) {
            System.out.format("Touch screen: %s%n", p);
            touchScreenMap.put(p, -1);
        }

        for (Path p : touchScreens) {
            if (p.toString().equals("/dev/input/event5")) {
//            if (p.toString().equals("/dev/input/event6")) {
                touchScreenMap.put(p, 0);
            }
        }

        // TODO:
        // Save and restore map between touch and screen

        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();

//            javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, "Found : " + gs.length, "screen detected ?",
//                    javax.swing.JOptionPane.DEFAULT_OPTION);

            for (int j = 0; j < gs.length; j++) {
                GraphicsDevice gd = gs[j];

                Rectangle r = gd.getDefaultConfiguration().getBounds();
                System.out.format("x: %d, y: %d, w: %d, h: %d%n", r.x, r.y, r.width, r.height);
                System.out.flush();
/*
                JFrame frame = new JFrame(gd.getDefaultConfiguration());
                frame.setTitle("I'm on monitor #" + j);
                frame.setSize(400, 200);
                frame.add(new JLabel("hello world"));
                frame.setVisible(true);
*/


                MainJFrame mainFrame = new MainJFrame(j, r);
                if (j==0) {
                    LineIcon.initialize(mainFrame);
                    TurnoutIcon.initialize(mainFrame);
                    TurntableIcon.initialize(mainFrame);
                }
                mainFrame.setVisible(true);
                mainFrame.init();


                Path p = null;
                Path pFree = null;
                for (var entry : touchScreenMap.entrySet()) {
                    if (entry.getValue() == r.x) p = entry.getKey();
                    if (entry.getValue() == -1) pFree = entry.getKey();
                }
                // System.out.format("%d: p: %s, pFree: %s%n", j, p, pFree);
                if (p == null) p = pFree;

                touchScreenMap.remove(p);
/*
                TouchListener listener = new TouchListener() {
                    @Override
                    public void event(TouchEvent event) {
                    }
                };
                TouchManager.create(p, listener);
*/
                if (p != null)
                TouchManager.create(p, mainFrame.getTouchListener());
//                touchScreenMap.put(p, -1);
            }


/*
            MainJFrame mainFrame = new MainJFrame();
            mainFrame.setVisible(true);
            mainFrame.init();
*/
        });
    }
}
