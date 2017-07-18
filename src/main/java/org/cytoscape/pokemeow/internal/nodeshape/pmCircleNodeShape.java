package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmCircleNodeShape extends pmBasicNodeShape {
    public int CircleSegment = 60;
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
        int count = 7*numOfVertices;
        vertices = new float[count];
        colorIndices = new int[numOfVertices];

        float theta = (float)Math.PI *2 / (float)CircleSegment;
        float tangetial_factor = (float)Math.tan(theta);
        float radial_factor = (float)Math.cos(theta);
        float x = radius;
        float y = .0f;

        for(int i=0; i<count; i+=7){
            vertices[i] = x;//radius * (float)Math.cos(i * doublePi / CircleSegment);
            vertices[i+1] = y; //radius * (float)Math.sin( i * doublePi / CircleSegment);
            vertices[i+2]=.0f;
            float tx = -y;
            float ty = x;
            x += tx*tangetial_factor;
            y += ty*tangetial_factor;
            x *= radial_factor;
            y *= radial_factor;

            vertices[i+3] = 1.0f;
            vertices[i+4] = .0f;
            vertices[i+5] = .0f;
            vertices[i+6] = 1.0f;

            colorIndices[Math.floorDiv(i,7)] = i+3;
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    @Override
    public void setDefaultTexcoord(GL4 gl4){
        useTexture = true;
        Vector4 [] coordList = new Vector4[CircleSegment];
        float factor = 1.0f/(2*radius);
        for(int i=0;i<CircleSegment;i++){
            float x = vertices[7*i] * factor + 0.5f;
            float y = vertices[7*i+1] * factor + 0.5f;
            coordList[i] = new Vector4(x,y,.0f,-1.0f);
        }
        setColor(gl4, coordList);
    }

    @Override
    public boolean isHit(float posx, float posy) {
        return ((posx - origin.x) *(posx - origin.x) +  (posy - origin.y) *(posy - origin.y) < radius*radius*scale.x*scale.y);
    }

    public boolean isHit(float posx, float posy, boolean skip){
        return super.isHit(posx, posy);
    }
}
