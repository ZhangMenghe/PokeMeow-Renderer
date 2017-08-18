package org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/5.
 */
public class pmShortArrowheadShape extends pmArrowheadShape{
    public pmShortArrowheadShape(){
        super(true);
        curve = new QuadraticBezier(-0.1f, -0.2f,.0f,.0f,0.5f,.0f);
        curve2 = new QuadraticBezier(0.5f,.0f,.0f,.0f,-0.1f, 0.2f);
        initPoints();
    }

    public pmShortArrowheadShape(GL4 gl4){
        super(true);
        curve = new QuadraticBezier(-0.1f, -0.2f,.0f,.0f,0.5f,.0f);
        curve2 = new QuadraticBezier(0.5f,.0f,.0f,.0f,-0.1f, 0.2f);
        initPoints();
        initBuffer(gl4, true);
    }
}
