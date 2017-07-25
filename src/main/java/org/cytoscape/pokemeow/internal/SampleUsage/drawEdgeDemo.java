package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmDeltaArrowShape;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmDiscArrowShape;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.edge.pmEdge;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmSolidLine;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

/**
 * Created by ZhangMenghe on 2017/7/25.
 */
public class drawEdgeDemo extends Demo{
    private pmEdge[] edgeList;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);

        pmLineVisual line = new pmSolidLine(gl4,-0.5f,-0.5f,
                0.5f,0.5f,pmLineVisual.LINE_CUBIC_CURVE);
        pmBasicArrowShape srcArrow = new pmDeltaArrowShape(gl4);
        pmBasicArrowShape destArrow= new pmDeltaArrowShape(gl4);
        edgeList = new pmEdge[1];
        edgeList[0] = new pmEdge(gl4,line,srcArrow,destArrow);
    }
    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        for(pmEdge edge:edgeList)
            edge.draw(gl4,gshaderParam);
    }
    @Override
    public void dispose(GLAutoDrawable drawable) {

    }
    @Override
    public void mouseDragged(MouseEvent e) {
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2*(float) lastMousePosition.x/ commonUtil.DEMO_VIEWPORT_SIZE.x-1;
        float posy = 1.0f-(2*(float) lastMousePosition.y/commonUtil.DEMO_VIEWPORT_SIZE.y);

        for(pmEdge edge : edgeList){
            if(edge.curveType == pmLineVisual.LINE_STRAIGHT)
                continue;
            else if(edge.isAnchorHit(posx, posy,1)){
                System.out.println("HIT ANCHOR1 - " + times);
                times++;
                edge.setControlPoints(posx, posy,1);
                return;
            }
            else if(edge.isAnchorHit(posx, posy,2)){
                System.out.println("HIT ANCHOR2 - " + times);
                times++;
                edge.setControlPoints(posx, posy, 2);
                return;
            }
        }
    }
}
