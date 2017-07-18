package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.PMVMatrix;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
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
    private pmBasicNodeShape[] NodeList;
    private pmNodeShapeFactory factory;
    private Vector2 lastMousePosition;
    private GL4 gl4;
    public float[] rList;
    private int width = 600;
    PMVMatrix pmvMatrix = new PMVMatrix();
    private int times = 0;
    private int numOfNodes = 10;

    @Override
    public void init(GLAutoDrawable drawable) {
        gl4 = drawable.getGL().getGL4();
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/flat.vert"),
                null, null, null,
                debugDraw.class.getResource("shader/flat.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmNodeShapeFactory(gl4);
        NodeList = new pmBasicNodeShape[numOfNodes];
        int n=0;
        for(Byte idx = 0;idx<numOfNodes;idx++)
            NodeList[n++] = factory.createNode(gl4, idx);

        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                float cx = -0.6f + y*0.5f;
                float cy = -0.6f + x*0.5f;
                int idx = 3*x+y;
                NodeList[idx].setOrigin(new Vector3(cx, cy, .0f));
                NodeList[idx].setScale(0.5f);
                NodeList[idx].setColor(gl4, new Vector4(0.69f, 0.88f, 0.9f,1.0f));
            }
        }
        NodeList[9].setOrigin(new Vector3(.0f,0.8f,.0f));
        NodeList[9].setScale(0.5f);
        NodeList[9].setRotation(3.14f/8);
        NodeList[9].setColor(gl4, new Vector4(0.69f, 0.88f, 0.9f,1.0f));
//        mtriangle = factory.createNode(gl4, pmNodeShapeFactory.SHAPE_RECTANGLE);
//        mtriangle.setColor(gl4, new Vector4(1.0f, .0f, .0f, 1.0f));
        //mtriangle.setOrigin(new Vector3(-0.5f,.0f,.0f));
        //mtriangle.setRotation(3.14f);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        gl4.glUseProgram(program);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        for(pmBasicNodeShape node : NodeList)
            factory.drawNode(gl4, node, gshaderParam);
        //debugWriteToFile();
    }
    private void debugWriteToFile(){
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
        for(int i=0;i<numOfNodes;i++)
            NodeList[i].gsthForDraw.dispose(gl4);
//        mtriangle.gsthForDraw.dispose(gl4);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        gl4.glClearColor(0.2f, 0.2f, 0.2f,1.0f);
        gl4.glViewport(x, y, width, height);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        lastMousePosition = new Vector2(e.getX(), e.getY());
        float posx = 2*(float) lastMousePosition.x/ commonUtil.DEMO_VIEWPORT_SIZE.x-1;
        float posy = 1.0f-(2*(float) lastMousePosition.y/commonUtil.DEMO_VIEWPORT_SIZE.y);

        System.out.println(lastMousePosition);
        for(int i=0;i<numOfNodes;i++){
            pmBasicNodeShape node = NodeList[i];
            if(node.isHit(posx, posy)){
                node.setColor(gl4, new Vector4(.0f,1.0f,.0f,1.0f));
                node.dirty = true;
                System.out.println("HIT - " + i + " " + times);
                times++;
            }
        }

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

