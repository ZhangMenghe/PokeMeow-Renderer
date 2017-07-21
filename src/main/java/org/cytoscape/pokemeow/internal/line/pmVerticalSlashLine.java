package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmVerticalSlashLine extends pmPatternLineBasic{
    private float[] singlePattern = {
            -0.25f, 0.25f, .0f,
            -0.25f, -0.25f, .0f,
            0.25f, 0.25f, .0f,
            0.25f, -0.25f, .0f
    };
    public final static int arrDensity = 2;
    public pmVerticalSlashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type) {
        super(gl4, srcx, srcy, destx, desty, type);
        pointsPerPattern = 4;
        if (curveType == LINE_STRAIGHT)
            initVertices(gl4, singlePattern);
        else {
            float[] curvePoints = vertices;
            numOfPatterns = QuadraticBezier.resolution / arrDensity;
            numOfVertices = 3 * pointsPerPattern * numOfPatterns;
            vertices = new float[numOfVertices];

            float deltax, deltay, k;
            double theta;
            float cost, sint;
            float orix, oriy;
            int base = 3 * pointsPerPattern;
            shrink = 4.0f / numOfPatterns;
            for (int i = 0, n = 0; i < numOfPatterns; i++, n += arrDensity) {
                deltay = curvePoints[3 * (i + 1) + 1] - curvePoints[3 * i + 1];
                deltax = curvePoints[3 * (i + 1)] - curvePoints[3 * i];
                k = deltay / deltax;
                theta = Math.atan(k);
                cost = (float) Math.cos(theta);
                sint = (float) Math.sin(theta);
                orix = curvePoints[3 * n];
                oriy = curvePoints[3 * n + 1];
                for (int j = 0; j < pointsPerPattern; j++) {
                    float tmpx = orix + singlePattern[3 * j] * shrink;
                    float tmpy = oriy + singlePattern[3 * j + 1] * shrink;
                    vertices[base * i + 3 * j] = tmpx;//tmpx*cost-tmpy*sint;
                    vertices[base * i + 3 * j + 1] = tmpy;// tmpx*sint-tmpy*cost;
                    vertices[base * i + 3 * j + 2] = zorder;
                }
            }
            initLineVisual(gl4, vertices);
        }
    }
    public void setControlPoints(float nctrx, float nctry, int anchorID){
        super.setControlPoints(nctrx,nctry,anchorID);
//        numOfPatterns = QuadraticBezier.resolution/arrDensity;
//        float[] curvePoints = vertices;
//        float deltax, deltay, k;
//        double theta;
//        float cost, sint;
//        float orix, oriy;
//        int base = 3 * pointsPerPattern;
//        shrink = 4.0f / numOfPatterns;
//        for (int i = 0, n = 0; i < numOfPatterns; i++, n += arrDensity) {
//            deltay = curvePoints[3 * (i + 1) + 1] - curvePoints[3 * i + 1];
//            deltax = curvePoints[3 * (i + 1)] - curvePoints[3 * i];
//            k = deltay / deltax;
//            theta = Math.atan(k);
//            cost = (float) Math.cos(theta);
//            sint = (float) Math.sin(theta);
//            orix = curvePoints[3 * n];
//            oriy = curvePoints[3 * n + 1];
//            for (int j = 0; j < pointsPerPattern; j++) {
//                float tmpx = orix + singlePattern[3 * j] * shrink;
//                float tmpy = oriy + singlePattern[3 * j + 1] * shrink;
//                vertices[base * i + 3 * j] = tmpx;//tmpx*cost-tmpy*sint;
//                vertices[base * i + 3 * j + 1] = tmpy;// tmpx*sint-tmpy*cost;
//                vertices[base * i + 3 * j + 2] = zorder;
//            }
//        }

    }
}
