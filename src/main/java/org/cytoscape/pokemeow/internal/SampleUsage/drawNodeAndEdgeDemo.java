package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.edge.pmEdge;
import main.java.org.cytoscape.pokemeow.internal.edge.pmEdgeFactory;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ZhangMenghe on 2017/8/3.
 */
public class drawNodeAndEdgeDemo extends Demo {
    private pmNodeShapeFactory nodeFactory;
    private pmEdgeFactory edgeFactory;
    private ArrayList<pmEdge> edgeList;
    private ArrayList<pmBasicNodeShape> nodeList;
    private HashMap<Integer, ArrayList<Integer>> NodeEdgeMap;
    private Vector4[] colorList = {
            new Vector4(0.97f, 0.67f, 0.65f, 1.0f),
            new Vector4(0.69f, 0.88f, 0.9f, 1.0f)
    };
    private byte[] Type = {0,1,2,3,4,5,6,7,8,9,10,11,12};
    private byte mouseState =1;
    private Integer reactNodeId = -1;
    private pmEdge activeEdge;
    private int numOfNodes = 0;
    private int numOfEdges = 0;
    private pmShaderParams gshaderParamNode;
    private int programNode;
    private boolean needFirstCheck = true;
    private int count = 0;
    private final static byte ADD_NODE = 0;
    private final static byte ADD_EDGE = 1;
    private final static byte SELECT_DELETE = 2;
    private final static byte SELECT_CHANGECOLOR = 3;
    private final static byte CHANGE_CONTROLPOINT = 4;
    private final static byte CHANGE_SRCDEST = 5;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null, null, null,
                Demo.class.getResource("shader/arrow.frag"));
        programNode = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/flat.vert"),
                null,null,null,
                Demo.class.getResource("shader/flat.frag"));

        gshaderParam = new pmShaderParams(gl4, program);
        gshaderParamNode = new pmShaderParams(gl4, programNode);

        edgeList = new ArrayList();
        nodeList = new ArrayList();
        nodeFactory = new pmNodeShapeFactory(gl4);
        edgeFactory = new pmEdgeFactory(gl4);
        NodeEdgeMap = new HashMap<>();
        //////////////////////////////////////////////////////////////////
        pmBasicNodeShape node = nodeFactory.createNode(Type[1]);
        node.isfirst = true;
        node.setOrigin(new Vector2(.0f, .0f));
        node.setScale(0.5f);
        node.setColor(gl4,colorList[0]);
        NodeEdgeMap.put(0,new ArrayList<>());
        numOfNodes++;
        nodeList.add(node);
        ////////////////////////////////////////////////////////////////////
    }

        @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        for (int i=0;i<numOfEdges;i++)
            edgeFactory.drawEdge(edgeList.get(i),gshaderParam);
       gl4.glUseProgram(programNode);
//            gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        for (int j=0;j<numOfNodes;j++)
            nodeFactory.drawNode(gl4, nodeList.get(j), gshaderParamNode, true);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for (pmEdge edge : edgeList)
            edge.dispose(gl4);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Vector2 diff;

        if (lastMousePosition == null) {
            lastMousePosition = new Vector2(e.getX(), e.getY());
        } else {
            Vector2 newPosition = new Vector2(e.getX(), e.getY());
            diff = Vector2.subtract(newPosition, lastMousePosition);
            diff.y *= -1.0f;
            lastMousePosition = newPosition;
        }
        float posx = 2 * (float) lastMousePosition.x / commonUtil.DEMO_VIEWPORT_SIZE.x - 1;
        float posy = 1.0f - (2 * (float) lastMousePosition.y / commonUtil.DEMO_VIEWPORT_SIZE.y);
        if(mouseState == ADD_EDGE)
            activeEdge.resetSrcAndDest(posx,posy,false);

