package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/7/6.
 */
public class pmLineFactory {
    public static final byte LINE_SOLID = 0;
    public static final byte LINE_DASH_EQUAL = 1;
    public static final byte LINE_DASH_LONG = 2;
    public static final byte LINE_DASH_DOT = 3;
    public static final byte LINE_DOTS = 4;
    public static final byte LINE_SIN = 5;
    public static final byte LINE_ZIGZAG = 6;

    public static final byte LINE_VERTICAL_SLASH = 7;
    public static final byte LINE_FORWARD_SLASH = 8;
    public static final byte LINE_BACKWARD_SLASH = 9;
    public static final byte LINE_CONTIGUOUS_ARROW = 10;
    public static final byte LINE_PARALLEL = 11;
    public static final byte LINE_SEPARATE_ARROW = 12;

    private GL4 gl4;

    public pmLineFactory(GL4 gl){
        gl4 = gl;
        gl4.glEnable(GL4.GL_LINE_SMOOTH);
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
    }
    public pmLineVisual createLine(Byte type, float srcx, float srcy, float destx, float desty, Byte curveType) {
        switch (type) {
            case 0:
                return new pmSolidLine(gl4, srcx, srcy, destx, desty, curveType);
            case 1:
                return new pmEqualDashLine(gl4, srcx, srcy, destx, desty, curveType);
            case 2:
                return new pmDashLongLine(gl4, srcx, srcy, destx, desty, curveType);
            case 3:
                return new pmDashDotLine(gl4, srcx, srcy, destx, desty, curveType);
            case 4:
                return new pmDotLine(gl4, srcx, srcy, destx, desty, curveType);
            case 5:
                return new pmSineWaveLine(gl4, srcx, srcy, destx, desty, curveType);
            case 6:
                return new pmZigZagLine(gl4, srcx, srcy, destx, desty, curveType);
            case 7:
                return new pmVerticalSlashLine(gl4, srcx, srcy, destx, desty, curveType);
            case 8:
                return new pmForwardSlashLine(gl4, srcx, srcy, destx, desty, curveType);
            case 9:
                return new pmBackwardSlashLine(gl4, srcx, srcy, destx, desty, curveType);
            case 10:
                return new pmContiguousArrowLine(gl4, srcx, srcy, destx, desty, curveType);
            case 11:
                return new pmParallelLine(gl4, new pmEqualDashLine(gl4, srcx, srcy, destx, desty, curveType));
            case 12:
                return new pmSeparateArrowLine(gl4, srcx, srcy, destx, desty, curveType);
            default:
                return new pmSolidLine(gl4, srcx, srcy, destx, desty, curveType);
        }
    }


    private void drawLine_GL(GL4 gl4, pmLineVisual line, pmShaderParams gshaderParam){
        switch (line.connectMethod){
            case pmLineVisual.CONNECT_STRIP:
                gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, line.numOfVertices);
                break;
            case pmLineVisual.CONNECT_SEGMENTS:
                gl4.glDrawArrays(GL4.GL_LINES, 0, line.numOfVertices);
                break;
            case pmLineVisual.CONNECT_DOTS:
                gl4.glPointSize(3.0f);
                gl4.glDrawArrays(GL4.GL_POINTS, 0, line.numOfVertices);
                break;
            case pmLineVisual.CONNECT_ARRAY:
                gl4.glBindBuffer(GL_ARRAY_BUFFER, line.objects[line.EBO]);
                gl4.glDrawElements(GL4.GL_TRIANGLES,line.numOfIndices, GL.GL_UNSIGNED_INT,0);
                gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
                break;
            case pmLineVisual.CONNECT_PATTERN:
                for(int i=0;i<line.numOfPatterns;i++){
                    pmBasicArrowShape arrow = line.patternList[i];
                    gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(arrow.modelMatrix.asArrayCM()));
                    gl4.glUniform4f(gshaderParam.vec4_color, arrow.color.x, arrow.color.y, arrow.color.z,arrow.color.w);
                    gl4.glBindVertexArray(arrow.objects[arrow.VAO]);
//                    gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.VBO]);
                    gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.EBO]);
                    gl4.glDrawElements(GL4.GL_TRIANGLE_FAN,arrow.numOfIndices, GL.GL_UNSIGNED_INT,0);
                    gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
//                    gl4.glBindVertexArray(0);
                }
                break;
            case pmLineVisual.CONNECT_PARALLEL:
                for(int i=0; i<2; i++){
                    pmLineVisual mline = line.plineList[i];
                    gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(mline.modelMatrix.asArrayCM()));
                    gl4.glUniform4f(gshaderParam.vec4_color, mline.color.x, mline.color.y, mline.color.z,mline.color.w);
                    gl4.glBindVertexArray(mline.objects[mline.VAO]);
                    gl4.glBindBuffer(GL_ARRAY_BUFFER, mline.objects[mline.VBO]);
                    drawLine_GL(gl4, mline, gshaderParam);
                    gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
                    gl4.glBindVertexArray(0);
                }
                break;
            default:
                gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, line.numOfVertices);
                break;
        }
    }

    public void drawLine(GL4 gl4, pmLineVisual line, pmShaderParams gshaderParam){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(line.modelMatrix.asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, line.color.x, line.color.y, line.color.z,line.color.w);
        gl4.glBindVertexArray(line.objects[line.VAO]);

        if(line.dirty){
            line.data_buff = Buffers.newDirectFloatBuffer(line.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  line.objects[line.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, line.data_buff.capacity() * Float.BYTES, line.data_buff);
        }
        drawLine_GL(gl4, line, gshaderParam);
        gl4.glPointSize(10.0f);

        if(line.curveType == pmLineVisual.LINE_QUADRIC_CURVE){
            gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(line.modelMatrix.asArrayCM()));
            gl4.glUniform4f(gshaderParam.vec4_color, 1.0f, .0f, .0f,.0f);
            drawAnchorPoint(line.anchor, line.dirty);
        }
        if(line.curveType == pmLineVisual.LINE_CUBIC_CURVE){
            gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(line.modelMatrix.asArrayCM()));
            gl4.glUniform4f(gshaderParam.vec4_color, 1.0f, .0f, .0f,.0f);
            drawAnchorPoint(line.anchor, line.dirty);
            drawAnchorPoint(line.anchor2, line.dirty);
        }
        line.dirty = false;
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }

    private void drawAnchorPoint(pmAnchor anchor, boolean dirty){
        gl4.glBindVertexArray(anchor.objects[anchor.VAO]);
        if(dirty){
            anchor.data_buff = Buffers.newDirectFloatBuffer(anchor.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  anchor.objects[anchor.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, 3 * Float.BYTES, anchor.data_buff);
        }
        gl4.glDrawArrays(GL4.GL_POINTS, 0, 1);

    }

    public void drawLineList(GL4 gl4, pmLineVisual[] lineList, pmShaderParams gshaderParam){
        for(pmLineVisual line: lineList)
            drawLine(gl4, line, gshaderParam);
    }
}
