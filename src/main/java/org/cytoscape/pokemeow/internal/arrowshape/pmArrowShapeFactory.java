package org.cytoscape.pokemeow.internal.arrowshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import org.cytoscape.pokemeow.internal.rendering.pmEdgeBuffer;
import org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class pmArrowShapeFactory {
    public static final byte SHAPE_ARROWHEAD = 0;
    public static final byte SHAPE_ARROWHEAD_SHORT = 1;
    public static final byte SHAPE_DELTA = 2;
    public static final byte SHAPE_DELTA_SHORT = 3;
    public static final byte SHAPE_DELTA_SHORT2 = 4;
    public static final byte SHAPE_DIAMOND = 5;
    public static final byte SHAPE_DIAMOND_SHORT = 6;
    public static final byte SHAPE_DIAMOND_SHORT2 = 7;
    public static final byte SHAPE_DISC = 8;
    public static final byte SHAPE_HALFBOTTOM = 9;
    public static final byte SHAPE_HALFTOP = 10;
    public static final byte SHAPE_TEE = 11;

    private GL4 gl4;

    public pmArrowShapeFactory(GL4 gl){
        gl4 = gl;
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
    }

    public pmBasicArrowShape createArrow(byte type, boolean initBuffer){
        if(initBuffer)
            return createArrowWithBuffer(type);
        else
            return createArrowWithoutBuffer(type);
    }

    private pmBasicArrowShape createArrowWithBuffer(byte type){
        switch (type) {
            case SHAPE_ARROWHEAD:
                return new pmArrowheadShape(gl4);
            case SHAPE_ARROWHEAD_SHORT:
                return new pmShortArrowheadShape(gl4);
            case SHAPE_DELTA:
                return new pmDeltaArrowShape(gl4);
            case SHAPE_DELTA_SHORT:
                return new pmShortDeltaArrowShape(gl4);
            case SHAPE_DELTA_SHORT2:
                return new pmShort2DeltaArrowShape(gl4);
            case SHAPE_DIAMOND:
                return new pmDiamondArrow(gl4);
            case SHAPE_DIAMOND_SHORT:
                return new pmShortDiamondArrowShape(gl4);
            case SHAPE_DIAMOND_SHORT2:
                return new pmShort2DiamondArrowShape(gl4);
            case SHAPE_DISC:
                return new pmDiscArrowShape(gl4);
            case SHAPE_HALFBOTTOM:
                return new pmHalfBottomArrowShape(gl4);
            case SHAPE_HALFTOP:
                return new pmHalfTopArrowShape(gl4);
            case SHAPE_TEE:
                return new pmTeeArrowShape(gl4);
            default:
                return new pmDeltaArrowShape(gl4);
        }
    }

    private pmBasicArrowShape createArrowWithoutBuffer(byte type){
        switch (type) {
            case SHAPE_ARROWHEAD:
                return new pmArrowheadShape();
            case SHAPE_ARROWHEAD_SHORT:
                return new pmShortArrowheadShape();
            case SHAPE_DELTA:
                return new pmDeltaArrowShape();
            case SHAPE_DELTA_SHORT:
                return new pmShortDeltaArrowShape();
            case SHAPE_DELTA_SHORT2:
                return new pmShort2DeltaArrowShape();
            case SHAPE_DIAMOND:
                return new pmDiamondArrow();
            case SHAPE_DIAMOND_SHORT:
                return new pmShortDiamondArrowShape();
            case SHAPE_DIAMOND_SHORT2:
                return new pmShort2DiamondArrowShape();
            case SHAPE_DISC:
                return new pmDiscArrowShape();
            case SHAPE_HALFBOTTOM:
                return new pmHalfBottomArrowShape();
            case SHAPE_HALFTOP:
                return new pmHalfTopArrowShape();
            case SHAPE_TEE:
                return new pmTeeArrowShape();
            default:
                return new pmDeltaArrowShape();
        }
    }

    public void drawArrow(GL4 gl4, pmBasicArrowShape arrow, pmShaderParams gshaderParam){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(arrow.modelMatrix.asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, arrow.color.x, arrow.color.y, arrow.color.z,arrow.color.w);
        gl4.glBindVertexArray(arrow.objects[arrow.VAO]);
        if(arrow.dirty){
            arrow.data_buff = Buffers.newDirectFloatBuffer(arrow.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  arrow.objects[arrow.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, arrow.data_buff.capacity() * Float.BYTES, arrow.data_buff);
            arrow.dirty = false;
        }
        if(arrow.numOfIndices == -1)
            gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, 0, arrow.numOfVertices);
        else{
            gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, arrow.objects[arrow.EBO]);
            gl4.glDrawElements(GL4.GL_TRIANGLE_STRIP,arrow.numOfIndices, GL4.GL_UNSIGNED_INT,0);
            gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,0);
        }

        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }

    public void drawArrow(GL4 gl4, pmBasicArrowShape arrow, pmShaderParams gshaderParam, pmEdgeBuffer edgeBuffer){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(arrow.modelMatrix.asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, arrow.color.x, arrow.color.y, arrow.color.z,arrow.color.w);
        if(arrow.numOfIndices != -1 && !arrow.isfirst) {
            gl4.glGenVertexArrays(1, arrow.objects, 0);
            gl4.glBindVertexArray(arrow.objects[0]);
            gl4.glEnableVertexAttribArray(0);
            gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT,false, 3*Float.BYTES, arrow.bufferByteOffset);
        }
        else
            gl4.glBindVertexArray(edgeBuffer.objects[edgeBuffer.VAO]);

        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  edgeBuffer.objects[edgeBuffer.VBO]);
        if(arrow.dirty){
            arrow.data_buff = Buffers.newDirectFloatBuffer(arrow.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  edgeBuffer.objects[edgeBuffer.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, arrow.bufferByteOffset, arrow.numOfVertices*12, arrow.data_buff);
            if(arrow.numOfIndices != -1){
                arrow.indice_buff = Buffers.newDirectIntBuffer(arrow.elements);
                gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, edgeBuffer.objects[edgeBuffer.EBO]);
                gl4.glBufferSubData(GL.GL_ELEMENT_ARRAY_BUFFER, arrow.indexByteOffset, arrow.numOfIndices*4, arrow.indice_buff);
            }
            arrow.dirty = false;
        }
        if(arrow.numOfIndices == -1)
            gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, arrow.bufferVerticeOffset, arrow.numOfVertices);
        else{
            gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, edgeBuffer.objects[edgeBuffer.EBO]);
            gl4.glDrawElements(GL4.GL_TRIANGLE_STRIP,arrow.numOfIndices, GL4.GL_UNSIGNED_INT,arrow.indexByteOffset);
            gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,0);
        }
        gl4.glBindVertexArray(0);
    }
    public void drawArrowList(GL4 gl4, pmBasicArrowShape[] arrowList, pmShaderParams gshaderParam){
        if(arrowList == null)
            return;
        for(pmBasicArrowShape arrow: arrowList){
            drawArrow(gl4, arrow, gshaderParam);
        }
    }
}
