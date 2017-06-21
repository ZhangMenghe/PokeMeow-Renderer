package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.SampleUsage.Demo;
import main.java.org.cytoscape.pokemeow.internal.SampleUsage.ShaderManager;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmSthForDraw;
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
    private static final float[] colors = {
            1.0f, .0f,.0f,1.0f,
            1.0f, .0f,.0f,1.0f,
            1.0f, .0f,.0f,1.0f
    };

    private pmShaderParams gshaderProgram;
    private pmSthForDraw gsthForDraw;

    private int program;

    @Override
    public void create(GL4 gl4) {
        program = ShaderManager.INSTANCE.buildProgram(gl4, "flat");
        gshaderProgram = new pmShaderParams(gl4,program);
        gsthForDraw = new pmSthForDraw();

        //TODO: Hope to find a better interleave method
        float []data = new float[18];
        for(int i = 0, idx = 0;i<3;i++){
            data[idx++] = vertices[2*i];
            data[idx++] = vertices[2*i+1];
            data[idx++] = colors[4*i];
            data[idx++] = colors[4*i+1];
            data[idx++] = colors[4*i+2];
            data[idx++] = colors[4*i+3];
        }
        gsthForDraw.initBuiffer(gl4,3,data);
        gl4.glUseProgram(program);
    }

    @Override
    public void render(GL4 gl4) {
        gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, gsthForDraw.numOfVertices);
    }

    @Override
    public void dispose(GL4 gl4) {
        gsthForDraw.dispose(gl4);
    }

    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
