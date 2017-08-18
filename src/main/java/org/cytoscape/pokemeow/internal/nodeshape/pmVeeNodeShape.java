package org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.algebra.Vector3;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmVeeNodeShape extends pmBasicNodeShape{
    public float[] _vertices = {
            .0f,  0.25f, zorder,
            0.25f, -0.25f, zorder,
            .0f, -0.125f, zorder,
            -0.25f, -0.25f, zorder
    };

    public pmVeeNodeShape(){
        super();
        vertices = _vertices;
        numOfVertices = 4;
        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;
    }

    public pmVeeNodeShape(GL4 gl4){
        super();
        vertices = _vertices;
        numOfVertices = 4;
        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    @Override
    public boolean isHit(float posx, float posy) {
        if (isOutsideBoundingBox(posx,posy))
            return false;
        return super.isHit(posx,posy);
    }
}
