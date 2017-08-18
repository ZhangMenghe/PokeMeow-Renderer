package org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.algebra.Vector2;
import org.cytoscape.pokemeow.internal.algebra.Vector4;
import org.cytoscape.pokemeow.internal.commonUtil;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmCircleNodeShape extends pmBasicNodeShape {
    public int CircleSegment = 20;
    public float radius = 0.25f;
    /*
    High efficient way to draw circle,
    reference:  http://slabode.exofire.net/circle_draw.shtml
    */
    public pmCircleNodeShape(){
        super();
        numOfVertices = CircleSegment;
        initCircle(numOfVertices);
    }
    public pmCircleNodeShape(int new_CircleSegment){
        super();
        CircleSegment = new_CircleSegment;
        numOfVertices = CircleSegment;
        initCircle(numOfVertices);
    }
    public pmCircleNodeShape(GL4 gl4){
        super();
        numOfVertices = CircleSegment;
        initCircle(numOfVertices);
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    public pmCircleNodeShape(GL4 gl4, int new_CircleSegment){
        super();
        CircleSegment = new_CircleSegment;
        numOfVertices = CircleSegment;
        initCircle(numOfVertices);
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    private void initCircle(int numOfVertices){
        int count = 3*numOfVertices;
        vertices = new float[count];

        float theta = (float)Math.PI *2 / (float)CircleSegment;
        float tangetial_factor = (float)Math.tan(theta);
        float radial_factor = (float)Math.cos(theta);
        float x = radius;
        float y = .0f;

        for(int i=0; i<count; i+=3){
            vertices[i] = x;//radius * (float)Math.cos(i * doublePi / CircleSegment);
            vertices[i+1] = y; //radius * (float)Math.sin( i * doublePi / CircleSegment);
            vertices[i+2] = zorder;
            float tx = -y;
            float ty = x;
            x += tx*tangetial_factor;
            y += ty*tangetial_factor;
            x *= radial_factor;
            y *= radial_factor;
        }
    }

    @Override
    public boolean isHit(float posx, float posy) {
        return ((posx - origin.x) *(posx - origin.x) +  (posy - origin.y) *(posy - origin.y) < radius*radius*scale.x*scale.y);
    }

    public boolean isHit(float posx, float posy, boolean skip){
        return super.isHit(posx, posy);
    }
}
