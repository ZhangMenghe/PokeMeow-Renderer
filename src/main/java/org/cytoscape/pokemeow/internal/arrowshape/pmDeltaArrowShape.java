package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmDeltaArrowShape extends pmBasicArrowShape {
    public float[] vertices = {
            -0.5f,  0.25f, .0f,
            0.5f, .0f,.0f,
            -0.5f, -0.25f, .0f
    };

    public pmDeltaArrowShape(GL4 gl4){
        super();
        numOfVertices = 3;
        initBuffer(gl4, vertices);
        setScale(0.5f);
    }

    public pmDeltaArrowShape(GL4 gl4, boolean skip){
        super();
        numOfVertices = 3;
        setScale(0.5f);
    }

    public void setZorder(GL4 gl4, float new_z){
        vertices[2] = new_z;
        vertices[5] = new_z;
        vertices[8] = new_z;
        this.initBuffer(gl4, vertices);
    }
}
