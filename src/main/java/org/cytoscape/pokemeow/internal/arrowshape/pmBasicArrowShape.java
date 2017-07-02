package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

import java.nio.FloatBuffer;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public abstract class pmBasicArrowShape {
    public int VAO = 0;
    public int VBO = 1;
    public int[] objects = new int[2];
    public int numOfVertices = 0;

    protected Vector3 origin = new Vector3();
    protected Matrix4 modelMatrix = Matrix4.identity();
    protected Vector3 scale = new Vector3(1.0f,1.0f,1.0f);
    protected Vector4 color = new Vector4(.0f,1.0f,.0f,1.0f);

    public pmBasicArrowShape(){
        origin = new Vector3(.0f,.0f,.0f);
        scale = new Vector3(0.5f,0.5f,0.5f);
        modelMatrix = Matrix4.mult(Matrix4.scale((scale)),Matrix4.translation(origin));
    }

    protected void initBuffer(GL4 gl4, float[] vertices){
        numOfVertices = vertices.length/3;
        FloatBuffer data_buff = Buffers.newDirectFloatBuffer(vertices);
        gl4.glGenVertexArrays(1,objects,VAO);
        gl4.glGenBuffers(1,objects,VBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL.GL_ARRAY_BUFFER, data_buff.capacity() * Float.BYTES, data_buff, GL.GL_STATIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3*Float.BYTES, 0);

        gl4.glBindVertexArray(0);
    }

    public void setScale(Vector2 new_scale){
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
    }

    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public void setZorder(float new_z){
    }

    public void dispose(GL4 gl4) {
        gl4.glDeleteBuffers(2, objects,0);
        gl4.glDeleteVertexArrays(1, objects, VAO);
    }
}
