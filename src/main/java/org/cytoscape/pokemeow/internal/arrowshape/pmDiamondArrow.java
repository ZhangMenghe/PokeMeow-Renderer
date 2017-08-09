package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmDiamondArrow extends pmTeeArrowShape{
    public pmDiamondArrow(){
        super();
        vertices[0] = -0.5f;
        vertices[1] = .0f;
        vertices[3] = .0f;
        vertices[4] = -0.25f;
        vertices[6] = 0.5f;
        vertices[7] = .0f;
        vertices[9] = .0f;
        vertices[10] = 0.25f;
    }

    public pmDiamondArrow(GL4 gl4){
        super();
        vertices[0] = -0.5f;
        vertices[1] = .0f;
        vertices[3] = .0f;
        vertices[4] = -0.25f;
        vertices[6] = 0.5f;
        vertices[7] = .0f;
        vertices[9] = .0f;
        vertices[10] = 0.25f;
        //initBuffer(gl4,true);
        initBuffer(gl4);
    }
}
