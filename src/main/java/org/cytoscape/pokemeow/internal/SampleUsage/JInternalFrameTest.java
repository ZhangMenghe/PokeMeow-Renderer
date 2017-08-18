package org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import org.cytoscape.pokemeow.internal.viewport.Viewport;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JFrame;
import java.awt.event.*;
import java.awt.*;

public class JInternalFrameTest extends JFrame implements GLEventListener {
    GL4 gl;
    JDesktopPane jdpDesktop;
    static int openFrameCount = 0;
    private Demo demo = null;
    private MyInternalFrame frame;
    public JInternalFrameTest() {
        super("JInternalFrame Usage Demo");
        // Make the main window positioned as 50 pixels from each edge of the
        // screen.
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
        //jdpDesktop.setBackground(new Color(1.0f,.0f,.0f,1.0f));
        setJMenuBar(createMenuBar());
        // Make dragging faster by setting drag mode to Outline
        jdpDesktop.putClientProperty("JDesktopPane.dragMode", "outline");
        //Viewport view = new Viewport(frame);
        GLProfile profile = GLProfile.getDefault(); // Use the system's default version of OpenGL
        GLCapabilities capabilities = new GLCapabilities(profile);

        GLJPanel panel = new GLJPanel(capabilities);
        panel.addGLEventListener(this);
        JInternalFrame JInframe = (JInternalFrame) frame;
        JInframe.getContentPane().setLayout(new BorderLayout());
        JInframe.getContentPane().add(panel, BorderLayout.CENTER);

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
        JInternalFrameTest frame = new JInternalFrameTest();
        frame.setVisible(true);
    }
    class MyInternalFrame extends JInternalFrame {

        static final int xPosition = 30, yPosition = 30;
        public MyInternalFrame() {
            super("IFrame #" + (++openFrameCount), true, // resizable
                    true, // closable
                    true, // maximizable
                    true);// iconifiable
            setSize(300, 300);
            // Set the window's location.
            setLocation(xPosition * openFrameCount, yPosition
                    * openFrameCount);
        }
    }
    public void display(GLAutoDrawable drawable){
        gl.glClearColor(0.2f, 0.2f, 0.2f,1.0f);
        gl.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        demo.display(drawable);
    }

    public void dispose( GLAutoDrawable drawable ) {
        // TODO Auto-generated method stub
        demo.dispose(drawable);
    }


    public void init( GLAutoDrawable drawable ) {
        gl = drawable.getGL().getGL4();
        demo = new simpleTriangleDemo();
        demo.init(drawable);
    }

    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {
        gl.glClearColor(0.2f, 0.2f, 0.2f,1.0f);
        demo.reshape(drawable, x, y, width, height);
    }
}