package main.java.org.cytoscape.pokemeow.internal.SampleUsage;
import main.java.org.cytoscape.pokemeow.internal.viewport.Viewport;

import javax.swing.*;

/**
 * Created by ZhangMenghe on 2017/6/19.
 */
public class ViewportDemo {
    public ViewportDemo(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame("JInternalFrame Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);

        JDesktopPane desktop = new JDesktopPane();
        JInternalFrame container = new MyInternalFrame();
        desktop.add(container);
        container.setLocation(20,20);
        container.setVisible(true);
        frame.add(desktop);
        frame.setVisible(true);
        Viewport view = new Viewport(container);
    }
    public static void main(String[] args){
        ViewportDemo demo = new ViewportDemo();
    }
    public int openFrameCount = 0;
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
}
