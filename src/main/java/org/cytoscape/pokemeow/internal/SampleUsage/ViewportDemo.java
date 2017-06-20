package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.viewport.Viewport;
import main.java.org.cytoscape.pokemeow.internal.viewport.ViewportMouseEvent;
import main.java.org.cytoscape.pokemeow.internal.viewport.ViewportMouseEventListener;

import javax.swing.*;
import java.awt.event.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
/**
 * Created by ZhangMenghe on 2017/6/19.
 */
public class ViewportDemo extends JPanel{
    public int openFrameCount = 0;
    JInternalFrame container;
    JFrame frame;
    JDesktopPane desktop;
    Viewport view;

    public ViewportDemo(){
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("JInternalFrame Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,600);

        desktop = new JDesktopPane();

        container = new JInternalFrame(
                "IFrame #" + (++openFrameCount),
                true, // resizable
                true, // closable
                true, // maximizable
                true);
        container.requestFocus();
        desktop.add(container);
        container.setLocation(30,30);
        container.setSize(300,300);

        container.setVisible(true);
        frame.add(desktop);
        frame.setVisible(true);
        view = new Viewport(container);
    }
    public static void main(String[] args){
        ViewportDemo demo = new ViewportDemo();
    }

}
