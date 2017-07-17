package main.java.org.cytoscape.pokemeow.internal;

/**
 * Created by ZhangMenghe on 2017/6/18.
 */

import main.java.org.cytoscape.pokemeow.internal.algebra.*;

public final class commonUtil {
    public static final float CAM_ZOOM_RATE = 1.25F;
    public static final Vector2 CAM_CLIP_RANGE = new Vector2(1e-2f, 1e6f);
    public static final Vector2 CAM_VIEWPORT_SIZE = new Vector2(100, 100);

    public static final int[] VIEW_PORT_INFO = {0, 0, 800, 800};

    public static Vector2 getAbsolutePos(float x, float y){
        return new Vector2(VIEW_PORT_INFO[2]*x, VIEW_PORT_INFO[3]*y);
    }

    public static Vector2 getRelativePos(float x, float y ){
        return new Vector2(x/VIEW_PORT_INFO[2], y/VIEW_PORT_INFO[3]);
    }
}
