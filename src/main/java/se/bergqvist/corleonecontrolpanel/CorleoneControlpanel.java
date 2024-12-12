package se.bergqvist.corleonecontrolpanel;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import se.bergqvist.config.Config;
import se.bergqvist.config.Config.ScreenConfig;
import se.bergqvist.controlpanel.icons.LineIcon;
import se.bergqvist.controlpanel.icons.TurnoutIcon;
import se.bergqvist.controlpanel.icons.TurntableIcon;
import se.bergqvist.touch.TouchManager;
import se.bergqvist.touch.TouchManager.EventListener;
import se.bergqvist.xml.LoadXml;

/**
 * Main program
 *
 * @author Daniel Bergqvist
 */
public class CorleoneControlpanel {

    private static final List<MainJFrame> _frames = new ArrayList<>();


    public static void setShowSelectScreen(boolean show) {
        for (MainJFrame f : _frames) {
            f.setShowSelectScreen(show);
        }
    }

    public static void switchTouchscreen() {
        Config.get().switchTouchscreen();
    }

    public static void main(String[] args) {

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

            // We need a temporary frame to create icon images
            JFrame tempFrame = new JFrame();
            tempFrame.pack();
            LineIcon.initialize(tempFrame);
            TurnoutIcon.initialize(tempFrame);
            TurntableIcon.initialize(tempFrame);
            tempFrame.dispose();

            // TODO:
            // Save and restore map between touch and screen

            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice[] gs = ge.getScreenDevices();

//            javax.swing.JOptionPane.showConfirmDialog((java.awt.Component) null, "Found : " + gs.length, "screen detected ?",
//                    javax.swing.JOptionPane.DEFAULT_OPTION);

            for (int j = 0; j < gs.length; j++) {
                GraphicsDevice gd = gs[j];

                Rectangle r = gd.getDefaultConfiguration().getBounds();
//                System.out.format("x: %d, y: %d, w: %d, h: %d%n", r.x, r.y, r.width, r.height);
//                System.out.flush();
/*
                JFrame frame = new JFrame(gd.getDefaultConfiguration());
                frame.setTitle("I'm on monitor #" + j);
                frame.setSize(400, 200);
                frame.add(new JLabel("hello world"));
                frame.setVisible(true);
*/


                MainJFrame mainFrame = new MainJFrame(r);
//                mainFrame.setVisible(true);
                mainFrame.init();
                _frames.add(mainFrame);

                ScreenConfig sc = new ScreenConfig(r.x, mainFrame, mainFrame.getTouchListener());
                Config.get().addScreenConfig(sc);
            }

            File file = new File(Config.get().getFilename());
            new LoadXml().load(file);

            for (ScreenConfig sc : Config.get().getScreenConfigs())  {
                Path path = Config.get().getPathForTouchscreen(sc.getPosition());
                System.out.format("Path: %s%n", path);
                EventListener listener = TouchManager.create(path, sc.getTouchListener());
                Config.get().setListenerForTouchscreen(path, listener);
                sc.getFrame().setVisible(true);
            }

        });
    }
}
