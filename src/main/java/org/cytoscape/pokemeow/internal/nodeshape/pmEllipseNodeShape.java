package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmEllipseNodeShape extends pmCircleNodeShape {
    public pmEllipseNodeShape(GL4 gl4){
        super(gl4);
        setScale(new Vector3(1.5f, 1.0f, 1.0f));
    }
}
