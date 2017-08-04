package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmSineWaveLine extends pmPatternLineBasic{
    private int period = 40;
    private float baseW = 0.174f;//baseW is approximate 2*pi/360

    public pmSineWaveLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT)
            initStraightVertices(srcx, srcy, destx, desty);
        else{
            singlePattern = new float[60];
            double start = -1.0;
            for(int i=0,n=0;i<60;i+=3,start+=0.1,n++){
                singlePattern[i] = (float) start;
                singlePattern[i+1] =(float) Math.sin(start*3.14 - 1.57f)/2;
                singlePattern[i+2] = zorder;
            }
            pointsPerPattern = 20;
            initCurveVertices();
        }
        connectMethod = CONNECT_STRIP;
        initLineVisual(gl4);

    }

    protected void initStraightVertices(float srcx, float srcy, float destx, float desty){
        float rlen;
        lineSegments = 80;
        if(slope<1)
            rlen = destx - srcx;
        else
            rlen = desty - srcy;
        numOfVertices = (int)(lineSegments * Math.abs(rlen)) +1;
        int numOfPoints = 3*numOfVertices;
        lineSegments = 100;
        vertices = new float[numOfPoints];
        float shrink = rlen/(numOfVertices-1);
        float exampleX = period*baseW;
        int i,n;
        for(i=0, n=0; i<numOfPoints; i+=3, n++){
            vertices[i] = -0.5f + shrink*n;
            vertices[i+1] = (float) Math.sin(exampleX*n)/40;
            vertices[i+2] = zorder;
        }
        vertices[1] = vertices[i-2] = .0f;
    }

}
