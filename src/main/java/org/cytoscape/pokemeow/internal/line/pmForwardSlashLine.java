package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmForwardSlashLine extends pmPatternLineBasic{
    float[] singlePattern = {
            .0f, 0.5f, .0f,
            -1.0f, -0.5f, .0f,
            .0f, -0.5f, .0f,
            1.0f, 0.5f, .0f
    };
    public pmForwardSlashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        pointsPerPattern = 4;
        if (curveType == LINE_STRAIGHT)
            initVertices(singlePattern);
        else
            initCurveVertices(singlePattern);
        initLineVisual(gl4);
    }
    public void setControlPoints(float nctrx, float nctry, int anchorID){
        super.setControlPoints(nctrx,nctry,anchorID,singlePattern);
    }
}
