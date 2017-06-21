package main.java.org.cytoscape.pokemeow.internal.rendering;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmVertexFormat {
    public Vector4 color;
    public Vector3 position;

    public pmVertexFormat() {
        position = new Vector3(.0f, .0f, 1.0f);
        color = new Vector4(1.0f, .0f, .0f, 1.0f);
    }
}
