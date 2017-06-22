package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmTriangleNodeShape extends pmBasicNodeShape{
    private static final float[] vertices = {
            0f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f
    };
    private float[] colors = {
            .0f, .0f,.0f,-1.0f,
            .0f, .0f,.0f,-1.0f,
            .0f, .0f,.0f,-1.0f
    };
    private int[] colorIndices = {2,8,14};
    public float[] data;
    public int numOfVertices = 3;
    public pmTriangleNodeShape(GL4 gl4){
        super();
        //TODO: Hope to find a better interleave method
        data = new float[18];
        for(int i = 0, idx = 0;i<numOfVertices;i++){
            data[idx++] = vertices[2*i];
            data[idx++] = vertices[2*i+1];
            data[idx++] = colors[4*i];
            data[idx++] = colors[4*i+1];
            data[idx++] = colors[4*i+2];
            data[idx++] = colors[4*i+3];
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, data);
    }

    public void setColor(GL4 gl4, float[] new_color){
        for (int i=0;i<numOfVertices;i++)
            System.arraycopy(new_color,0,colors,i*4,4);
        for(int i:colorIndices){
            data[i] = new_color[0];
            data[i+1] = new_color[1];
            data[i+2] = new_color[2];
            data[i+3] = new_color[3];
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, data);
    }

    public void setColor(GL4 gl4, Vector4 new_color){
        float[]new_color_arr = {new_color.x, new_color.y,new_color.z,new_color.w};
        setColor(gl4,new_color_arr);
    }

    public void setColor(GL4 gl4, Vector4 [] colorList){
        int len = colorList.length;
         for(int i:colorIndices){
             int idx = Math.floorDiv(i,6);
             if(idx >=len)
                 idx = 0;
            data[i] = colorList[idx].x;
            data[i+1] = colorList[idx].y;
            data[i+2] = colorList[idx].z;
            data[i+3] = colorList[idx].w;
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, data);
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
