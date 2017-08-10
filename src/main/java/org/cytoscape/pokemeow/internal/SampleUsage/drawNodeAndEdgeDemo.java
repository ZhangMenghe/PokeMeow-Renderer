package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

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
    private Byte[] Type = {0,1,2,3,4,5,6,7,8,9};
    private int mouseState = -1;
    private Integer reactNodeId = -1;
    private int numOfNodes = 0;
    private int numOfEdges = 0;
    private pmShaderParams gshaderParamNode;
    private int programNode;
    private boolean needFirstCheck = true;
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
        if (reactNodeId != -1) {
            nodeList.get(reactNodeId).setOrigin(new Vector2(posx, posy));
            for (Integer index : NodeEdgeMap.get(reactNodeId)) {
                //change src of edge
                if (index >= 0)
                    edgeList.get(index).resetSrcAndDest(posx, posy, 1);
                else//change dest
                    edgeList.get(-index).resetSrcAndDest(posx, posy, 0);
            }
        }

    }

    private Integer hitNode(float posx, float posy) {
        int idx = 0;
        for (pmBasicNodeShape node : nodeList) {
            if (node.isHit(posx, posy)) {
                node.setColor(gl4, colorList[1]);
                node.dirty = true;
                return idx;
            }
            idx++;
        }
        return -1;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2 * (float) lastMousePosition.x / commonUtil.DEMO_VIEWPORT_SIZE.x - 1;
        float posy = 1.0f - (2 * (float) lastMousePosition.y / commonUtil.DEMO_VIEWPORT_SIZE.y);
        if(e.getButton() == 1)
            reactNodeId = hitNode(posx, posy);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastMousePosition.x = e.getX();
        lastMousePosition.y = e.getY();
        float posx = 2 * (float) lastMousePosition.x / commonUtil.DEMO_VIEWPORT_SIZE.x - 1;
        float posy = 1.0f - (2 * (float) lastMousePosition.y / commonUtil.DEMO_VIEWPORT_SIZE.y);
        if (e.getButton() == 3) {
            if (e.isShiftDown()) {
                  edgeList.add(edgeFactory.createEdge(pmLineFactory.LINE_SOLID, pmLineVisual.LINE_CUBIC_CURVE, nodeList.get(0).origin.x,nodeList.get(0).origin.y,posx,posy,false));
//                edgeList.add(edgeFactory.createEdge(Type[numOfEdges%2], pmLineVisual.LINE_STRAIGHT, nodeList.get(0).origin.x,nodeList.get(0).origin.y,posx,posy,false));
                if(needFirstCheck){
                    if(edgeList.get(numOfEdges)._destArrow!=null){
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

            }
            if(e.isControlDown()){
                pmBasicNodeShape node = nodeFactory.createNode(Type[numOfNodes%10]);
                node.setOrigin(new Vector2(posx, posy));
                node.setColor(gl4, colorList[numOfNodes%2]);
                node.setScale(0.5f);
                nodeList.add(node);
                numOfNodes++;
            }
        }
        if(e.getButton() == 1){
            if(e.isShiftDown())
                mouseState = 1;
            if(e.isControlDown())
                mouseState = 2;
            return;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e){
        mouseState = -1;
    }

}
