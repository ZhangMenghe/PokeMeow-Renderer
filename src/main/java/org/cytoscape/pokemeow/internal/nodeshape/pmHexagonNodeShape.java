package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmHexagonNodeShape extends pmCircleNodeShape{
    public pmHexagonNodeShape(GL4 gl4){
        super(gl4, 6);
    }
    public pmHexagonNodeShape(){
        super(6);
    }
    @Override
    public boolean isHit(float posx, float posy) {
        if(((posx - origin.x) *(posx - origin.x) +  (posy - origin.y) *(posy - origin.y) > radius*radius))
            return false;
        return super.isHit(posx, posy,true);
    }
}
