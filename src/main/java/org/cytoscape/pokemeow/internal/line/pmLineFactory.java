package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/7/6.
 */
public class pmLineFactory {
    public static final byte LINE_SOLID = 0;
    public static final byte LINE_DASH_EQUAL = 1;
    public static final byte LINE_SIN = 2;
    public static final byte LINE_ZIGZAG = 3;
    public static final byte LINE_DOTS = 4;
    public static final byte LINE_DASH_DOT = 5;
    public static final byte LINE_DASH_LONG = 6;

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
        float[] ipos = {-1.0f, .0f, .0f, 1.0f, .0f, .0f};
        numOfPoints = 3*(lineSegments+1);
        float[] pos = new float[numOfPoints];
        base = 2.0f/lineSegments;
        switch (type) {
            case 0:
                return new pmLineVisual(gl4, ipos);
            case 1:
                for(int i=0, n = 0;i<numOfPoints;i+=3,n++){
                    pos[i] = -1.0f + base*n;
                    pos[i+1] = .0f;
                    pos[i+2] = .0f;
                }
                return new pmLineVisual(gl4, pos, pmLineVisual.DRAWMETHOD_GL, pmLineVisual.CONNECT_SEGMENTS);
            case 2:
                //baseW is approximate 2*pi/360
                float baseW = 0.174f;
                //exampleX is 40*baseW
                float exampleX = 6.98f;
                for(int i=0, n = 0;i<numOfPoints;i+=3,n++){
                    pos[i] = -1.0f + base*n;
                    pos[i+1] = (float) Math.sin(exampleX*n)/20;
                    pos[i+2] = .0f;
                }
                return new pmLineVisual(gl4, pos);
            case 3:
                int []values = {0,1,0,-1};
                for(int i=0, n = 0;i<numOfPoints;i+=3,n++){
                    pos[i] = -1.0f + base*n;
                    pos[i+1] = values[n%4]/5.0f;
                    pos[i+2] = .0f;
                }
                return new pmLineVisual(gl4, pos);
            case 4:
                for(int i=0, n = 0;i<numOfPoints;i+=3,n++){
                    pos[i] = -1.0f + base*n;
                    pos[i+1] = .0f;
                    pos[i+2] = .0f;
                }
                return new pmLineVisual(gl4, pos, pmLineVisual.DRAWMETHOD_GL, pmLineVisual.CONNECT_DOTS);
            case 5:
                pos[0] = -1.0f; pos[1] = .0f; pos[2] = .0f;
                base/=2;
                for(int i=3, n = 1;i<numOfPoints;i+=3,n++){
                    if(n%4==1)
                        pos[i] = pos[i-3] + 5*base;
                    else
                        pos[i] = pos[i-3] + base;
                    pos[i+1] = .0f;
                    pos[i+2] = .0f;
                }
                return new pmLineVisual(gl4, pos, pmLineVisual.DRAWMETHOD_GL, pmLineVisual.CONNECT_SEGMENTS);
            case 6:
                pos[0] = -1.0f; pos[1] = .0f; pos[2] = .0f;
                base/=4;
                for(int i=3, n = 1;i<numOfPoints;i+=3,n++){
                    if(n%2==1)
                        pos[i] = pos[i-3] + 7*base;
                    else
                        pos[i] = pos[i-3] + base;
                    pos[i+1] = .0f;
                    pos[i+2] = .0f;
                }
                pmLineVisual result = new pmLineVisual(gl4, pos, pmLineVisual.DRAWMETHOD_GL, pmLineVisual.CONNECT_SEGMENTS);
                //result.setScale(new Vector2(4f, 1.0f));
                return result;
            default:
                return new pmLineVisual(gl4, ipos);
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
            default:
                gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, line.numOfVertices);
                break;
        }
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
