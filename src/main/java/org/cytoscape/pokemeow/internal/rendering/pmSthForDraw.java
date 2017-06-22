package main.java.org.cytoscape.pokemeow.internal.rendering;


import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;

import com.jogamp.*;
/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmSthForDraw {
    public int VAO = 0;
    public int VBO = 1;
    public int[] objects = new int[2];
    public int indiceBuf;//index buffer, maybe no use
    public int numOfVertices;
    public int numOfIndices;

    public void initBuiffer(GL4 gl, int numVertices,float[] data ){
        numOfVertices = numVertices;
        FloatBuffer data_buff = Buffers.newDirectFloatBuffer(data);
        gl.glGenVertexArrays(1,objects,VAO);
        gl.glGenBuffers(1,objects,VBO);

        gl.glBindVertexArray(objects[VAO]);
        gl.glBindBuffer(GL_ARRAY_BUFFER, objects[VBO]);
        gl.glBufferData(GL_ARRAY_BUFFER, data_buff.capacity() * Float.BYTES,data_buff, GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 2, GL_FLOAT, false, 6*Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);
        gl.glVertexAttribPointer(1, 4, GL_FLOAT, false, 6*Float.BYTES, 2*Float.BYTES);
        gl.glBindVertexArray(0);
    }

    public void dispose(GL4 gl4){
        gl4.glDeleteVertexArrays(1, objects, VAO);
    }

}
