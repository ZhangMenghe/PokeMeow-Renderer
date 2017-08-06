package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmParallelogramNodeShape extends pmRectangleNodeShape {
    public pmParallelogramNodeShape(GL4 gl4){
        super();
        vertices[7] = .0f;
        vertices[21] = .0f;
        setScale(new Vector3(1.0f,0.5f,1.0f));
        setScale(1.5f);
//        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices, indices);
    }
}
