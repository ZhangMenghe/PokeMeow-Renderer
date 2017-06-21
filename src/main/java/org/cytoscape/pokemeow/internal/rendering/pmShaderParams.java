package main.java.org.cytoscape.pokemeow.internal.rendering;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmShaderParams {
    public int mat4_MVP;
    public int mat4_viewMatrix;

    public pmShaderParams(GL4 gl, int program){
        gl.glUseProgram(program);
        mat4_MVP = gl.glGetUniformLocation(program,"MVP");
        mat4_viewMatrix = gl.glGetUniformLocation(program, "viewMatrix");
    }
}
