package main.java.org.cytoscape.pokemeow.internal.rendering;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Created by ZhangMenghe on 2017/8/4.
 */
public class pmNodeBuffer {
    public int VAO = 0;
    public int VBO = 1;
    public int EBO = 2;//index buffer, maybe no use
    public int[] objects = new int[3];
    public int numOfVertices;
    public int numOfIndices;
//    public FloatBuffer data_buff;
    public int capacity = 4000;
    public boolean shouldBeResize = false;
    public IntBuffer index_buff;
    public int dataOffset;
    public int indexOffset;
    public pmNodeBuffer(GL4 gl4){
        gl4.glGenVertexArrays(1,objects,VAO);
        gl4.glGenBuffers(1,objects,VBO);
        gl4.glGenBuffers(1,objects,EBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL.GL_ARRAY_BUFFER, capacity, null, GL.GL_DYNAMIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 7*Float.BYTES, 0);

        gl4.glEnableVertexAttribArray(1);
        gl4.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 7*Float.BYTES, 3*Float.BYTES);

        gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,objects[EBO]);
        gl4.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, 0,null, GL.GL_DYNAMIC_DRAW);

        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }

}
