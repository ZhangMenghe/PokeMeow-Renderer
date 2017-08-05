package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmRectangleNodeShape extends pmBasicNodeShape{
    public float[] _vertices = {
         0.25f,  0.25f, zorder, .0f, .0f, .0f, 1.0f, // Top-left
         0.25f, -0.25f, zorder, .0f, .0f, .0f, 1.0f, // Top-right
        -0.25f, -0.25f, zorder, .0f, .0f, .0f, 1.0f, // Bottom-right
        -0.25f,  0.25f, zorder, .0f, .0f, .0f, 1.0f  // Bottom-left
    };
    public int []_indices = {
            3, 0, 1,
            3, 1, 2
    };

    public pmRectangleNodeShape(GL4 gl4){
        super();
        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;

        vertices = _vertices;
        indices = _indices;
        numOfVertices = 4;
        numOfIndices = 6;
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices, indices);
    }

    public pmRectangleNodeShape(){
        super();
        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;
        vertices = _vertices;
        indices = _indices;
        numOfVertices = 4;
        numOfIndices = 6;
    }

    @Override
    public boolean isHit(float posx, float posy) {
        if (posx<xMin || posx>xMax || posy<yMin || posy>yMax)
            return false;
        return super.isHit(posx,posy);
    }
}
