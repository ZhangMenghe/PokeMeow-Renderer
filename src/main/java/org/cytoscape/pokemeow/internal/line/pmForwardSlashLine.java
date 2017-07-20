package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmForwardSlashLine extends pmPatternLineBasic{
    public pmForwardSlashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT) {
            pointsPerPattern = 4;
            float[] singlePattern = {
                    .0f, 0.25f, .0f,
                    -0.5f, -0.25f, .0f,
                    .0f, -0.25f, .0f,
                    0.5f, 0.25f, .0f
            };
            initVertices(gl4, singlePattern);
        }
    }
}
