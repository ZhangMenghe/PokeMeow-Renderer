package main.java.org.cytoscape.pokemeow.internal.nodeshape;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmDiamondNodeShape extends pmRectangleNodeShape{
    public pmDiamondNodeShape(GL4 gl4){
        super(gl4);
        setRotation((float) Math.PI/4);
    }
    @Override
    public void setDefaultTexcoord(GL4 gl4){
        Vector4 [] coordList = {new Vector4(0.5f,1.0f,.0f,-1.0f),
                new Vector4(1.0f,0.5f,.0f,-1.0f),
                new Vector4(0.5f,.0f,.0f,-1.0f),
                new Vector4(.0f,0.5f,.0f,-1.0f)
        };
        setColor(gl4,coordList);
    }
}
