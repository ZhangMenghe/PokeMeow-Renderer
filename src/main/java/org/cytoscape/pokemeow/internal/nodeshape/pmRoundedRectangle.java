package org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmRoundedRectangle extends pmBasicNodeShape  {
    public int CircleSegment = 5;
    public float halfLength = 0.25f;
    public float radius = 0.0625f;
    private float[] controlPoints;
    public pmRoundedRectangle(){
        super();
        commonInitial();
    }
    public pmRoundedRectangle(GL4 gl4){
        super();
        commonInitial();
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    private void commonInitial(){
        controlPoints = new float[8];
        numOfVertices = 4*CircleSegment + 4;
        int count = 3*CircleSegment;

        vertices = new float[numOfVertices * 7];

        float singleStep = radius / CircleSegment;
        float radius_sqr = radius * radius;
        float x = .0f;
        float y = .0f;
        //2nd -quadrant
        for (int i = 0; i < count; i += 3) {
            x += singleStep;
            y = (float) Math.sqrt(radius_sqr - x * x + 0.0000001);//add a bias to avoid NAN
            vertices[i] = x+halfLength-radius;
            vertices[i + 1] = radius-y-halfLength;
            vertices[i + 2] = .0f;
        }
        //1st - quadrant
        y = .0f;
        for (int i = count; i < 2*count; i += 3) {
            y += singleStep;
            x = (float) Math.sqrt(radius_sqr - y * y + 0.0000001);
            vertices[i] = x+halfLength-radius;
            vertices[i + 1] = y+halfLength-radius;
            vertices[i + 2] = .0f;
        }
        //4rd - quadrant
        x = .0f;
        for (int i = 2*count; i < 3*count; i += 3) {
            x += singleStep;
            y = (float) Math.sqrt(radius_sqr - x * x + 0.0000001);
            vertices[i] = radius-x-halfLength;
            vertices[i + 1] = y + halfLength - radius;
            vertices[i + 2] = .0f;
        }
        //3rd - quadrant
        y = .0f;
        for (int i = 3*count; i < 4*count; i += 3) {
            y += singleStep;
            x = (float) Math.sqrt(radius_sqr - y * y + 0.0000001);
            vertices[i] = radius-x-halfLength;
            vertices[i + 1] = radius-y-halfLength;
            vertices[i + 2] = .0f;
        }
        int baseP = 4*count;
        float baseValue = halfLength-radius;
        //set 4 middle points
        vertices[baseP] = baseValue;
        vertices[baseP + 1] = -baseValue;
        controlPoints[0] = baseValue;
        controlPoints[1] = -baseValue;

        vertices[baseP+3] = baseValue;
        vertices[baseP +4] = baseValue;
        controlPoints[2] = baseValue;
        controlPoints[3] = baseValue;

        vertices[baseP+ 6] = -baseValue;
        vertices[baseP + 7] = baseValue;
        controlPoints[4] = -baseValue;
        controlPoints[5] = baseValue;

        vertices[baseP +9] = -baseValue;
        vertices[baseP + 10] = -baseValue;
        controlPoints[6] = -baseValue;
        controlPoints[7] = -baseValue;

        xMinOri = -0.25f;xMaxOri = 0.25f;yMinOri = -0.25f;yMaxOri = 0.25f;
        xMin= xMinOri;xMax = xMaxOri;yMin = yMinOri;yMax = yMaxOri;
    }

    @Override
    public boolean isHit(float posx, float posy) {
        if(isOutsideBoundingBox(posx,posy))
            return false;
        for(int i=0;i<4;i++){
            if(((posx-controlPoints[2*i]) * (posy-controlPoints[2*i+1]) + (posx-controlPoints[2*i+1]) * (posy-controlPoints[2*i])) < radius*radius*scale.x*scale.y)
                return true;
        }
        return  false;
    }
}
