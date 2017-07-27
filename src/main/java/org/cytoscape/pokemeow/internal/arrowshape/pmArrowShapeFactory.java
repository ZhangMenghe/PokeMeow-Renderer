package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

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

    public pmBasicArrowShape createArrow(Byte type){
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

    public void drawArrow(GL4 gl4, pmBasicArrowShape arrow, pmShaderParams gshaderParam){
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
            gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.EBO]);
            gl4.glDrawElements(GL4.GL_TRIANGLE_STRIP,arrow.numOfIndices, GL4.GL_UNSIGNED_INT,0);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        }

        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }
    
    public void drawArrowList(GL4 gl4, pmBasicArrowShape[] arrowList, pmShaderParams gshaderParam){
        for(pmBasicArrowShape arrow: arrowList){
            drawArrow(gl4, arrow, gshaderParam);
        }
    }
}
