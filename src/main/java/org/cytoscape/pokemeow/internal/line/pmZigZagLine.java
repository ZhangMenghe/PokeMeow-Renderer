package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmZigZagLine extends pmPatternLineBasic{
    private float height = 20.0f;
    public pmZigZagLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type, boolean initBuffer){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT)
            initStraightVertices(srcx, srcy, destx, desty);
        else{
            singlePattern = new float[30];
            double start = -1.0;
            for(int i=0, n=0;i<30;i+=3,start+=.1,n++){
                singlePattern[i] = (float) start;
                singlePattern[i+1] = (float)start;
                singlePattern[i+2] = zorder;
            }
            pointsPerPattern = 10;
            initCurveVertices();
        }

        connectMethod = CONNECT_STRIP;
        if(initBuffer)
            initLineVisual(gl4);
    }
    protected void initStraightVertices(float srcx, float srcy, float destx, float desty){
        lineSegments = 60;
        float rlen;
        if(slope>1 || slope<-1)
            rlen = desty - srcy;
        else
            rlen = destx - srcx;
        numOfVertices = (int)(lineSegments * Math.abs(rlen)) +1;
        int numOfPoints = 3*numOfVertices;

        vertices = new float[numOfPoints];
        float shrink = 1.0f/(numOfVertices-1);

        float []values = {0,0.5f,0,-0.5f};
        for(int i=0, n=0; i<numOfPoints; i+=3, n++){
            vertices[i] = -0.5f + shrink*n;
            vertices[i+1] = values[n%4]/height;
            vertices[i+2] = zorder;
        }
    }

}

