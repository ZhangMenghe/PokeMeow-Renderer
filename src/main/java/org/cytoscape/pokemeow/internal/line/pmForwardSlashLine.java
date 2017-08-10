package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmForwardSlashLine extends pmPatternLineBasic{
    float[] _singlePattern = {
            0.5f, 0.01f, .0f,
            -0.5f, -0.01f, .0f
//            .0f, -0.5f, .0f,
//            1.0f, 0.5f, .0f
    };

    public pmForwardSlashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type, boolean initBuffer){
        super(gl4, srcx, srcy, destx, desty, type, initBuffer);
        pointsPerPattern = 2;
        singlePattern = _singlePattern;
        if (curveType == LINE_STRAIGHT)
            initStraightVertices();
        else{
            float[] curvePoints = vertices;
            arrDensity = 1;
            numOfPatterns = QuadraticBezier.resolution  / arrDensity-2;
            numOfVertices = pointsPerPattern * numOfPatterns;
            vertices = new float[3*numOfVertices];
            shrink = 1.0f / numOfPatterns;
            for(int i=0;i<pointsPerPattern;i++) {
                singlePattern[3 * i] *= shrink;
                singlePattern[3 * i + 1] *= lineWidthFactor;
            }
            setCurveVerticesByPattern(curvePoints);
        }
        if(initBuffer)
            initLineVisual(gl4);
    }

}
