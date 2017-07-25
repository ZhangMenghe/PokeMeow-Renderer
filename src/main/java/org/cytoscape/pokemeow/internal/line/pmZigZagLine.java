package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmZigZagLine extends pmPatternLineBasic{
    private float height = 20.0f;
    private float[] singlePattern;

    public pmZigZagLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT)
            initStraightLinePoints(srcx, srcy, destx, desty);
        else{
            singlePattern = new float[30];
            double start = -1.0;
            for(int i=0, n=0;i<30;i+=3,start+=.1,n++){
                singlePattern[i] = (float) start;
                singlePattern[i+1] = (float)start;
                singlePattern[i+2] = zorder;
            }
            pointsPerPattern = 10;
            initCurveVertices(gl4, singlePattern);
        }

        connectMethod = CONNECT_STRIP;
        initLineVisual(gl4, vertices);
    }

    public void setControlPoints(float nctrx, float nctry, int anchorID){
        super.setControlPoints(nctrx,nctry,anchorID,singlePattern);
    }

    private void initStraightLinePoints(float srcx, float srcy, float destx, float desty){
        lineSegments = 20;
        float deltay = desty - srcy;
        float deltax = destx - srcx;
//        float k = deltay / deltax;
        float length =(float) Math.sqrt(deltay*deltay + deltax*deltax);

        float rlen = Math.abs(srcx-destx) + Math.abs(srcy-desty);
        numOfVertices = lineSegments * (int)rlen +1;
        int numOfPoints = 3*numOfVertices;

        vertices = new float[numOfPoints];
        float shrink = length/(numOfVertices-1);
        double theta = Math.atan(slope);
        float cost = (float)Math.cos(theta);
        float sint = (float)Math.sin(theta);
        float gapx = .0f,gapy = .0f;
        int []values = {0,1,0,-1};
        for(int i=0, n=0; i<numOfPoints; i+=3, n++){
            float tmpx = srcx + shrink*n;
            float tmpy =  values[n%4]/height;
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
}

