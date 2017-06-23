package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmCircleNodeShape extends pmBasicNodeShape {
    public int CircleSegment = 360;
    public float[] vertices;
    public int[] colorIndices;
    public float radius = 0.25f;
    /*
    High efficient way to draw circle,
    reference:  http://slabode.exofire.net/circle_draw.shtml
    */
    public pmCircleNodeShape(GL4 gl4){
        super();
        numOfVertices = CircleSegment;
        initCircle(gl4, numOfVertices);
    }

    public pmCircleNodeShape(GL4 gl4, int new_CircleSegment){
        super();
        CircleSegment = new_CircleSegment;
        numOfVertices = CircleSegment;
        initCircle(gl4, numOfVertices);
    }

    private void initCircle(GL4 gl4, int numOfVertices){
        int count = 6*numOfVertices;
        vertices = new float[count];
        colorIndices = new int[numOfVertices];

        float theta = (float)Math.PI *2 / (float)CircleSegment;
        float tangetial_factor = (float)Math.tan(theta);
        float radial_factor = (float)Math.cos(theta);
        float x = radius;
        float y = .0f;

        for(int i=0; i<count; i+=6){
            vertices[i] = x;//radius * (float)Math.cos(i * doublePi / CircleSegment);
            vertices[i+1] = y; //radius * (float)Math.sin( i * doublePi / CircleSegment);

            float tx = -y;
            float ty = x;
            x += tx*tangetial_factor;
            y += ty*tangetial_factor;
            x *= radial_factor;
            y *= radial_factor;

            vertices[i+2] = 1.0f;
            vertices[i+3] = .0f;
            vertices[i+4] = .0f;
            vertices[i+5] = 1.0f;

            colorIndices[Math.floorDiv(i,6)] = i+2;
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }
    @Override
    public void setColor(GL4 gl4, float[] new_color){
        for(int i:colorIndices){
            vertices[i] = new_color[0];
            vertices[i+1] = new_color[1];
            vertices[i+2] = new_color[2];
            vertices[i+3] = new_color[3];
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    @Override
    public void setColor(GL4 gl4, Vector4 new_color){
        for(int i:colorIndices){
            vertices[i] = new_color.x;
            vertices[i+1] = new_color.y;
            vertices[i+2] = new_color.z;
            vertices[i+3] = new_color.w;
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    @Override
    public void setColor(GL4 gl4, Vector4 [] colorList){
        int len = colorList.length;
        for(int i:colorIndices){
            int idx = Math.floorDiv(i,6);
            if(idx >=len)
                idx = 0;
            vertices[i] = colorList[idx].x;
            vertices[i+1] = colorList[idx].y;
            vertices[i+2] = colorList[idx].z;
            vertices[i+3] = colorList[idx].w;
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }
    @Override
    public void setDefaultTexcoord(GL4 gl4){
        Vector4 [] coordList = new Vector4[CircleSegment];
        float factor = 1.0f/(2*radius);
        for(int i=0;i<CircleSegment;i++){
            float x = vertices[6*i] * factor + 0.5f;
            float y = vertices[6*i+1] * factor + 0.5f;
            coordList[i] = new Vector4(x,y,.0f,-1.0f);
        }
        setColor(gl4,coordList);
    }
}
