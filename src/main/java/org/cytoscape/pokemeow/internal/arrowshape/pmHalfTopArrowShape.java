package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmHalfTopArrowShape extends pmBasicArrowShape{
    public float[] _vertices = {
            -0.15f, -0.05f, .0f,
            -0.65f, -0.55f, .0f,
            -0.72f,  -0.55f, .0f,
            -0.25f, -0.1f, .0f,
            -0.25f, -0.05f, .0f
    };
    public int []_elements = {
            1,2,0,
            2,0,3,
            0,3,4
    };
    public pmHalfTopArrowShape(){
        super();
        numOfVertices = 5;
        numOfIndices = 9;
        vertices = _vertices;
        elements = _elements;
        setScale(0.5f);
    }
    public pmHalfTopArrowShape(GL4 gl4){
        super();
        numOfVertices = 5;
        numOfIndices = 9;
        vertices = _vertices;
        elements = _elements;
        initBuffer(gl4, true);
        setScale(0.5f);
    }
    public void setZorder(GL4 gl4, float new_z){
        vertices[2] = new_z;
        vertices[5] = new_z;
        vertices[8] = new_z;
        vertices[11] = new_z;
        vertices[14] = new_z;
        vertices = _vertices;
        elements = _elements;
        initBuffer(gl4, true);
    }
}
