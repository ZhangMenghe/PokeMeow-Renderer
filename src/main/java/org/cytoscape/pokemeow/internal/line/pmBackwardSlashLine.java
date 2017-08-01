package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmBackwardSlashLine extends pmPatternLineBasic{
    private float[] _singlePattern = {
            -0.5f, 0.25f, .0f,
            0.5f, -0.25f, .0f
//            .0f, -0.5f, .0f,
//            -1.0f, 0.5f, .0f
    };
    public pmBackwardSlashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
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
            setCurveVerticesByPattern(curvePoints);
        }
        initLineVisual(gl4);
    }

}
