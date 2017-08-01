package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmDotLine extends pmLineVisual{
    public pmDotLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT){
            lineSegments = 20;
            float rlen = destx-srcx;
            numOfVertices = (int)(lineSegments * Math.abs(rlen)) +1;
            int numOfPoints = 3*numOfVertices;
            vertices = new float[numOfPoints];
            float shrink = rlen/(numOfVertices-1);
            for (int i = 0, n = 0; i < numOfPoints; i += 3, n++) {
                vertices[i] = srcx + shrink * n;
                vertices[i + 1] = srcy;// + slope * (vertices[i] - srcx);
                vertices[i + 2] = zorder;
            }
        }
        connectMethod = CONNECT_DOTS;
        initLineVisual(gl4);
    }
}