package org.cytoscape.pokemeow.internal.rendering;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmShaderParams {
    public int mat4_modelMatrix;
    public int mat4_viewMatrix;
    public int sampler_texture;
    public int vec4_color;
    public int vec4_gradColor;
    public int int_gradColorBorder;
    public pmShaderParams(GL4 gl, int program){
        mat4_modelMatrix = gl.glGetUniformLocation(program,"modelMatrix");
        mat4_viewMatrix = gl.glGetUniformLocation(program, "viewMatrix");
        sampler_texture = gl.glGetUniformLocation(program, "texSampler");
        vec4_color = gl.glGetUniformLocation(program, "vcolor");
        vec4_gradColor = gl.glGetUniformLocation(program, "vcolorGrad");
        int_gradColorBorder = gl.glGetUniformLocation(program, "gradColorBorder");
    }
}
