package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/6/27.
 */
public class renderToTextureDemo extends Demo {
    private pmShaderParams gshaderParam;
    private int program;
    private int programTexture;
    private pmBasicNodeShape mtriangle;
    private pmBasicNodeShape node;
    private pmNodeShapeFactory factory;
//    private Texture texture;
//    private pmLoadTexture textureLoader;
    private int texture;
    private int frameBuffer;
    @Override
    public void create(GL4 gl4) {
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/flat.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/flat.frag"));
        programTexture =  GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/texture.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/texture.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmNodeShapeFactory(gl4);
        mtriangle = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_TRIANGLE);
        node  = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_CIRCLE);

        mtriangle.setColor(gl4, new Vector4(1.0f,.0f,.0f,1.0f));
        node.setScale(3.0f);
        node.setColor(gl4,new Vector4(.0f,1.0f,.0f,1.0f));
        node.setDefaultTexcoord(gl4);
        int[] tmpHandle = new int[3];

        gl4.glGenFramebuffers(1,tmpHandle,0);
        frameBuffer = tmpHandle[0];
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, frameBuffer);


        gl4.glGenTextures(1,tmpHandle,0);
        texture = tmpHandle[0];
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, texture);
        gl4.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL.GL_RGB,600,600,0,GL4.GL_RGB, GL4.GL_UNSIGNED_BYTE,null);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER,GL4.GL_LINEAR);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);

        gl4.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, texture, 0);
        int []DrawBuffers = {GL4.GL_COLOR_ATTACHMENT0};

        gl4.glDrawBuffers(1, Buffers.newDirectIntBuffer(DrawBuffers)); // "1" is the size of DrawBuffers
    }
    @Override
    public void render(GL4 gl4) {
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, frameBuffer);
        gl4.glViewport(0, 0, 600, 600);
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
        gl4.glDisable(GL4.GL_DEPTH_TEST);

         gl4.glUseProgram(program);
        factory.drawNode(gl4,mtriangle,gshaderParam);

        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
        gl4.glViewport(0, 0, 600, 600);

        gl4.glUseProgram(programTexture);
        gl4.glClearColor(.0f,1.0f,.0f,1.0f);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        gl4.glActiveTexture(GL4.GL_TEXTURE0);
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, texture);
//        factory.drawNode(gl4,node,gshaderParam);
        gl4.glUniform1i(gshaderParam.sampler_texture,0);
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(node.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(node.viewMattrix.asArrayCM()));
        gl4.glBindVertexArray(node.gsthForDraw.objects[node.gsthForDraw.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, node.gsthForDraw.objects[node.gsthForDraw.VBO]);
        gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, 0, node.numOfVertices);
        gl4.glBindVertexArray(0);
    }
    public void reSetMatrix(){
        mtriangle.setViewMattrix(viewMatrix);
    }
    @Override
    public void dispose(GL4 gl4) {
        mtriangle.gsthForDraw.dispose(gl4);
    }
    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
