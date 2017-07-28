package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
/**
 * Created by ZhangMenghe on 2017/7/6.
 */

public class drawCurveDemo extends Demo {
    private pmLineVisual[] lineList;
    private pmLineFactory factory;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        numOfItems = 13;
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmLineFactory(gl4);
        lineList = new pmLineVisual[numOfItems];

        int n = 0;
        for(Byte i=0;i<13;i++){
            float cy = -0.9f + 0.1f * n;
            lineList[n++] = factory.createLine(i,cy,-1.0f,
                    cy,1,
                                                    pmLineVisual.LINE_QUADRIC_CURVE);
//            lineList[n-1].setRotation(3.14f/2);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        factory.drawLineList(gl4, lineList, gshaderParam);
    }

    public void reSetMatrix(boolean viewChanged){}

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for(pmLineVisual line : lineList)
            line.dispose(gl4);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
//        if(Math.abs(e.getX() - lastMousePosition.x) + Math.abs(e.getY() - lastMousePosition.y) < 3.0f)
//            return;
//        System.out.println("---drag---");
//        System.out.println(lastMousePosition);
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2*(float) lastMousePosition.x/ commonUtil.DEMO_VIEWPORT_SIZE.x-1;
        float posy = 1.0f-(2*(float) lastMousePosition.y/commonUtil.DEMO_VIEWPORT_SIZE.y);

        for(pmLineVisual line : lineList){
            if(line.curveType == pmLineVisual.LINE_STRAIGHT)
                continue;
            else if(line.anchor.isHit(posx, posy)){
                System.out.println("HIT ANCHOR1 - " + times);
                times++;
                line.setControlPoints(posx, posy,1);
                return;
            }
            else if(line.anchor2!=null && line.anchor2.isHit(posx, posy)){
                System.out.println("HIT ANCHOR2 - " + times);
                times++;
                line.setControlPoints(posx, posy, 2);
                return;
            }
        }
    }
}
