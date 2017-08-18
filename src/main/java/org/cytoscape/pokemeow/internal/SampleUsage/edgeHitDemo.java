package org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GLAutoDrawable;

import org.cytoscape.pokemeow.internal.algebra.Vector4;

import org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import org.cytoscape.pokemeow.internal.commonUtil;
import org.cytoscape.pokemeow.internal.edge.pmEdge;
import org.cytoscape.pokemeow.internal.line.pmLineFactory;
import org.cytoscape.pokemeow.internal.line.pmLineVisual;
import org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import org.cytoscape.pokemeow.internal.utils.GLSLProgram;


/**
 * Created by ZhangMenghe on 2017/8/3.
 */
public class edgeHitDemo extends Demo{
    private pmEdge[] edgeList;
    private Vector4[]colorList = {
            new Vector4(0.97f,0.67f,0.65f,1.0f),
            new Vector4(0.69f, 0.88f, 0.9f,1.0f)
    };
    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        numOfItems = 13;
        edgeList = new pmEdge[numOfItems];
        int n = 0;
        for(byte i=0;i<numOfItems;i++){
            float cy = -0.9f + 0.1f*n;
            edgeList[n++] = new pmEdge(gl4, i, pmLineVisual.LINE_STRAIGHT, pmArrowShapeFactory.SHAPE_ARROWHEAD,pmArrowShapeFactory.SHAPE_ARROWHEAD,
                    -1.0f,cy,1.0f,cy, true);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        for(pmEdge edge:edgeList)
            edge.draw(gl4, gshaderParam);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for(pmEdge edge:edgeList)
            edge.dispose(gl4);
    }

    private pmEdge hitEdge(float posx, float posy){
        for(pmEdge edge : edgeList){
            if(edge.isHit(posx, posy)){
                System.out.println("HIT - " + times);
                times++;
                edge.setColor(colorList[times%2]);
                return edge;
            }
            else
                System.out.println("MISS - " + times);
        }
        return null;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2*(float) lastMousePosition.x/ commonUtil.DEMO_VIEWPORT_SIZE.x-1;
        float posy = 1.0f-(2*(float) lastMousePosition.y/commonUtil.DEMO_VIEWPORT_SIZE.y);

        hitEdge(posx,posy);
    }

}
