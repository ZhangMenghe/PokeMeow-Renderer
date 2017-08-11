package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmSolidLine extends pmLineVisual {
    float []_vertices = {-0.5f,.0f,zorder,0.5f,.0f,zorder};
    public pmSolidLine(GL4 gl4, float srcx, float srcy, float destx, float desty, byte type, boolean initBuffer){
        super(gl4, srcx, srcy, destx, desty, type, initBuffer);
        if(curveType == LINE_STRAIGHT){
            numOfVertices = 2;
            vertices = _vertices;
        }
        if(initBuffer)
            initLineVisual(gl4);
    }
}
