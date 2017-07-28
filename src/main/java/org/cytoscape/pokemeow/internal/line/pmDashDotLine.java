package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmDashDotLine extends pmLineVisual{
    public pmDashDotLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);

        connectMethod = CONNECT_SEGMENTS;
        initLineVisual(gl4);
    }
    @Override
    protected void setQuadraticBezierCurveVertices(float ctrx, float ctry){
        numOfVertices = QuadraticBezier.resolution/2 + 1;
        vertices = new float[3*numOfVertices];

        controlPoints[0] = ctrx;
        controlPoints[1] = ctry;

        QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, ctrx, ctry, destPos.x, destPos.y);
        Vector2[] curvePoints = curve.getPointsOnCurves();

        for(int k=0, n=0; k<numOfVertices; k++,n++){
            vertices[3*k] = curvePoints[n].x;
            vertices[3*k+1] = curvePoints[n].y;
            vertices[3*k+2] = zorder;
            if(k%4==0)
                n+=4;
        }
    }

    @Override
    protected void setCubicBezierCurveVertices(float ctr1x, float ctr1y, float ctr2x, float ctr2y){
        numOfVertices = CubicBezier.resolution/2 + 1;
        vertices = new float[3*numOfVertices];

        controlPoints[0] = ctr1x; controlPoints[1] = ctr1y;
        controlPoints[2] = ctr2x; controlPoints[3] = ctr2y;
        CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, ctr1x, ctr1y, ctr2x, ctr2y, destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        for(int k=0, n=0; k<numOfVertices; k++,n++){
            vertices[3*k] = curvePoints[n].x;
            vertices[3*k+1] = curvePoints[n].y;
            vertices[3*k+2] = zorder;
            if(k%4==0)
                n+=4;
        }
    }

    @Override
    protected void setSrcAndDest(float srcx, float srcy, float destx, float desty){
        super.setSrcAndDest(srcx,srcy,destx,desty);
        if(curveType == LINE_STRAIGHT){
            float rlen = Math.abs(srcx-destx) + Math.abs(srcy-desty);
            numOfVertices = lineSegments * (int)rlen +1;
            int numOfPoints = 3*numOfVertices;
            vertices = new float[numOfPoints];
            float shrink = 0.5f*rlen/(numOfVertices-1);
            vertices[0]=srcx; vertices[1]=srcy; vertices[2]=zorder;
            if(Math.abs(slope) <= 1) {
                for (int i = 3, n = 1; i < numOfPoints; i += 3, n++) {
                    if (n % 4 == 1)
                        vertices[i] = vertices[i - 3] + shrink * 5;
                    else
                        vertices[i] = vertices[i - 3] + shrink;
                    vertices[i + 1] = srcy + slope * (vertices[i] - srcx);
                    vertices[i + 2] = zorder;
                }
            }
            else{
                float k = 1.0f/slope;
                float tmpy;
                for (int i = 3, n = 1; i < numOfPoints; i += 3, n++) {
                    if (n % 4 == 1)
                        tmpy = vertices[i - 2] + shrink * 5;
                    else
                        tmpy = vertices[i - 2] + shrink;
                    vertices[i] = srcx + k*(tmpy - srcy);
                    vertices[i + 1] = tmpy;
                    vertices[i + 2] = zorder;
                }
            }
        }
    }
}
