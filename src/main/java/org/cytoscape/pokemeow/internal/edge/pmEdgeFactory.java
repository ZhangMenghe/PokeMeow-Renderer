package main.java.org.cytoscape.pokemeow.internal.edge;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmEdgeBuffer;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

/**
 * Created by ZhangMenghe on 2017/8/4.
 */
public class pmEdgeFactory {
    private pmEdgeBuffer edgeBuffer;
    private GL4 gl4;
    public pmEdgeFactory(GL4 gl){
        gl4 = gl;
        gl4.glEnable(GL4.GL_LINE_SMOOTH);
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        edgeBuffer = new pmEdgeBuffer(gl4);
    }
    public pmEdge createEdge(Byte lineType, Byte mcurveType,
                             float srcx, float srcy, float destx, float desty,boolean initBuffer){
        pmEdge edge =  new pmEdge(gl4,lineType,mcurveType,srcx,srcy,destx,desty,initBuffer);
        int[] offsets = edge.setBufferOffset(edgeBuffer.dataOffset, edgeBuffer.indexOffset, edgeBuffer.capacity);
        edgeBuffer.dataOffset = offsets[0];
        edgeBuffer.indexOffset = offsets[1];

        if(offsets[2] != edgeBuffer.capacity){
            edgeBuffer.capacity = offsets[2];
            edgeBuffer.shouldBeResize = true;
        }


        return edge;
    }
    public pmEdge createEdge(Byte lineType, Byte mcurveType,Byte destArrowType,
                             float srcx, float srcy, float destx, float desty,boolean initBuffer){
        return new pmEdge(gl4,lineType,mcurveType,destArrowType,srcx,srcy,destx,desty,initBuffer);
    }
    public pmEdge createEdge(Byte lineType, Byte mcurveType, Byte srcArrowType, Byte destArrowType,
                             float srcx, float srcy, float destx, float desty,boolean initBuffer){
        return new pmEdge(gl4,lineType,mcurveType,srcArrowType,destArrowType,srcx,srcy,destx,desty,initBuffer);
    }
    public pmEdge createEdge(pmLineVisual line, pmBasicArrowShape srcArrow, pmBasicArrowShape destArrow,boolean initBuffer){
        return new pmEdge(gl4,line,srcArrow,destArrow);
    }
    public void drawEdge(pmEdge edge, pmShaderParams gshaderParam){
        if(edgeBuffer.shouldBeResize){
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  edgeBuffer.objects[edgeBuffer.VBO]);
            gl4.glBufferData(GL.GL_ARRAY_BUFFER, edgeBuffer.capacity, null, GL.GL_DYNAMIC_DRAW);
            edgeBuffer.shouldBeResize  = false;
        }
        edge.draw(gl4, gshaderParam, edgeBuffer);

    }
}
