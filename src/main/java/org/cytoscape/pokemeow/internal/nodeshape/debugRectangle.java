package main.java.org.cytoscape.pokemeow.internal.nodeshape;


import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class debugRectangle extends pmBasicNodeShape{
    public float[] _vertices = {
            0.25f,  0.25f, zorder,  // Top-left
            0.25f, -0.25f, zorder,  // Top-right
            -0.25f, -0.25f, zorder,  // Bottom-right
            -0.25f,  0.25f, zorder  // Bottom-left
    };
    public debugRectangle(GL4 gl4){
        super();
        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;

        vertices = _vertices;
        numOfVertices = 4;
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }
    public debugRectangle(){
        super();
        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;
        vertices = _vertices;
        numOfVertices = 4;
    }
    @Override
    public boolean isHit(float posx, float posy) {
        if (posx<xMin || posx>xMax || posy<yMin || posy>yMax)
            return false;
        return super.isHit(posx,posy);
    }
}
