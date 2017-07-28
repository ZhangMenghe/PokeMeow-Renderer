package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmSolidLine extends pmLineVisual {
    public pmSolidLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        initLineVisual(gl4);
    }

    public void setSrcAndDest(float srcx, float srcy, float destx, float desty){
        super.setSrcAndDest(srcx,srcy,destx,desty);
        if(curveType == LINE_STRAIGHT) {
            if(vertices == null){
                numOfVertices = 2;
                vertices = new float[6];
            }
            vertices[0] = srcx; vertices[1] = srcy;
            vertices[3] = destx; vertices[4] = desty;
        }
    }
}
