package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.SampleUsage.debugDraw;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import java.nio.FloatBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/7/11.
 */
public class pmAggLineVisual {
    private int program;
    private int VAO = 0;
    private int VBO = 1;
    private int EBO = 2;

    private int[] objects = new int[3];
    private Vector3 origin = new Vector3();
    private Matrix4 modelMatrix = Matrix4.identity();
    private Vector2 scale = new Vector2(1.0f, 1.0f);
    private Vector4 color = new Vector4(1.0f,1.0f,.0f,1.0f);
    private int numOfVertices = 2;
    private float[] vertices = {
            -0.5f, .0f, .0f, .0f, .0f,
            0.5f, .0f, .0f, 1.0f, 1.0f
    };
    public  pmAggLineVisual(GL4 gl4){
        FloatBuffer vertice_buf = Buffers.newDirectFloatBuffer(vertices);
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/agg.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/agg.frag"));
        gl4.glGenVertexArrays(1, objects, VAO);
        gl4.glGenBuffers(1, objects, VBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, objects[VBO]);
        //gl4.glGenBuffers(1, objects, EBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL4.GL_ARRAY_BUFFER, Float.BYTES*12, vertice_buf, GL4.GL_DYNAMIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 5 * Float.BYTES, 0);

        gl4.glEnableVertexAttribArray(1);
        gl4.glVertexAttribPointer(1, 2, GL4.GL_FLOAT, false, 5 * Float.BYTES, 3*Float.BYTES);

        //gl4.glBindBuffer(GL4.GL_ELEMENT_ARRAY_BUFFER,objects[EBO]);
        //gl4.glBufferData(GL4.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES, null, GL4.GL_STATIC_DRAW);

        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
        gl4.glBindVertexArray(0);
    }
    public void drawAggLine(GL4 gl4){
        gl4.glUseProgram(program);
        gl4.glUniform1i(gl4.glGetUniformLocation(program, "dash_atlas"), 0);
        gl4.glUniform1f(gl4.glGetUniformLocation(program, "dash_index"),1);
        gl4.glUniform1f(gl4.glGetUniformLocation(program, "dash_phase"), 0);
        gl4.glUniform1f(gl4.glGetUniformLocation(program, "dash_period"), 1f);
        gl4.glUniform1f(gl4.glGetUniformLocation(program, "linelength"), 2);
        gl4.glUniform1f(gl4.glGetUniformLocation(program, "linewidth"),2);
        gl4.glUniform1f(gl4.glGetUniformLocation(program, "antialias"), 1);

        gl4.glUniform4f(gl4.glGetUniformLocation(program, "vcolor"), color.x, color.y, color.z, color.w);
        gl4.glUniform2f(gl4.glGetUniformLocation(program, "caps"), 1.0f,1.0f);

        gl4.glUniformMatrix4fv(gl4.glGetUniformLocation(program,"modelMatrix"), 1,false, Buffers.newDirectFloatBuffer(modelMatrix.asArrayCM()));
        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glLineWidth(10.0f);
//        gl4.glPointSize(5.0f);
//        CONNECT_DOTS
        gl4.glDrawArrays(GL4.GL_LINE_STRIP, 0, numOfVertices);
        gl4.glBindVertexArray(0);
    }
}
