package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmEqualDashLine extends pmLineVisual{
    public pmEqualDashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT){
            float rlen = Math.abs(srcx-destx) + Math.abs(srcy-desty);
            numOfVertices = lineSegments * (int)rlen +1;
            int numOfPoints = 3*numOfVertices;
            vertices = new float[numOfPoints];
            float shrink = rlen/(numOfVertices-1);
            if(Math.abs(slope) <= 1) {
                for (int i = 0, n = 0; i < numOfPoints; i += 3, n++) {
                    vertices[i] = srcx + shrink * n;
                    vertices[i + 1] = srcy + slope * (vertices[i] - srcx);
                    vertices[i + 2] = zorder;
                }
            }
            else{
                float k = 1.0f/slope;
                for (int i = 0, n = 0; i < numOfPoints; i += 3, n++) {
                    float tmpy = srcy+shrink*n;
                    vertices[i] = srcx + k * (tmpy - srcy);
                    vertices[i + 1] = tmpy;
                    vertices[i + 2] = zorder;
                }
            }
        }
        connectMethod = CONNECT_SEGMENTS;
        initLineVisual(gl4, vertices);
    }
}
