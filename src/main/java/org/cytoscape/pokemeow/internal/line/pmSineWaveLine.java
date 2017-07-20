package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmSineWaveLine extends pmLineVisual{
    private int lineSegments = 50;
    private int period = 40;
    private float baseW = 0.174f;//baseW is approximate 2*pi/360
    public pmSineWaveLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT){
            float deltay = desty - srcy;
            float deltax = destx - srcx;
            float k = deltay / deltax;
            float length =(float) Math.sqrt(deltay*deltay + deltax*deltax);

            float rlen = Math.abs(srcx-destx) + Math.abs(srcy-desty);
            int pointNum = lineSegments * (int)rlen;
            numOfVertices = 3*(pointNum+1);

            vertices = new float[numOfVertices];
            float shrink = length/pointNum;
            float exampleX = period*baseW;
            double theta = Math.atan(k);
            float cost = (float)Math.cos(theta);
            float sint = (float)Math.sin(theta);
            float gapx = .0f,gapy = .0f;
            for(int i=0, n=0; i<numOfVertices; i+=3, n++){
                float tmpx = srcx + shrink*n;
                float tmpy = (float) Math.sin(exampleX*n)/20;
                vertices[i] = tmpx*cost-tmpy*sint+gapx;
                vertices[i+1] = tmpx*sint+tmpy*cost+gapy;
                if(n==0){
                    gapx = srcx - vertices[0];
                    gapy = srcy - vertices[1];
                    vertices[0] = srcx;
                    vertices[1] = srcy;
                }
                vertices[i+2] = zorder;
            }
        }

        connectMethod = CONNECT_STRIP;
        initLineVisual(gl4, vertices);

    }
}
