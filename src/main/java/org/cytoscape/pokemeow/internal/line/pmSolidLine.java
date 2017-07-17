package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmSolidLine extends pmLineVisual {
    float[] vertices = {-1.0f, .0f, .0f, 1.0f, .0f, .0f};
    public pmSolidLine(GL4 gl4, Byte mDrawMethod){
        super(gl4);
        connectMethod = mDrawMethod;
        initLineVisual(gl4, vertices);
    }
}
