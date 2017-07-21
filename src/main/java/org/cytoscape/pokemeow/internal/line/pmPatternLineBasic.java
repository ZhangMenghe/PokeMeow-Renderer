package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmPatternLineBasic extends pmLineVisual {
    protected int pointsPerPattern;
    protected float shrink;
    public final static int arrDensity = 8;
    public pmPatternLineBasic(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        connectMethod = CONNECT_SEGMENTS;
    }

    protected void initVertices(GL4 gl4, float []singlePattern){
        float rlen = Math.abs(srcPos.x-destPos.x) + Math.abs(srcPos.y-destPos.y);
        shrink = 1.0f/numOfPatterns;
        numOfPatterns = (int)(rlen * numOfPatterns);
        numOfVertices = pointsPerPattern*numOfPatterns;
        vertices = new float[3*numOfVertices];
        int base = 3*pointsPerPattern;
        float k = (srcPos.y-destPos.y) / (srcPos.x-destPos.x);
        double theta = Math.atan(k);
        float cost = (float)Math.cos(theta);
        float sint = (float)Math.sin(theta);

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
        for(int i=1;i<numOfPatterns;i++){
            for(int j=0;j<pointsPerPattern;j++) {
                lastx = vertices[base * (i-1) +3*j];
                lasty = vertices[base * (i-1) +3*j+1];
                vertices[base * i + 3*j] =  lastx + shrink;
                vertices[base * i + 3*j+1] = lasty + shrink * k;
                vertices[base * i + 3*j+2] = zorder;
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

        float orix, oriy;
        int base = 3 * pointsPerPattern;
        shrink = 1.0f / numOfPatterns;
        for (int i = 0, n = 0; i < numOfPatterns; i++, n += arrDensity) {
            orix = curvePoints[3 * n];
            oriy = curvePoints[3 * n + 1];
            for (int j = 0; j < pointsPerPattern; j++) {
                vertices[base * i + 3 * j] = orix + singlePattern[3 * j] * shrink;
                vertices[base * i + 3 * j + 1] = oriy + singlePattern[3 * j + 1] * shrink;
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
        Vector2[] curvePoints;
        if(curveType == LINE_QUADRIC_CURVE) {
            controlPoints[0] = nctrx;
            controlPoints[1] = nctry;

            QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, nctrx, nctry, destPos.x, destPos.y);
            curvePoints = curve.getPointsOnCurves();
        }
        else {
            CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3], destPos.x, destPos.y);
            curvePoints = curve.getPointsOnCurves();
        }
        float orix, oriy;
        int base = 3 * pointsPerPattern;
        for (int i = 0, n = 0; i < numOfPatterns; i++, n += arrDensity) {
            orix = curvePoints[n].x;
            oriy = curvePoints[n].y;
            for (int j = 0; j < pointsPerPattern; j++) {
                vertices[base * i + 3 * j] = orix + singlePattern[3 * j] * shrink;
                vertices[base * i + 3 * j + 1] = oriy + singlePattern[3 * j + 1] * shrink;
                vertices[base * i + 3 * j + 2] = zorder;
            }
        }

    }
}
