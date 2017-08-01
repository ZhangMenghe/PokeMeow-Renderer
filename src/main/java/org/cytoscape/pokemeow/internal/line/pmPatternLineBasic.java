package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;
import org.omg.CORBA.MARSHAL;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmPatternLineBasic extends pmLineVisual {
    protected int pointsPerPattern;
    protected float shrink;
    public int arrDensity = 8;
    public float[]singlePattern;

    public pmPatternLineBasic(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        connectMethod = CONNECT_SEGMENTS;
    }

    protected void initStraightVertices(){
        double theta = Math.atan(slope);
        if(slope<0)
            theta-=3.14f;
        float cost = (float)Math.cos(theta);
        float sint = (float)Math.sin(theta);

        shrink = 1.0f/numOfPatterns;
        int base = 3*pointsPerPattern;

        for(int i=0;i<pointsPerPattern;i++){
            float tmpx = singlePattern[3*i]*cost-singlePattern[3*i+1]*sint;
            float tmpy = singlePattern[3*i]*sint+singlePattern[3*i+1]*cost;
            singlePattern[3*i] = tmpx;
            singlePattern[3*i+1] = tmpy;
        }

        float lastx, lasty;
        float rlen = destPos.x - srcPos.x;
        int absNumOfPatterns = (int)(Math.abs(rlen) * numOfPatterns)-1;
        numOfVertices = pointsPerPattern*absNumOfPatterns;
        vertices = new float[3*numOfVertices];
        for(int j=0;j<pointsPerPattern;j++){
            vertices[3*j] = singlePattern[3*j] * shrink+srcPos.x+ shrink;
            vertices[3*j+1] = singlePattern[3*j +1] * shrink+srcPos.y;
            vertices[3*j+2] = zorder;
        }
        for(int i=1;i<absNumOfPatterns;i++){
            for(int j=0;j<pointsPerPattern;j++) {
                lastx = vertices[base * (i-1) +3*j];
                lasty = vertices[base * (i-1) +3*j+1];
                vertices[base * i + 3*j] =  lastx + shrink;
                vertices[base * i + 3*j+1] = lasty;
                vertices[base * i + 3*j+2] = zorder;
            }
        }
    }
    protected void initStraightVertices(float srcx, float srcy, float destx, float desty){}

    protected void initCurveVertices(){
        float[] curvePoints = vertices;
        arrDensity = 2;
        numOfPatterns = QuadraticBezier.resolution  / arrDensity;
        numOfVertices = pointsPerPattern * numOfPatterns;
        vertices = new float[3*numOfVertices];
        shrink = 0.5f / numOfPatterns;
        setCurveVerticesByPattern(curvePoints);
    }

    protected void setCurveVerticesByPattern(float[] curvePoints){
        float orix, oriy,lastorix,lastoriy;
        int base = 3 * pointsPerPattern;
        float []tmpSinglePattern = new float[3*pointsPerPattern];
        orix = curvePoints[0];
        oriy = curvePoints[1];
        arrDensity = (int) curvePoints.length/(3*numOfPatterns);
        for (int i = 0, n = 1; i < numOfPatterns; i++, n += arrDensity) {
            lastoriy = oriy; lastorix = orix;
            orix = curvePoints[3 * n];oriy = curvePoints[3 * n + 1];
            float k = (oriy - lastoriy)/(orix-lastorix);
            double theta = Math.atan(k);

            float cost = (float)Math.cos(theta);
            float sint = (float)Math.sin(theta);
            for(int ii=0;ii<pointsPerPattern;ii++){
                float tmpx = singlePattern[3*ii]*cost-singlePattern[3*ii+1]*sint;
                float tmpy = singlePattern[3*ii]*sint+singlePattern[3*ii+1]*cost;
                tmpSinglePattern[3*ii] = tmpx;
                tmpSinglePattern[3*ii+1] = tmpy;
            }
            for (int j = 0; j < pointsPerPattern; j++) {
                vertices[base * i + 3 * j] = orix + tmpSinglePattern[3 * j] * shrink;
                vertices[base * i + 3 * j + 1] = oriy + tmpSinglePattern[3 * j + 1] * shrink;
                vertices[base * i + 3 * j + 2] = zorder;
            }
        }
    }

    public void setControlPoints(float nctrx, float nctry, int anchorID){
        dirty = true;
        if(anchorID == 1){
            controlPoints[0] = nctrx; controlPoints[1] = nctry;
            anchor.setPosition(nctrx, nctry);
        }
        else{
            controlPoints[2] = nctrx; controlPoints[3] = nctry;
            anchor2.setPosition(nctrx, nctry);
        }
        float[] curvePoints;
        if(curveType == LINE_QUADRIC_CURVE) {
            controlPoints[0] = nctrx;
            controlPoints[1] = nctry;

            QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, nctrx, nctry, destPos.x, destPos.y);
            curvePoints = curve.getPointsOnCurves(zorder);
        }
        else {
            CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3], destPos.x, destPos.y);
            curvePoints = curve.getPointsOnCurves(zorder);
        }
        setCurveVerticesByPattern(curvePoints);
    }

    protected void resetSrcAndDestCurve(float srcx, float srcy, float destx, float desty){
        float [] curvePoints;
        modelMatrix = Matrix4.identity();
        afterSetCurve = true;
        if(curveType == LINE_QUADRIC_CURVE){
            if(Math.abs(slope)<=1){
                controlPoints[0] =(srcx + destx)/2.0f;
                controlPoints[1] =(srcy + desty)/2.0f+0.1f;
            }
            else{
                controlPoints[0] =(srcx + destx)/2.0f+0.1f;
                controlPoints[1] =(srcy + desty)/2.0f;
            }
            QuadraticBezier curve = new QuadraticBezier(srcx,srcy,controlPoints[0],controlPoints[1],destx,desty);
            curvePoints = curve.getPointsOnCurves(zorder);
            anchor.setPosition(controlPoints[0], controlPoints[1]);
        }
        else{
            if(Math.abs(slope)<=1){
                float tmpx = (destx-srcx)/3.0f;
                float tmpy = (srcy + desty)/2.0f;
                controlPoints[0] =tmpx+srcx;
                controlPoints[1] =tmpy+0.1f;
                controlPoints[2] =tmpx*2+srcx;
                controlPoints[3] =tmpy+0.1f;
            }
            else{
                float tmpx = (destx+srcx)/2.0f;
                float tmpy = (desty-srcy)/3.0f;
                controlPoints[0] = tmpx+0.1f;
                controlPoints[1] = tmpy+srcy;
                controlPoints[2] = tmpx+0.1f;
                controlPoints[3] = 2*tmpy+srcy;
            }
            CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3], destPos.x, destPos.y);
            curvePoints = curve.getPointsOnCurves(zorder);
            anchor.setPosition(controlPoints[0], controlPoints[1]);
            anchor2.setPosition(controlPoints[2], controlPoints[3]);
        }
        setCurveVerticesByPattern(curvePoints);
    }
    @Override
    public void resetSrcAndDest(float srcx, float srcy, float destx, float desty){
        if(curveType == LINE_STRAIGHT){
            super.resetSrcAndDest(srcx,srcy,destx,desty);
            return;
        }
        dirty = true;
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        slope = (desty - srcy) / (destx - srcx);


        resetSrcAndDestCurve(srcx, srcy, destx, desty);
    }
}
