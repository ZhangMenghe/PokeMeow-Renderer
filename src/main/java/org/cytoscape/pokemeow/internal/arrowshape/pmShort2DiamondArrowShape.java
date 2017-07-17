package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmShort2DiamondArrowShape extends pmTeeArrowShape{
    public pmShort2DiamondArrowShape(GL4 gl4){
        super(gl4, true);
        vertices[0] = -0.125f;
        vertices[1] = -0.25f;
        vertices[3] = .0f;
        vertices[4] = .0f;
        vertices[6] = -0.125f;
        vertices[7] = 0.25f;
        vertices[9] = -0.25f;
        vertices[10] = .0f;
        initBuffer(gl4, vertices, elements);
    }
}