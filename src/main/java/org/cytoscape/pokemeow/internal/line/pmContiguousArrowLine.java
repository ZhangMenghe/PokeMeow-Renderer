package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmContiguousArrowLine extends pmPatternLineBasic {
    private float[] _singlePattern = {
            .0f, 0.5f, .0f,
            0.5f, .0f, .0f,
            -1.5f, .0f, .0f,
            0.5f, .0f, .0f,
            .0f, -0.5f, .0f,
            0.5f, .0f, .0f,
            -1.0f, 0.5f,.0f,
            -0.5f, .0f,.0f,
            -1.0f, -0.5f, .0f,
            -0.5f, .0f,.0f
    };

    public pmContiguousArrowLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        pointsPerPattern = 10;
        singlePattern = _singlePattern;
        if(curveType == LINE_STRAIGHT)
            initStraightVertices();
        else{
            float[] curvePoints = vertices;
            arrDensity = 2;
            numOfPatterns = QuadraticBezier.resolution  / arrDensity;
            numOfVertices = pointsPerPattern * numOfPatterns;
            vertices = new float[3*numOfVertices];
            shrink = 0.6f / numOfPatterns;
            setCurveVerticesByPattern(curvePoints);
        }
        initLineVisual(gl4);
    }
}