//        if (reactNodeId != -1) {
//            nodeList.get(reactNodeId).setOrigin(new Vector2(posx, posy));
//            for (Integer index : NodeEdgeMap.get(reactNodeId)) {
//                //change src of edge
//                if (index >= 0)
//                    edgeList.get(index).resetSrcAndDest(posx, posy, 1);
//                else//change dest
//                    edgeList.get(-index).resetSrcAndDest(posx, posy, 0);
//            }
//        }

    }

    private int hitNode(float posx, float posy) {
        int idx = 0;
        for (pmBasicNodeShape node : nodeList) {
            if (node.isHit(posx, posy)) {
                node.setColor(gl4, colorList[count%2]);
                count++;
                node.dirty = true;
                return idx;
            }
            idx++;
        }
        return -1;
    }
    private void checkAndDelete(float posx, float posy){
        for(int i=numOfNodes-1; i>-1; i-- ){
            pmBasicNodeShape tmp = nodeList.get(i);
            if(tmp.isHit(posx,posy)){
                nodeList.remove(i);
                numOfNodes--;
                nodeFactory.deleteNode(gl4, tmp);
            }
        }
        for(int i=numOfEdges-1; i>-1; i-- ){
            pmEdge tmp = edgeList.get(i);
            if(tmp.isHit(posx,posy)){
                edgeList.remove(i);
                numOfEdges--;
                edgeFactory.deleteEdge(gl4, tmp);
            }
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2 * (float) lastMousePosition.x / commonUtil.DEMO_VIEWPORT_SIZE.x - 1;
        float posy = 1.0f - (2 * (float) lastMousePosition.y / commonUtil.DEMO_VIEWPORT_SIZE.y);
        switch (mouseState){
            case -1:
                System.out.println("No task");
                break;
            case ADD_EDGE:
                System.out.println("Click to add an edge");
                activeEdge = edgeFactory.createEdge(Type[numOfEdges%13], pmLineVisual.LINE_CUBIC_CURVE, .0f,.0f,posx,posy,false);
                edgeList.add(activeEdge);
                if(needFirstCheck){
                    if(edgeList.get(numOfEdges)._destArrow != null){
                        needFirstCheck = false;
                        edgeList.get(numOfEdges)._destArrow.isfirst = true;
                    }
                    else if(edgeList.get(numOfEdges)._line.patternList!=null){
                        needFirstCheck = false;
                        edgeList.get(numOfEdges)._line.patternList[0].isfirst = true;
                    }
                }
                NodeEdgeMap.get(0).add(numOfEdges);
                times++;
                numOfEdges ++;
                break;
            case ADD_NODE:
                numOfNodes++;
                pmBasicNodeShape node = nodeFactory.createNode(Type[numOfNodes%10]);
                if(numOfNodes == 1)
                    node.isfirst = true;
                node.setOrigin(new Vector2(posx, posy));
                node.setColor(gl4, colorList[numOfNodes%2]);
                node.setScale(0.5f);
                nodeList.add(node);
                break;
            case SELECT_DELETE:
                checkAndDelete(posx, posy);
                break;
            case SELECT_CHANGECOLOR:
                reactNodeId = hitNode(posx,posy);
                break;
            default:
                break;
        }
    }
    @Override
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode() == 'n' || e.getKeyCode() == 'N')
            mouseState = ADD_NODE;
        if(e.getKeyCode() == 'e' || e.getKeyCode() == 'E')
            mouseState = ADD_EDGE;
        if(e.getKeyCode() == 'd' || e.getKeyCode() == 'D')
            mouseState = SELECT_DELETE;
        if(e.getKeyCode() == 'c' || e.getKeyCode() == 'C')
            mouseState = SELECT_CHANGECOLOR;
        if(e.getKeyCode() == 'm' || e.getKeyCode() == 'M')
            mouseState = CHANGE_CONTROLPOINT;
        if(e.getKeyCode() == 'v' || e.getKeyCode() == 'V')
            mouseState = CHANGE_SRCDEST;
    }
    @Override
    public void mouseReleased(MouseEvent e){
    }

}
