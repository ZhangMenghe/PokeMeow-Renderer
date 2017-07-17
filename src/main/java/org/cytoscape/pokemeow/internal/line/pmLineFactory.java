package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

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
    public static final byte LINE_SEPARATE_SLASH = 12;


    private GL4 gl4;
    private int lineSegments = 50;
    private int numOfPoints;
    public pmLineFactory(GL4 gl){
        gl4 = gl;
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
    }

    public pmLineVisual createLine_GL(Byte type) {
        float base;

        numOfPoints = 3*(lineSegments+1);
        float[] pos = new float[numOfPoints];
        base = 2.0f/lineSegments;
        int numOfPatterns;
        int pointsPerPattern;
        float shrink;
        float offset = -1.0f;
        float[] posPattern1;
        switch (type) {
            case 0:
                return new pmSolidLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 1:
                return new pmEqualDashLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 2:
                return new pmDashLongLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 3:
                return new pmDashDotLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 4:
                return new pmDotLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 5:
                return new pmSineWaveLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 6:
                return new pmZigZagLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 7:
                return new pmVerticalSlashLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 8:
                return new pmForwardSlashLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 9:
                return new pmBackwardSlashLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 10:
                return new pmContiguousArrowLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            case 11:
                return new pmParallelLine(gl4, pmLineVisual.DRAWMETHOD_GL, new pmEqualDashLine(gl4, pmLineVisual.DRAWMETHOD_GL));
            case 12:
                return new pmSeparateArrowLine(gl4, pmLineVisual.DRAWMETHOD_GL);
            default:
                return new pmSolidLine(gl4, pmLineVisual.DRAWMETHOD_GL);
        }
    }

    public void drawLine_GL(GL4 gl4, pmLineVisual line, pmShaderParams gshaderParam){
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
                for(int i=0;i<5;i++){
                    pmBasicArrowShape arrow = line.patternList[i];
                    gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(arrow.modelMatrix.asArrayCM()));
                    gl4.glUniform4f(gshaderParam.vec4_color, arrow.color.x, arrow.color.y, arrow.color.z,arrow.color.w);
                    gl4.glBindVertexArray(arrow.objects[arrow.VAO]);
                    gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.VBO]);
                    gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.EBO]);
                    gl4.glDrawElements(GL4.GL_TRIANGLE_FAN,arrow.numOfIndices, GL.GL_UNSIGNED_INT,0);
                    gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
                    gl4.glBindVertexArray(0);
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
                }
                break;
            default:
                gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, line.numOfVertices);
                break;
        }
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }

    public void drawLine_AGG(GL4 gl4, pmLineVisual line, pmShaderParams gshaderParam){
            if(line.numOfIndices == -1)
                gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, line.numOfVertices);
            else{
                gl4.glBindBuffer(GL_ARRAY_BUFFER, line.objects[line.EBO]);
                gl4.glDrawElements(GL4.GL_LINE,line.numOfIndices, GL.GL_UNSIGNED_INT,0);
                gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
            }
    }

    public void drawLine(GL4 gl4, pmLineVisual line, pmShaderParams gshaderParam){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(line.modelMatrix.asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, line.color.x, line.color.y, line.color.z,line.color.w);
        gl4.glBindVertexArray(line.objects[line.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, line.objects[line.VBO]);
        if(line.drawMethod == pmLineVisual.DRAWMETHOD_GL)
            drawLine_GL(gl4, line, gshaderParam);
        else
            drawLine_AGG(gl4, line, gshaderParam);
        gl4.glBindVertexArray(0);
    }

    public void drawLineList(GL4 gl4, pmLineVisual[] lineList, pmShaderParams gshaderParam){
        for(pmLineVisual line: lineList)
            drawLine(gl4, line, gshaderParam);
    }
}
