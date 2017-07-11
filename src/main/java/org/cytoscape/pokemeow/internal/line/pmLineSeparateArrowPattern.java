package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;

/**
 * Created by ZhangMenghe on 2017/7/9.
 */
public class pmLineSeparateArrowPattern extends pmBasicArrowShape {
    public float[] vertices = {
            .0f, 0.5f, .0f,
            0.25f, .0f, .0f,
            0.125f, .0f, .0f,
            0.125f, -0.5f, .0f,
            -0.125f, -0.5f, .0f,
            -0.125f, .0f, .0f,
            -0.25f, .0f, .0f
    };
    public int [] elements = {
            0,6,5,
            0,5,2,
            0,1,2,
            2,3,4,
            2,5,4
    };
    public pmLineSeparateArrowPattern(GL4 gl4){
        super();
        numOfVertices = 7;
        numOfIndices = 15;
        this.initBuffer(gl4, vertices, elements);
    }
}
