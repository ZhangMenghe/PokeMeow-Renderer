package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmParallelogramNodeShape extends pmRectangleNodeShape {
    public pmParallelogramNodeShape(GL4 gl4){
        super(gl4, true);
        vertices[6] = .0f;
        vertices[18] = .0f;
        setScale(new Vector3(1.0f,0.5f,1.0f));
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices,elements);
    }
}
