package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import main.java.org.cytoscape.pokemeow.internal.viewport.Viewport;
import main.java.org.cytoscape.pokemeow.internal.viewport.ViewportMouseEvent;
import main.java.org.cytoscape.pokemeow.internal.viewport.ViewportMouseEventListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
/**
 * Created by ZhangMenghe on 2017/6/19.
 */
public class ViewportDemo extends JFrame{
    JDesktopPane jdpDesktop;
    static int openFrameCount = 0;
    private MyInternalFrame frame;

    public ViewportDemo() {
        super("ViewportDemo");
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset, screenSize.width - inset * 2,
                screenSize.height - inset * 2);
        // Add a Window Exit Listener
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        // Create and Set up the GUI.
        jdpDesktop = new JDesktopPane();
        // A specialized layered pane to be used with JInternalFrames
        createFrame(); // Create first window
        setContentPane(jdpDesktop);
        setJMenuBar(createMenuBar());
        // Make dragging faster by setting drag mode to Outline
        jdpDesktop.putClientProperty("JDesktopPane.dragMode", "outline");
        Viewport view = new Viewport(frame);
        //TODO : tackle!!
        view.demo = new drawNodeAndEdgeDemo();

    }
    protected JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Frame");
        menu.setMnemonic(KeyEvent.VK_N);
        JMenuItem menuItem = new JMenuItem("New IFrame");
        menuItem.setMnemonic(KeyEvent.VK_N);
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                createFrame();

            }
        });
        menu.add(menuItem);
        menuBar.add(menu);
        return menuBar;
    }
    protected void createFrame() {
        frame = new MyInternalFrame();
        frame.setVisible(true);
        // Every JInternalFrame must be added to content pane using JDesktopPane
        jdpDesktop.add(frame);
        try {
            frame.setSelected(true);
        } catch (java.beans.PropertyVetoException e) {
        }
    }
    public static void main(String[] args) {
        ViewportDemo frame = new ViewportDemo();
        frame.setVisible(true);
    }
    class MyInternalFrame extends JInternalFrame {

        static final int xPosition = 30, yPosition = 30;
        public MyInternalFrame() {
            super("IFrame #" + (++openFrameCount), true, // resizable
                    true, // closable
                    true, // maximizable
                    true);// iconifiable
            setSize(800, 800);
            // Set the window's location.
            setLocation(xPosition * openFrameCount, yPosition
                    * openFrameCount);
        }
    }

}
