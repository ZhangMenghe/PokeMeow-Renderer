package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GLAutoDrawable;
import com.sun.corba.se.impl.logging.POASystemException;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmDeltaArrowShape;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.edge.pmEdge;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmSolidLine;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
/**
 * Created by ZhangMenghe on 2017/7/25.
 */
public class drawEdgeDemo extends Demo{
    private pmEdge[] edgeList;
    private Vector4 []colorList = {
            new Vector4(0.97f,0.67f,0.65f,1.0f),
            new Vector4(0.69f, 0.88f, 0.9f,1.0f)
    };
    private int mouseState = -1;
    private pmShaderParams arrowParam;
    private float srcx = -0.5f;
    private float srcy = -0.5f;
    private float destx = 0.5f;
    private float desty = 0.5f;
    private  int program2;
    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/line.frag"));
        program2 = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        arrowParam = new pmShaderParams(gl4, program2);
        numOfItems = 1;
//
//        pmLineVisual line = new pmSolidLine(gl4,srcx, srcy, destx, desty,pmLineVisual.LINE_STRAIGHT);
//        pmBasicArrowShape srcArrow = new pmDeltaArrowShape(gl4);
//        pmBasicArrowShape destArrow= new pmDeltaArrowShape(gl4);
        edgeList = new pmEdge[numOfItems];
//        edgeList[0] = new pmEdge(gl4,line,srcArrow,destArrow);

//        edgeList[0] = new pmEdge(gl4, pmLineFactory.LINE_SOLID, pmLineVisual.LINE_STRAIGHT, pmArrowShapeFactory.SHAPE_ARROWHEAD_SHORT, pmArrowShapeFactory.SHAPE_DELTA,
//                -0.5f,-0.5f, 0.5f,0.5f);
//        int n = 0;
//        for(Byte i=0;i<numOfItems;i++) {
//            float cy = -0.6f + 0.1f * n;
//            edgeList[n++] = new pmEdge(gl4, pmLineFactory.LINE_SOLID, pmLineVisual.LINE_QUADRIC_CURVE, i,i,
//                       cy,-0.5f,cy,0.5f);
//        }
        edgeList[0] = new pmEdge(gl4, pmLineFactory.LINE_SEPARATE_ARROW, pmLineVisual.LINE_QUADRIC_CURVE, pmArrowShapeFactory.SHAPE_ARROWHEAD,pmArrowShapeFactory.SHAPE_ARROWHEAD,
                srcx, srcy, destx, desty);
//        edgeList[0].setOrigin(new Vector2(.0f,.0f));
//        edgeList[0].resetSrcAndDest(-0.8f,.0f,0.8f,.0f);
//        edgeList[1] = new pmEdge(gl4, pmLineFactory.LINE_SOLID, pmLineVisual.LINE_QUADRIC_CURVE, pmLineFactory.LINE_SOLID,pmLineFactory.LINE_SOLID,
//                0.2f,0.5f,0.2f,-0.5f);
//        edgeList[0].resetSrcAndDest(.0f, .0f, 0.25f, 0.25f);
//////        edgeList[0].setOrigin(new Vector2(.0f,0.5f));
//        edgeList[0].setRotation(3.14f/4);

    }

    @Override
    public void display(GLAutoDrawable drawable) {
//        super.display(drawable);
        for(pmEdge edge:edgeList)
            edge.draw(gl4, gshaderParam, arrowParam, program, program2);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for(pmEdge edge:edgeList)
            edge.dispose(gl4);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Vector2 diff = new Vector2();

        if (lastMousePosition == null)
        {
            lastMousePosition = new Vector2(e.getX(), e.getY());
        }
        else
        {
            Vector2 newPosition = new Vector2(e.getX(), e.getY());
            diff = Vector2.subtract(newPosition, lastMousePosition);
            diff.y *= -1.0f;
            lastMousePosition = newPosition;
        }
        float posx = 2*(float) lastMousePosition.x/ commonUtil.DEMO_VIEWPORT_SIZE.x-1;
        float posy = 1.0f-(2*(float) lastMousePosition.y/ commonUtil.DEMO_VIEWPORT_SIZE.y);
        if(mouseState==-1){
            return;
        }
        pmEdge hitEdge;
        switch (mouseState){
            case 0:
                hitEdge =edgeList[0];//= hitEdge(posx,posy);
//                if(hitEdge!=null){
                    float currentAngle =(float) Math.atan(diff.y/diff.x)/20;
                    hitEdge.setRotation(currentAngle);
//                }
                break;
            case 1:
                hitEdge = edgeList[0];//= hitEdge(posx,posy);
                hitEdge.setOrigin(new Vector2(posx, posy));
//                edgeList[0].setOrigin(new Vector2(.0f,.0f));
                //System.out.println(posx +"-"+posy);
                break;
            case 3:
                tackleAnchor(posx, posy);
                break;
            case 2:
                edgeList[0].resetSrcAndDest(srcx,srcy,posx,posy);
                break;
            default:
                tackleAnchor(posx, posy);
        }

    }
    private void tackleAnchor(float posx, float posy){
        for(pmEdge edge : edgeList){
            if(edge.curveType == pmLineVisual.LINE_STRAIGHT)
                continue;
            else if(edge.isAnchorHit(posx, posy,1)){
//                System.out.println("HIT ANCHOR1 - " + times);
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
    private pmEdge hitEdge(float posx, float posy){

        for(pmEdge edge : edgeList){
            if(edge.isHit(posx, posy)){
//                System.out.println("HIT - " + times);
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
    @Override
    public void mousePressed(MouseEvent e){
        if(e.getButton() ==1){
            if(e.isShiftDown())
                mouseState = 3;//try reset
            else
                mouseState = 2;
            return;
        }
        if(e.getButton()==3){
            if(e.isControlDown())
                mouseState = 0;//rotate
            else
                mouseState = 1;
        }

    }
    @Override
    public void mouseReleased(MouseEvent e){
        mouseState = -1;
    }

}
