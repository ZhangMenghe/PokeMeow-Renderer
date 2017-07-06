package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmShort2DeltaArrowShape extends pmDeltaArrowShape {

    public pmShort2DeltaArrowShape(GL4 gl4){
        super(gl4, true);
        vertices[0] = -0.25f;
        vertices[1] = 0.25f;
        vertices[3] = .0f;
        vertices[4] = .0f;
        vertices[6] = -0.25f;
        vertices[7] = -0.25f;
        initBuffer(gl4, vertices);
    }
}