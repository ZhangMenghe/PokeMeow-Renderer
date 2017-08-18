package org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.algebra.Vector3;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmEllipseNodeShape extends pmCircleNodeShape {
    public pmEllipseNodeShape(GL4 gl4){
        super(gl4);
        setScale(new Vector3(1.5f, 1.0f, 1.0f));
    }
    public pmEllipseNodeShape(){
        super();
        setScale(new Vector3(1.5f, 1.0f, 1.0f));
    }
    @Override
    public boolean isHit(float posx, float posy) {
        if(((posx - origin.x) *(posx - origin.x) +  (posy - origin.y) *(posy - origin.y) < radius*radius*scale.x*scale.y))
            return true;
        return super.isHit(posx, posy,true);
    }
}
