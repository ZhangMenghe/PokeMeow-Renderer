package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

import java.nio.FloatBuffer;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmTriangleNodeShape extends pmBasicNodeShape{
    private float[] mvertices = {
               .0f,  0.25f, zorder, .0f, .0f, .0f, 1.0f,
            -0.25f, -0.25f, zorder, .0f, .0f, .0f, 1.0f,
             0.25f, -0.25f, zorder, .0f, .0f, .0f, 1.0f
    };
    public pmTriangleNodeShape(){
        super();
        vertices = mvertices;
        numOfVertices = 3;
        xMinOri = -0.25f; xMaxOri = 0.25f; yMinOri = -0.25f; yMaxOri = 0.25f;
        xMin = xMinOri; xMax = xMaxOri; yMin = yMinOri; yMax = yMaxOri;
    }
    public pmTriangleNodeShape(GL4 gl4){
        super();
        vertices = mvertices;
        numOfVertices = 3;
        xMinOri = -0.25f; xMaxOri = 0.25f; yMinOri = -0.25f; yMaxOri = 0.25f;
        xMin = xMinOri; xMax = xMaxOri; yMin = yMinOri; yMax = yMaxOri;
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    @Override
    public boolean isHit(float posx, float posy) {
        if(posx<xMin || posx>xMax || posy<yMin || posy>yMax)
            return false;
        return super.isHit(posx, posy);
    }
}
