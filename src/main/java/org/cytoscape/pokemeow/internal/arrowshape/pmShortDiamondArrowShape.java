package org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmShortDiamondArrowShape extends pmTeeArrowShape{
    public pmShortDiamondArrowShape(){
        super();
        vertices[0] = -0.25f;
        vertices[1] = -0.25f;
        vertices[3] = .0f;
        vertices[4] = .0f;
        vertices[6] = -0.25f;
        vertices[7] = 0.25f;
        vertices[9] = -0.5f;
        vertices[10] = .0f;
    }

    public pmShortDiamondArrowShape(GL4 gl4){
        super();
        vertices[0] = -0.25f;
        vertices[1] = -0.25f;
        vertices[3] = .0f;
        vertices[4] = .0f;
        vertices[6] = -0.25f;
        vertices[7] = 0.25f;
        vertices[9] = -0.5f;
        vertices[10] = .0f;
        //initBuffer(gl4,true);
        initBuffer(gl4);
    }
}