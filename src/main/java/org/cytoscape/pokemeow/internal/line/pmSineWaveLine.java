package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmSineWaveLine extends pmPatternLineBasic{
    private int lineSegments = 50;
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
        float deltay = desty - srcy;
        float deltax = destx - srcx;
        float length =(float) Math.sqrt(deltay*deltay + deltax*deltax);

        float rlen = Math.abs(srcx-destx) + Math.abs(srcy-desty);
        numOfVertices = lineSegments * (int)rlen +1;
        int numOfPoints = 3*numOfVertices;

        vertices = new float[numOfPoints];
        float shrink = length/(numOfVertices-1);
        float exampleX = period*baseW;
        double theta = Math.atan(slope);
        float cost = (float)Math.cos(theta);
        float sint = (float)Math.sin(theta);
        float gapx = .0f,gapy = .0f;
        for(int i=0, n=0; i<numOfPoints; i+=3, n++){
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

    public void setControlPoints(float nctrx, float nctry, int anchorID){
        super.setControlPoints(nctrx,nctry,anchorID);
    }

}
