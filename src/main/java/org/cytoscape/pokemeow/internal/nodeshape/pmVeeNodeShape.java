package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmVeeNodeShape extends pmRectangleNodeShape{
    public pmVeeNodeShape(GL4 gl4){
        super(gl4, true);
        vertices[0] = .0f;
        vertices[14] = .0f;
        vertices[15] = -0.125f;
        vertices[22] = -0.25f;
        int [] new_elements = {3,0,2,0,2,1};
        elements = new_elements;
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices, elements);
    }
}
