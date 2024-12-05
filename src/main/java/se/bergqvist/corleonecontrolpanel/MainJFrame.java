package se.bergqvist.corleonecontrolpanel;

import se.bergqvist.event.EventTypeEnum;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.JFrame;
import se.bergqvist.event.EventEnum;
import se.bergqvist.touch.TouchManager;

// Force fsck on next boot:
// https://www.cyberciti.biz/faq/linux-force-fsck-on-the-next-reboot-or-boot-sequence/
// sudo touch /forcefsck


/**
 * Main window
 *
 * @author Daniel Bergqvist
 */
public class MainJFrame extends javax.swing.JFrame {

    private final int screen;

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame(int screen, Rectangle bounds) {
        this.screen = screen;
/*
        if (screen == 0) {
//        if (screen == 1) {
            mainJPanel = new MainJPanel(this);
            getContentPane().add(mainJPanel);
            pack();
        } else {
            initComponents();
        }
*/
        initComponents();
        setBounds(bounds);
//        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void init() {
//        Thread t = new Thread(this::listenToEvents);
//        t.setDaemon(true);
//        t.start();
    }

    public TouchManager.TouchListener getTouchListener() {
        return ((MainJPanel)mainJPanel).getTouchListener();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainJPanel = new MainJPanel(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout mainJPanelLayout = new javax.swing.GroupLayout(mainJPanel);
        mainJPanel.setLayout(mainJPanelLayout);
        mainJPanelLayout.setHorizontalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 870, Short.MAX_VALUE)
        );
        mainJPanelLayout.setVerticalGroup(
            mainJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 458, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainJPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(mainJPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel mainJPanel;
    // End of variables declaration//GEN-END:variables

}