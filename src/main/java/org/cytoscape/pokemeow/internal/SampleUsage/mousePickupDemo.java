package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.PMVMatrix;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import com.jogamp.newt.event.MouseListener;
import com.jogamp.newt.event.MouseEvent;
import java.io.*;
import java.nio.FloatBuffer;

/**
 * Created by ZhangMenghe on 2017/7/12.
 */
public class mousePickupDemo implements GLEventListener,MouseListener {
    private pmShaderParams gshaderParam;
    private int program;
    private pmBasicNodeShape mtriangle;
    private pmNodeShapeFactory factory;
    private Vector2 lastMousePosition;
    private GL4 gl4;
    private boolean print = false;
    public float[] rList;
    private int width = 600;
    PMVMatrix pmvMatrix = new PMVMatrix();
    @Override
    public void init(GLAutoDrawable drawable) {

        gl4 = drawable.getGL().getGL4();
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/flat.vert"),
                null, null, null,
                debugDraw.class.getResource("shader/flat.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmNodeShapeFactory(gl4);
        mtriangle = factory.createNode(gl4, pmNodeShapeFactory.SHAPE_TRIANGLE);
        mtriangle.setColor(gl4, new Vector4(1.0f, .0f, .0f, 1.0f));
        //mtriangle.setOrigin(new Vector3(-1.0f,1.0f,.0f));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl4.glUseProgram(program);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);

        factory.drawNode(gl4, mtriangle, gshaderParam);
        FloatBuffer glBB = FloatBuffer.allocate(12 * width * width);
        gl4.glReadPixels(0, 0, width, width, GL4.GL_RGBA, GL4.GL_FLOAT, glBB);
        float[] tmp;
        tmp = glBB.array();
        rList = new float[width * width];
        int i = 0;
        for (int yi = 0; yi < width; yi++) {
            for (int xi = 0; xi < width; xi++) {
                float r = tmp[4 * i];
                rList[i++] = r;
            }
        }

        FloatBuffer tmpBuf = pmvMatrix.glGetMviMatrixf();
        float[] tmpArr = tmpBuf.array();
        BufferedWriter writer = null;
        try {
            String fileName = "F:\\data.txt";
            writer = new BufferedWriter(new FileWriter(fileName));

            for(i =0;i<width;i++) {
                for (int j = 0; j < width; j++) {
                    if(rList[i * width + j] == 0.2f)
                        writer.write(String.valueOf(0));
                    else if(rList[i * width + j] == 1.0f)
                        writer.write(String.valueOf(1));
                    else
                        writer.write("#");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.out.println("error");
        }
        finally
        {
            try
            {
                if ( writer != null)
                    writer.close( );
            }
            catch ( IOException e)
            {
            }
        }
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {

        mtriangle.gsthForDraw.dispose(gl4);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

        gl4.glClearColor(0.2f, 0.4f, 0.4f, 1.0f);
        gl4.glViewport(x, y, width, height);


    }

    @Override
    public void mouseClicked(MouseEvent e) {

        lastMousePosition = new Vector2(e.getX(), e.getY());
        System.out.println(lastMousePosition);
        if(mtriangle.isHit(lastMousePosition))
            System.out.println("HIT");
        print = !print;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
//        lastMousePosition = new Vector2(e.getX(), e.getY());
//        System.out.println(lastMousePosition);
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseWheelMoved(MouseEvent e){}
    public void mouseDragged(MouseEvent e){}
    public void mouseMoved(MouseEvent e){}

    public static void main(String[] args) throws IOException {
        final GLProfile glProfile = GLProfile.get(GLProfile.GL4);
        GLCapabilities glCapabilities = new GLCapabilities(glProfile);
        final GLWindow glWindow = GLWindow.create(glCapabilities);
        final Animator animator = new Animator(glWindow);
        glWindow.setSize(600, 600);
        glWindow.setTitle("mousePickUp Demo");
        glWindow.setFullscreen(false);
        glWindow.setVisible(true);

        final mousePickupDemo demo = new mousePickupDemo();
        glWindow.addGLEventListener(demo);
        glWindow.addMouseListener(demo);
        animator.start();
    }


}

