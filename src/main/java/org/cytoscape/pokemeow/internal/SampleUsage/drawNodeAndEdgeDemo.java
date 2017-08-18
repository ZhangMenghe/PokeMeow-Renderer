package org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.MouseEvent;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import org.cytoscape.pokemeow.internal.algebra.Vector2;
import org.cytoscape.pokemeow.internal.algebra.Vector4;
import org.cytoscape.pokemeow.internal.commonUtil;
import org.cytoscape.pokemeow.internal.edge.pmEdge;
import org.cytoscape.pokemeow.internal.edge.pmEdgeFactory;
import org.cytoscape.pokemeow.internal.line.pmLineFactory;
import org.cytoscape.pokemeow.internal.line.pmLineVisual;
import org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import org.w3c.dom.NodeList;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    private byte mouseState = 0;
    private Integer reactNodeId = -1;
    private pmEdge activeEdge;
    private int numOfNodes = 0;
    private int numOfEdges = 0;
    private pmShaderParams gshaderParamNode;
    private int programNode;
    private boolean needFirstCheck = true;
    private int count = 0;
    private int nFrame = 0;
    private  long lastTime;
    private Random random;
    private IntBuffer queryIDs;
    private ArrayList<Integer> nodeNeedToCheck;
    private final static byte ADD_NODE = 0;
    private final static byte ADD_EDGE = 1;
    private final static byte SELECT_DELETE = 2;
    private final static byte SELECT_CHANGECOLOR = 3;
    private final static byte CHANGE_CONTROLPOINT = 4;
    private final static byte CHANGE_SRCDEST = 5;
    private boolean afterDrag = false;
    private int tmp = 0;

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
        NodeEdgeMap = new HashMap<Integer, ArrayList<Integer>>();
        random = new Random();

        nodeNeedToCheck = new ArrayList();
        //////////////////////////////////////////////////////////////////
//        pmBasicNodeShape node = nodeFactory.createNode(Type[1]);
//        node.setOrigin(new Vector2(.0f, .0f));
//        node.setScale(0.5f);
//        node.setColor(colorList[0]);
//        NodeEdgeMap.put(0,new ArrayList<>());
//        numOfNodes++;
//        nodeList.add(node);
//        pmBasicNodeShape nodem = nodeFactory.createNode(gl4, Type[1]);
//        nodem.setOrigin(new Vector2(.0f, .0f));
//        nodem.setColor(new Vector4(1.0f,.0f,.0f,1.0f));
//        NodeEdgeMap.put(0,new ArrayList<>());
//        numOfNodes++;
//        nodeList.add(nodem);

        for(int i=0;i<10;i++){
            pmBasicNodeShape node = nodeFactory.createNode(Type[numOfNodes%10]);
            node.setOrigin(new Vector2(random.nextFloat() * 2-1.0f, random.nextFloat() * 2-1.0f));
//            node.setOrigin(new Vector2(.0f,.0f));
            node.setScale(random.nextFloat() * 0.5f);
            node.setColor(colorList[0]);
//            NodeEdgeMap.put(0,new ArrayList<Object>());
            numOfNodes++;
            nodeNeedToCheck.add(i);
            nodeList.add(node);
        }

