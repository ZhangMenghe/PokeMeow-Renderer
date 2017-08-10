package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

import java.nio.FloatBuffer;

/**
 * Created by ZhangMenghe on 2017/7/18.
 */
public class pmAnchor {
    public int VAO = 0;
    public int VBO = 1;
    public int[] objects = new int[2];
    public Vector4 color= new Vector4(1.0f,.0f,.0f,1.0f);//default to red
    public float[] vertices;
    public FloatBuffer data_buff;
    private float zorder = .0f;
    public int bufferByteOffset = 0;
    public int bufferVerticeOffset = 0;

    public pmAnchor(float posx, float posy){
        vertices = new float[3];
        vertices[0] = posx;
        vertices[1] = posy;
        vertices[2] = zorder;
    }

    public pmAnchor(GL4 gl4, float posx, float posy){
        vertices = new float[3];
        vertices[0] = posx;
        vertices[1] = posy;
        vertices[2] = zorder;

        data_buff = Buffers.newDirectFloatBuffer(vertices);
        gl4.glGenVertexArrays(1,objects,VAO);
        gl4.glGenBuffers(1,objects,VBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL.GL_ARRAY_BUFFER, 3 * Float.BYTES, data_buff, GL.GL_STATIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3*Float.BYTES, 0);

        gl4.glBindVertexArray(0);
    }

    public void setPosition(float posx, float posy){
        vertices[0] = posx;
        vertices[1] = posy;
    }
    public void setPosition(float posx, float posy, boolean useOffset){
        vertices[0] += posx;
        vertices[1] += posy;
    }
    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public boolean isHit(float posx, float posy){
        return Math.abs(posx-vertices[0]) < 0.08 && Math.abs(posy-vertices[1]) < 0.08;
    }
}
