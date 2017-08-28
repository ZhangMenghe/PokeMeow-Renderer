package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.line.pmAnchor;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmSimpleNodesFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ZhangMenghe on 2017/8/24.
 */
public class drawSimpleNodesDemo extends Demo{
    private pmSimpleNodesFactory simpleNodesFactory;
    private ArrayList<pmAnchor> nodeList;
    private Random random;
    private int nFrame = 0;
    private  long lastTime;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/simplest.vert"),
                null,null,null,
                Demo.class.getResource("shader/simplest.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        simpleNodesFactory = new pmSimpleNodesFactory(gl4);
        nodeList = new ArrayList();
        random = new Random();
        lastTime = System.currentTimeMillis();
        for(int i=0;i<500000;i++)
            nodeList.add(simpleNodesFactory.createSimpleNode(random.nextFloat() * 2-1.0f, random.nextFloat() * 2-1.0f));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        double currentTime = System.currentTimeMillis();
        nFrame++;
        if ( currentTime - lastTime >= 1000 ) { // If last prinf() was more than 1 sec ago
            // printf and reset timer
            System.out.println(1000.0 / nFrame + "ms/frame");
            nFrame = 0;
            lastTime += 1000;

        }
        super.display(drawable);
        for(pmAnchor node: nodeList)
            simpleNodesFactory.drawSimpleNode(node, gshaderParam,true);
    }

    @Override
    public void dispose(GLAutoDrawable drawable){
    }

    @Override
    public void reSetMatrix(boolean viewChanged){
    }

    public void mouseClicked(MouseEvent e) {
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2 * (float) lastMousePosition.x / viewportSize.x - 1;
        float posy = 1.0f - (2 * (float) lastMousePosition.y / viewportSize.y);
        System.out.println(posx + "   " + posy);
        nodeList.add(simpleNodesFactory.createSimpleNode(posx,posy));
    }
}
