package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
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
    public pmPatternLineBasic(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        connectMethod = CONNECT_SEGMENTS;
    }

    protected void initVertices(GL4 gl4, float []singlePattern){
//        float k = (destPos.y - srcPos.y) / (destPos.x - srcPos.x);
        double theta = Math.atan(slope);
        if(slope<0)
            theta-=3.14f;
        float cost = (float)Math.cos(theta);
        float sint = (float)Math.sin(theta);

        float rlen = Math.abs(srcPos.x-destPos.x) + Math.abs(srcPos.y-destPos.y);
        shrink = 1.0f/numOfPatterns;
        numOfPatterns = (int)(rlen * numOfPatterns);
        numOfVertices = pointsPerPattern*numOfPatterns;
        vertices = new float[3*numOfVertices];
        int base = 3*pointsPerPattern;

        for(int i=0;i<pointsPerPattern;i++){
            float tmpx = singlePattern[3*i]*cost-singlePattern[3*i+1]*sint;
            float tmpy = singlePattern[3*i]*sint+singlePattern[3*i+1]*cost;
            singlePattern[3*i] = tmpx;
            singlePattern[3*i+1] = tmpy;
        }

        for(int j=0;j<pointsPerPattern;j++){
            vertices[3*j] = singlePattern[3*j] * shrink+srcPos.x;
            vertices[3*j+1] = singlePattern[3*j +1] * shrink+srcPos.y;
            vertices[3*j+2] = zorder;
        }
        float lastx, lasty;
        if(Math.abs(slope) <= 1){
            for(int i=1;i<numOfPatterns;i++){
                for(int j=0;j<pointsPerPattern;j++) {
                    lastx = vertices[base * (i-1) +3*j];
                    lasty = vertices[base * (i-1) +3*j+1];
                    vertices[base * i + 3*j] =  lastx + shrink;
                    vertices[base * i + 3*j+1] = lasty + shrink * slope;
                    vertices[base * i + 3*j+2] = zorder;
                }
            }
        }
        else{
            float k = 1.0f/slope;
            for(int i=1;i<numOfPatterns;i++){
                for(int j=0;j<pointsPerPattern;j++) {
                    lastx = vertices[base * (i-1) +3*j];
                    lasty = vertices[base * (i-1) +3*j+1];

                    float tmpy = lasty + shrink;
                    vertices[base * i + 3*j+1] = tmpy;
                    vertices[base * i + 3*j] =  lastx + k *(tmpy - lasty);
                    vertices[base * i + 3*j+2] = zorder;
                }
            }
        }
        initLineVisual(gl4, vertices);
//        setRotation(3.14f/4);
//        setRotation((float)Math.atan((srcPos.y-destPos.y) / (srcPos.x-destPos.x)));

    }

    protected void initCurveVertices(GL4 gl4, float []singlePattern){
        float[] curvePoints = vertices;
        numOfPatterns = QuadraticBezier.resolution / arrDensity;
        numOfVertices = pointsPerPattern * numOfPatterns;
        vertices = new float[3*numOfVertices];
        shrink = 1.0f / numOfPatterns;
        setCurveVerticesByPattern(curvePoints, singlePattern);
    }

    protected void setCurveVerticesByPattern(float[] curvePoints, float []singlePattern){
        float orix, oriy, lastorix, lastoriy;
        int base = 3 * pointsPerPattern;
        orix = curvePoints[0];
        oriy = curvePoints[1];
        float []tmpSinglePattern = new float[3*pointsPerPattern];


        for (int i = 0, n = 0; i < numOfPatterns; i++, n += arrDensity) {
            lastorix = orix; lastoriy = oriy;
            orix = curvePoints[3 * n];oriy = curvePoints[3 * n + 1];
            float k = (oriy-lastoriy) / (orix - lastorix);
            double theta = Math.atan(k);
            if(k<0)
                theta-=3.14f;
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

    protected void setControlPoints(float nctrx, float nctry, int anchorID,float []singlePattern){
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
        setCurveVerticesByPattern(curvePoints, singlePattern);
    }
}
