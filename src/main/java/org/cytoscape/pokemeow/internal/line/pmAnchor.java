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
    public Matrix4 modelMatrix = Matrix4.identity();
    public Vector3 position;
    public Vector4 color;
    public float[] vertices;
    public boolean dirty = false;

    public pmAnchor(GL4 gl4, float posx, float posy){
        setPosition(posx, posy);
        vertices = new float[3];
        vertices[0] = posx;
        vertices[1] = posy;
        vertices[2] = -1.0f;

        FloatBuffer data_buff = Buffers.newDirectFloatBuffer(vertices);
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
        position = new Vector3(posx, posy, -1.0f);
        modelMatrix = Matrix4.translation(position);
    }
    public void setColor(Vector4 new_color){
        color = new_color;
    }
}
