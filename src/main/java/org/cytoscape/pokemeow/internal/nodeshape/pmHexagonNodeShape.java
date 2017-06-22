package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmHexagonNodeShape extends pmCircleNodeShape{
    public pmHexagonNodeShape(GL4 gl4){
        super(gl4, 6);
    }
}
