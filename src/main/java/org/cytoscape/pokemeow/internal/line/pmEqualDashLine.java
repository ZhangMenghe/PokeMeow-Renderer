package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmEqualDashLine extends pmLineVisual{
    public pmEqualDashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, byte type, boolean initBuffer){
        super(gl4, srcx, srcy, destx, desty, type, initBuffer);
        connectMethod = CONNECT_SEGMENTS;
        if(curveType == LINE_STRAIGHT) {
            lineSegments=20;
            float rlen;
            if(slope>1 || slope<-1)
                rlen = desty - srcy;
            else
                rlen = destx - srcx;
            numOfVertices = (int) (lineSegments * Math.abs(rlen)) + 1;
            int numOfPoints = 3 * numOfVertices;
            vertices = new float[numOfPoints];
            float shrink = 1.0f / (numOfVertices - 1);
            for (int i = 0, n = 0; i < numOfPoints; i += 3, n++) {
                vertices[i] = -0.5f + shrink * n;
                vertices[i + 1] = .0f;
                vertices[i + 2] = zorder;
            }
        }
        if(initBuffer)
            initLineVisual(gl4);
    }
}
