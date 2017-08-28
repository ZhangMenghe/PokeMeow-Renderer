package main.java.org.cytoscape.pokemeow.internal.rendering;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/8/4.
 */
public class pmNodeBuffer {
    public int VAO = 0;
    public int VBO = 1;
    public int[] objects = new int[2];
    public int capacity = 5000;
    public boolean shouldBeResize = false;
    public int dataOffset;
    public pmNodeBuffer(GL4 gl4){
        gl4.glGenVertexArrays(1,objects,VAO);
        gl4.glGenBuffers(1,objects,VBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL.GL_ARRAY_BUFFER, capacity, null, GL.GL_DYNAMIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 12, 0);

//        gl4.glEnableVertexAttribArray(1);
//        gl4.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 7*Float.BYTES, 3*Float.BYTES);

        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }

    public void doubleVBOSize(GL4 gl4){
        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);

        int[] t_object = new int[1];
        gl4.glGenBuffers(1,t_object,0);
        gl4.glBindBuffer(GL4.GL_COPY_READ_BUFFER, t_object[0]);
        gl4.glBufferData(GL4.GL_COPY_READ_BUFFER, capacity, null, GL.GL_DYNAMIC_DRAW);
        gl4.glCopyBufferSubData(GL4.GL_ARRAY_BUFFER,GL4.GL_COPY_READ_BUFFER,0,0,capacity);

        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  objects[VBO]);
        gl4.glBufferData(GL.GL_ARRAY_BUFFER, capacity*2, null, GL.GL_DYNAMIC_DRAW);
        gl4.glCopyBufferSubData(GL4.GL_COPY_READ_BUFFER, GL.GL_ARRAY_BUFFER, 0, 0, capacity);
        capacity*=2;

        gl4.glDeleteBuffers(1, t_object, 0);
    }

    public void dispose(GL4 gl4){
        gl4.glDeleteBuffers(2, objects, 0);
        gl4.glDeleteVertexArrays(1, objects, VAO);
    }
}