//        gl4.glDepthFunc( GL4.GL_LEQUAL );
        ////////////////////////////////////////////////////////////////////
        lastTime = System.currentTimeMillis();
        DoDepthQuery();
    }

    private void DoDepthQuery(){
        gl4.glUseProgram(programNode);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        gl4.glColorMask(false,false,false,false);
        gl4.glDepthMask(false);
        queryIDs = Buffers.newDirectIntBuffer(new int[numOfNodes]);
        gl4.glGenQueries(numOfNodes, queryIDs);
        IntBuffer tmp = Buffers.newDirectIntBuffer(new int[1]);
        for (int i=0;i<numOfNodes;i++){
            gl4.glBeginQuery(GL4.GL_SAMPLES_PASSED, queryIDs.get(i));

            nodeFactory.drawNode(gl4, nodeList.get(i), gshaderParamNode, true);
            gl4.glEndQuery(GL4.GL_SAMPLES_PASSED);
            gl4.glGetQueryObjectiv(queryIDs.get(i), GL4.GL_QUERY_RESULT, tmp);
            if (tmp.get(0) == 0)
                nodeList.get(i).visible = false;
        }
        gl4.glColorMask(true,true,true,true);
        gl4.glDepthMask(true);
    }

    private void DoDepthQuery(pmBasicNodeShape refNode, int refIdx){
        gl4.glUseProgram(programNode);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        gl4.glColorMask(false,false,false,false);
        gl4.glDepthMask(false);
        queryIDs = Buffers.newDirectIntBuffer(new int[numOfNodes]);
        gl4.glGenQueries(numOfNodes, queryIDs);
        IntBuffer tmp = Buffers.newDirectIntBuffer(new int[1]);
        pmBasicNodeShape node;
        for (int i=0; i<numOfNodes; i++){
            node = nodeList.get(i);
            if(i==refIdx)
                continue;
            if(refNode.isOutsideBoundingBox(node.origin.x, node.origin.y))
                continue;
            gl4.glBeginQuery(GL4.GL_SAMPLES_PASSED, queryIDs.get(i));
            nodeFactory.drawNode(gl4, node, gshaderParamNode);
            gl4.glEndQuery(GL4.GL_SAMPLES_PASSED);
            gl4.glGetQueryObjectiv(queryIDs.get(i), GL4.GL_QUERY_RESULT, tmp);
            if (tmp.get(0) == 0)
                nodeList.get(i).visible = false;
        }
//        nodeList.get(refIdx).visible = true;
        gl4.glColorMask(true,true,true,true);
        gl4.glDepthMask(true);
    }

        @Override
    public void display(GLAutoDrawable drawable) {
        double currentTime = System.currentTimeMillis();
        nFrame++;
        if ( currentTime - lastTime >= 1000 ) { // If last prinf() was more than 1 sec ago
            // printf and reset timer
//            System.out.println(1000.0 / nFrame + "ms/frame");
            nFrame = 0;
            lastTime += 1000;

        }
        super.display(drawable);
        for (int i=0;i<numOfEdges;i++)
            edgeFactory.drawEdge(edgeList.get(i),gshaderParam);
        gl4.glUseProgram(programNode);

        for(pmBasicNodeShape node: nodeList) {
            if (node.visible){
                nodeFactory.drawNode(gl4, node, gshaderParamNode, true);
                tmp++;
            }
        }
//        System.out.println(tmp);
        tmp = 0;
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

        if (reactNodeId != -1) {
            nodeList.get(reactNodeId).setOrigin(new Vector2(posx, posy));
//            for (Integer index : NodeEdgeMap.get(reactNodeId)) {
//                //change src of edge
//                if (index >= 0)
//                    edgeList.get(index).resetSrcAndDest(posx, posy, 1);
//                else//change dest
//                    edgeList.get(-index).resetSrcAndDest(posx, posy, 0);
//            }
        }
        afterDrag = true;
    }

    private int hitNode(float posx, float posy) {
        int idx = 0;
        for (pmBasicNodeShape node : nodeList) {
            if (node.isHit(posx, posy)) {
                node.setColor(colorList[count%2]);
                count++;
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
                node.setOrigin(new Vector2(posx, posy));
                node.setColor(colorList[numOfNodes%2]);
                node.setScale(0.5f);
                node.visible = true;
                nodeList.add(node);
                DoDepthQuery(node, numOfNodes-1);
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
        if(afterDrag && reactNodeId!=-1){
            DoDepthQuery(nodeList.get(reactNodeId), reactNodeId);
            reactNodeId = -1;
            afterDrag = false;
            System.out.println("Drag Release Query");
        }


    }

}
