package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmBackwardSlashLine extends pmPatternLineBasic{
    public pmBackwardSlashLine(GL4 gl4, Byte mDrawMethod){
        super(gl4, mDrawMethod);
        numOfPatterns = 5;
        pointsPerPattern = 4;
        numOfVertices = 3*pointsPerPattern*numOfPatterns;
        shrink = 2.0f/numOfPatterns;
        vertices = new float[numOfVertices];
        float[] singlePattern = {
                .0f,  0.25f, .0f,
                0.5f, -0.25f, .0f,
                .0f, -0.25f, .0f,
                -0.5f, 0.25f,.0f
        };
        initVertices(gl4, singlePattern);
    }
}
