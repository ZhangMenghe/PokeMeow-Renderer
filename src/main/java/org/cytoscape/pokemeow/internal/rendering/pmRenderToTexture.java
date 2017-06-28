package main.java.org.cytoscape.pokemeow.internal.rendering;

/**
 * Created by ZhangMenghe on 2017/6/28.
 */
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import main.java.org.cytoscape.pokemeow.internal.SampleUsage.debugDraw;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

public class pmRenderToTexture {
    public pmBasicNodeShape canvas;
    public int textureWidth = 600;
    public int textureHeight = 600;

    private int programTexture;
    private pmNodeShapeFactory factory;
    private pmShaderParams textureshaderParam;

    private int textureID;
    private int frameBufferID;

    public pmRenderToTexture(GL4 gl4){
        programTexture =  GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/texture.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/texture.frag"));
        textureshaderParam = new pmShaderParams(gl4, programTexture);
        factory = new pmNodeShapeFactory(gl4);
        canvas = factory.createNode(gl4, pmNodeShapeFactory.SHAPE_RECTANGLE);
        canvas.setDefaultTexcoord(gl4);
        int[] tmpHandle = new int[1];

        gl4.glGenFramebuffers(1,tmpHandle,0);
        frameBufferID = tmpHandle[0];
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, frameBufferID);

        gl4.glGenTextures(1,tmpHandle,0);
        textureID = tmpHandle[0];
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, textureID);
        gl4.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL.GL_RGB,textureWidth,textureHeight,0,GL4.GL_RGB, GL4.GL_UNSIGNED_BYTE,null);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER,GL4.GL_LINEAR);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);

        gl4.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, textureID, 0);
        int []DrawBuffers = {GL4.GL_COLOR_ATTACHMENT0};

        gl4.glDrawBuffers(1, Buffers.newDirectIntBuffer(DrawBuffers)); // "1" is the size of DrawBuffers
    }

    public void RenderToTexturePrepare(GL4 gl4){
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, frameBufferID);
        gl4.glViewport(0, 0, textureWidth,textureHeight);
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
        gl4.glDisable(GL4.GL_DEPTH_TEST);
    }

    public void RenderToScreen(GL4 gl4){
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
        gl4.glUseProgram(programTexture);
        gl4.glClearColor(.0f,1.0f,.0f,1.0f);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        //texture
        gl4.glActiveTexture(GL4.GL_TEXTURE0);
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, textureID);
        //fit uniforms
        gl4.glUniform1i(textureshaderParam.sampler_texture,0);
        gl4.glUniformMatrix4fv(textureshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(canvas.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(textureshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(canvas.viewMattrix.asArrayCM()));
        //bind buffer
        gl4.glBindVertexArray(canvas.gsthForDraw.objects[canvas.gsthForDraw.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, canvas.gsthForDraw.objects[canvas.gsthForDraw.VBO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, canvas.gsthForDraw.objects[canvas.gsthForDraw.EBO]);

        gl4.glDrawElements(GL4.GL_TRIANGLES,canvas.gsthForDraw.numOfIndices, GL.GL_UNSIGNED_INT,0);
        //unbind
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }
}
