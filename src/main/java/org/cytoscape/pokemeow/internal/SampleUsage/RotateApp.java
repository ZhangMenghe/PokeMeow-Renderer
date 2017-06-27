package main.java.org.cytoscape.pokemeow.internal.SampleUsage;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*;

public class RotateApp {

    private static final int N = 3;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                JFrame frame = new JFrame();
                frame.setLayout(new GridLayout(N, N, N, N));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                for (int i = 0; i < N * N; i++) {
                    frame.add(new RotatePanel());
                }
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}


class RotatePanel extends JPanel implements ActionListener {

    private static final int SIZE = 256;
    private static double DELTA_THETA = Math.PI / 90;
    private final Timer timer = new Timer(25, this);
    private Image image = RotatableImage.getImage(SIZE);
    private double dt = DELTA_THETA;
    private double theta;

    public RotatePanel() {
        this.setBackground(Color.lightGray);
        this.setPreferredSize(new Dimension(
                image.getWidth(null), image.getHeight(null)));
        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                image = RotatableImage.getImage(SIZE);
                dt = -dt;
            }
        });
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);
        g2d.rotate(theta);
        g2d.translate(-image.getWidth(this) / 2, -image.getHeight(this) / 2);
        g2d.drawImage(image, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        theta += dt;
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SIZE, SIZE);
    }

}

class RotatableImage {

    private static final Random r = new Random();

    static public Image getImage(int size) {
        BufferedImage bi = new BufferedImage(
                size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.getHSBColor(r.nextFloat(), 1, 1));
        g2d.setStroke(new BasicStroke(size / 8));
        g2d.drawLine(0, size / 2, size, size / 2);
        g2d.drawLine(size / 2, 0, size / 2, size);
        g2d.dispose();
        return bi;
    }
}
