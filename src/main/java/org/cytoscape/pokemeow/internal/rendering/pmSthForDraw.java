package main.java.org.cytoscape.pokemeow.internal.rendering;


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmSthForDraw {
    public int VAO = 0;
    public int VBO = 1;
    public int EBO = 2;//index buffer, maybe no use
    public int[] objects = new int[3];
    public int numOfVertices;
    public int numOfIndices;
    public int dataCapacity;
    public FloatBuffer data_buff;
    public IntBuffer indice_buff;
    public void initBuiffer(GL4 gl, int numVertices,float[] vertices ){
        numOfVertices = numVertices;
        data_buff = Buffers.newDirectFloatBuffer(vertices);
        dataCapacity = data_buff.capacity() * Float.BYTES;
        gl.glGenVertexArrays(1,objects,VAO);
        gl.glGenBuffers(1,objects,VBO);

        gl.glBindVertexArray(objects[VAO]);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, dataCapacity, data_buff, GL.GL_STATIC_DRAW);
//        gl.glBufferData(GL.GL_ARRAY_BUFFER, dataCapacity, null, GL.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 7*Float.BYTES, 0);

        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 7*Float.BYTES, 3*Float.BYTES);

        gl.glBindVertexArray(0);
    }

    public void initBuiffer(GL4 gl, int numVertices,float[] vertices, int[] indices){
        numOfVertices = numVertices;
        numOfIndices = indices.length;
        data_buff = Buffers.newDirectFloatBuffer(vertices);
        indice_buff = Buffers.newDirectIntBuffer(indices);
        dataCapacity = data_buff.capacity() * Float.BYTES;

        gl.glGenVertexArrays(1,objects,VAO);
        gl.glGenBuffers(1,objects,VBO);
        gl.glGenBuffers(1,objects,EBO);

        gl.glBindVertexArray(objects[VAO]);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, data_buff.capacity() * Float.BYTES,data_buff, GL.GL_STATIC_DRAW);

        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 7*Float.BYTES, 0);

        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 7*Float.BYTES, 3*Float.BYTES);

        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,objects[EBO]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indice_buff.capacity() * Integer.BYTES, indice_buff, GL.GL_STATIC_DRAW);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl.glBindVertexArray(0);
    }

    public void dispose(GL4 gl4){
        gl4.glDeleteBuffers(3, objects, 0);
        gl4.glDeleteVertexArrays(1, objects, VAO);
    }

}
