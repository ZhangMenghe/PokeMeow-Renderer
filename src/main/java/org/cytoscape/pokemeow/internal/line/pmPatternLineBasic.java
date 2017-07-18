package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmPatternLineBasic extends pmLineVisual {
    protected int numOfPatterns;
    protected int pointsPerPattern;
    protected float shrink;

    public pmPatternLineBasic(GL4 gl4){
        super(gl4);
        connectMethod = CONNECT_SEGMENTS;
    }

    protected void initVertices(GL4 gl4, float []singlePattern){
        int base = 3*pointsPerPattern;
        for(int j=0;j<pointsPerPattern;j++){
            vertices[3*j] = singlePattern[3*j] * shrink-1.0f;
            vertices[3*j+1] = singlePattern[3*j +1] * shrink;
            vertices[3*j+2] = .0f;
        }
        for(int i=1;i<numOfPatterns;i++){
            for(int j=0;j<pointsPerPattern;j++) {
                vertices[base * i + 3*j] = vertices[base * (i-1) +3*j] + shrink;//2/width,should be direction
                vertices[base * i + 3*j+1] = vertices[3*j+1];
                vertices[base * i + 3*j+2] = .0f;
            }
        }
        initLineVisual(gl4, vertices);
    }
}
