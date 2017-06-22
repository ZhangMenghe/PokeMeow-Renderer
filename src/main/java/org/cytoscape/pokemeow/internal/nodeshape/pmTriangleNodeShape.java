package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmTriangleNodeShape extends pmBasicNodeShape{
    private float[] vertices = {
            0f, 0.5f,.0f, .0f,.0f,-1.0f,
            -0.5f, -0.5f,.0f, .0f,.0f,-1.0f,
            0.5f, -0.5f, .0f, .0f,.0f,-1.0f
    };
    private int[] colorIndices = {2,8,14};

    public int numOfVertices = 3;

    public pmTriangleNodeShape(GL4 gl4){
        super();
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    public void setColor(GL4 gl4, float[] new_color){
        for(int i:colorIndices){
            vertices[i] = new_color[0];
            vertices[i+1] = new_color[1];
            vertices[i+2] = new_color[2];
            vertices[i+3] = new_color[3];
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

    public void setColor(GL4 gl4, Vector4 new_color){
        for(int i:colorIndices){
            vertices[i] = new_color.x;
            vertices[i+1] = new_color.y;
            vertices[i+2] = new_color.z;
            vertices[i+3] = new_color.w;
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, vertices);
    }

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

    public void setDefaultTexcoord(GL4 gl4){
        Vector4 [] coordList = {new Vector4(.0f,.0f,.0f,-1.0f),
                new Vector4(.0f,1.0f,.0f,-1.0f),
                new Vector4(1.0f,.0f,.0f,-1.0f),
                new Vector4(1.0f,1.0f,.0f,-1.0f)
        };

        setColor(gl4,coordList);
    }
}
