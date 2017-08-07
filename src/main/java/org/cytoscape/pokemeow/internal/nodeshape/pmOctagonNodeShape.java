package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmOctagonNodeShape extends pmCircleNodeShape{
    public pmOctagonNodeShape(GL4 gl4){
        super(gl4, 8);
        setRotation((float) Math.PI/8);
    }
    public pmOctagonNodeShape(){
        super(8);
        setRotation((float) Math.PI/8);
    }
    @Override
    public boolean isHit(float posx, float posy) {
        if(((posx - origin.x) *(posx - origin.x) +  (posy - origin.y) *(posy - origin.y) > radius*radius))
            return false;
        return super.isHit(posx, posy,true);
    }
}
