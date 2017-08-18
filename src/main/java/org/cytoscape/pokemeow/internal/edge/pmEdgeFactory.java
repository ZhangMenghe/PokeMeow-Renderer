package org.cytoscape.pokemeow.internal.edge;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.algebra.Vector2;
import org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import org.cytoscape.pokemeow.internal.line.pmLineVisual;
import org.cytoscape.pokemeow.internal.rendering.pmEdgeBuffer;
import org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

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

    public pmEdge createEdge(byte lineType, byte mcurveType,
                             float srcx, float srcy, float destx, float desty,boolean initBuffer){
        pmEdge edge =  new pmEdge(gl4,lineType,mcurveType,srcx,srcy,destx,desty,initBuffer);
        commonSetEdgeBufferOff(edge);
        return edge;
    }

    public pmEdge createEdge(byte lineType, byte mcurveType, byte destArrowType,
                             float srcx, float srcy, float destx, float desty,boolean initBuffer){
        pmEdge edge = new pmEdge(gl4,lineType,mcurveType,destArrowType,srcx,srcy,destx,desty,initBuffer);
        commonSetEdgeBufferOff(edge);
        return edge;
    }

    public pmEdge createEdge(byte lineType, byte mcurveType, byte srcArrowType, byte destArrowType,
                             float srcx, float srcy, float destx, float desty,boolean initBuffer){
        pmEdge edge = new pmEdge(gl4,lineType,mcurveType,srcArrowType,destArrowType,srcx,srcy,destx,desty,initBuffer);
        commonSetEdgeBufferOff(edge);
        return edge;
    }

    public pmEdge createEdge(pmLineVisual line, pmBasicArrowShape srcArrow, pmBasicArrowShape destArrow,boolean initBuffer){
        pmEdge edge = new pmEdge(gl4,line,srcArrow,destArrow);
        commonSetEdgeBufferOff(edge);
        return edge;
    }

    private void commonSetEdgeBufferOff(pmEdge edge){
        int[] offsets = edge.setBufferOffset(
                edgeBuffer.dataOffset,
                edgeBuffer.indexOffset,
                edgeBuffer.capacity,
                edgeBuffer.capacityIdx
        );
        edgeBuffer.dataOffset = offsets[0];
        edgeBuffer.indexOffset = offsets[1];

        if(offsets[2] == -1)
            edgeBuffer.shouldBeResize = 0;
        if(offsets[3] == -1)
            edgeBuffer.shouldBeResize = 1;
    }

    public void drawEdge(pmEdge edge, pmShaderParams gshaderParam){
        if(edgeBuffer.shouldBeResize!=-1){
            if(edgeBuffer.shouldBeResize == 0)
                edgeBuffer.doubleVBOSize(gl4);
            else
                edgeBuffer.doubleEBOSize(gl4);
            edgeBuffer.shouldBeResize  = -1;
        }
        edge.draw(gl4, gshaderParam, edgeBuffer);
    }
    public void deleteEdge(GL4 gl4, pmEdge edge){
    //TODO:Whether or not it is necessary to reuse deleted buffer?
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, edgeBuffer.objects[edgeBuffer.VBO]);
        gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, edge._line.bufferByteOffset,edge._line.numOfVertices*12, null);
        if(edge._destArrow!=null)
            releaseArrow(edge._destArrow);
        if(edge._srcArrow != null)
            releaseArrow(edge._srcArrow);
    }
    private void releaseArrow(pmBasicArrowShape arrow){
        gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, arrow.bufferByteOffset,arrow.numOfVertices*12, null);
        if(arrow.numOfIndices == -1){
            gl4.glBufferSubData(GL.GL_ELEMENT_ARRAY_BUFFER, arrow.indexByteOffset,arrow.numOfIndices*4, null);
        }
    }
}
