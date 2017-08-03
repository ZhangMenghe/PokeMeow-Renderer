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
            float rlen;
            if(slope<1)
                rlen = destx - srcx;
            else
                rlen = desty - srcy;
            numOfVertices = (int)(lineSegments * Math.abs(rlen)) +1;
            int numOfPoints = 3*numOfVertices;
            vertices = new float[numOfPoints];
            float shrink = rlen/(numOfVertices-1);
            for (int i = 0, n = 0; i < numOfPoints; i += 3, n++) {
                vertices[i] = -0.5f + shrink * n;
                vertices[i + 1] = .0f;// + slope * (vertices[i] - srcx);
                vertices[i + 2] = zorder;
            }
        }
        connectMethod = CONNECT_DOTS;
        initLineVisual(gl4);
    }
}