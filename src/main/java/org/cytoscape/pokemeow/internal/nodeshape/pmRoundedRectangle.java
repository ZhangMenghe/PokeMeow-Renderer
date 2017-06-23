package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmRoundedRectangle extends pmRectangleNodeShape  {
    public int CircleSegment;
    public float radius;

    public pmRoundedRectangle(GL4 gl4){
        super(gl4,true);
        CircleSegment = 10;
        numOfVertices = 4*CircleSegment + 4;
        radius = 0.125f/2;

        int count = 6*CircleSegment;
        vertices = new float[numOfVertices * 6];
        int[] new_colorIndices = new int[numOfVertices];
        colorIndices = new_colorIndices;
        float singleStep = radius / CircleSegment;
        float radius_sqr = radius * radius;
        float x = .0f;
        float y = .0f;
        //2nd -quadrant
        for (int i = 0; i < count; i += 6) {
            x += singleStep;
            y = (float) Math.sqrt(radius_sqr - x * x + 0.0000001);//add a bias to avoid NAN
            vertices[i] = x+0.25f-radius;
            vertices[i + 1] = radius-y-0.25f;
        }
        //1st - quadrant
        y = .0f;
        for (int i = count; i < 2*count; i += 6) {
            y += singleStep;
            x = (float) Math.sqrt(radius_sqr - y * y + 0.0000001);
            vertices[i] = x+0.25f-radius;
            vertices[i + 1] = y+0.25f-radius;
        }
        //4rd - quadrant
        x = .0f;
        for (int i = 2*count; i < 3*count; i += 6) {
            x += singleStep;
            y = (float) Math.sqrt(radius_sqr - x * x + 0.0000001);
            vertices[i] = radius-x-0.25f;
            vertices[i + 1] = y + 0.25f - radius;
        }
        //3rd - quadrant
        y = .0f;
        for (int i = 3*count; i < 4*count; i += 6) {
            y += singleStep;
            x = (float) Math.sqrt(radius_sqr - y * y + 0.0000001);
            vertices[i] = radius-x-0.25f;
            vertices[i + 1] = radius-y-0.25f;
        }
        int baseP = 4*count;
        float baseValue = 0.25f-radius;
        //set 4 middle points
        vertices[baseP] = baseValue;
        vertices[baseP + 1] = -baseValue;

        vertices[baseP+6] = baseValue;
        vertices[baseP + 7] = baseValue;

        vertices[baseP+ 12] = -baseValue;
        vertices[baseP + 13] = baseValue;

        vertices[baseP +18] = -baseValue;
        vertices[baseP + 19] = -baseValue;

        for(int i=0;i<numOfVertices;i++){
            vertices[i*6 + 2] = 1.0f;
            vertices[i*6 + 3] = .0f;
            vertices[i*6 + 4] = .0f;
            vertices[i*6 + 5] = -1.0f;
            colorIndices[i] = i*6+2;
        }
        int baseIdx = numOfVertices-4;

        int [] new_elements = {
                baseIdx+3,baseIdx,baseIdx+2,
                baseIdx,baseIdx+1,baseIdx+2,
                baseIdx-1, 0, baseIdx+3,
                0,baseIdx,baseIdx+3,
                baseIdx,CircleSegment-1,CircleSegment,
                baseIdx+1,baseIdx,CircleSegment,
                baseIdx+2,baseIdx+1,2*CircleSegment,
                baseIdx+1,2*CircleSegment-1,2*CircleSegment,
                baseIdx+3,baseIdx+2,3*CircleSegment,
                baseIdx+2,3*CircleSegment-1,3*CircleSegment,
        };
        int[] arc_elements = new int[12*CircleSegment];
        int n=0;
        for(int k = 0;k<4;k++){
            for(int i=0;i<CircleSegment-1;i++){
                arc_elements[n++] = baseIdx+k;
                arc_elements[n++] = k*CircleSegment + i;
                arc_elements[n++] = k*CircleSegment + i+1;
            }
        }
        elements = new int[new_elements.length + 12*CircleSegment];
        System.arraycopy(new_elements,0,elements,0,new_elements.length);
        System.arraycopy(arc_elements, 0, elements,new_elements.length, 12*CircleSegment);
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices, elements);
    }


}
