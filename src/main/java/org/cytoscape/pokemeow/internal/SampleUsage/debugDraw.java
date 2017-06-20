package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.SampleUsage.Demo;
import main.java.org.cytoscape.pokemeow.internal.SampleUsage.ShaderManager;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_STATIC_DRAW;
import static com.jogamp.opengl.GL2ES2.GL_FRAGMENT_SHADER;
import static com.jogamp.opengl.GL2ES2.GL_VERTEX_SHADER;
public class debugDraw implements Demo {
    private static final float[] vertices = {
            0f, 0.5f,
            -0.5f, -0.5f,
            0.5f, -0.5f
    };

    private static final int VAO = 0;
    private static final int VBO = 1;
    private final int[] objects = new int[2];

    private int program;
    private GL4 gl4;
    @Override
    public void create(GL4 gl4) {
        gl4 = gl4;
        program = ShaderManager.INSTANCE.buildProgram(gl4, "flat");

        final FloatBuffer data = Buffers.newDirectFloatBuffer(vertices);

        gl4.glGenBuffers(1, objects, VBO);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL_ARRAY_BUFFER, data.capacity() * Float.BYTES, data, GL_STATIC_DRAW);

        gl4.glGenVertexArrays(1, objects, VAO);
        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, objects[VBO]);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        gl4.glUseProgram(program);
    }

    @Override
    public void render(GL4 gl4) {
        gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, 3);
    }

    @Override
    public void dispose(GL4 gl4) {
        gl4.glDeleteVertexArrays(1, objects, VAO);
    }

    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
