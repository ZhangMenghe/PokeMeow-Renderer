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
            1.0f, .0f,.0f,1.0f,
            1.0f, .0f,.0f,1.0f,
            1.0f, .0f,.0f,1.0f
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
        for(int i=0;i<colorList.length;i++){
            float[]new_color = {colorList[i].x, colorList[i].y,colorList[i].z,colorList[i].w};
            //System.arraycopy(new_color,0,colors,i*4,4);
            this.data[i] = new_color[0];
            this.data[i+1] = new_color[1];
            this.data[i+2] = new_color[2];
            this.data[i+3] = new_color[3];
        }
        gsthForDraw.initBuiffer(gl4, numOfVertices, this.data);
    }
}
