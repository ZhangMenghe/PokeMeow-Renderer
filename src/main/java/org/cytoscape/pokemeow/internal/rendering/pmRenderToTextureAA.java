package main.java.org.cytoscape.pokemeow.internal.rendering;

/**
 * Created by ZhangMenghe on 2017/6/28.
 */
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import main.java.org.cytoscape.pokemeow.internal.SampleUsage.Demo;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import java.nio.IntBuffer;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

public class pmRenderToTextureAA {
    public pmBasicNodeShape canvas;
    public int textureWidth;
    public int textureHeight;

    private int programTexture;
    private pmNodeShapeFactory factory;
    private pmShaderParams textureshaderParam;

    private int textureID;
    private int frameBufferID;
    private int screenTexture;
    private int intermediateFBO;
    private int sampleNum = 4;

    public pmRenderToTextureAA(GL4 gl4, int width, int height){
        textureHeight = height;
        textureWidth = width;
        createRenderer(gl4);
    }

    public pmRenderToTextureAA(GL4 gl4){
        int [] tmp = new int[4];
        IntBuffer viewport_size = Buffers.newDirectIntBuffer(tmp);
        gl4.glGetIntegerv(GL4.GL_VIEWPORT, viewport_size);
        textureWidth = viewport_size.get(2);
        textureHeight = viewport_size.get(3);
        createRenderer(gl4);
    }

    private void createRenderer(GL4 gl4){
        programTexture =  GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/texture.vert"),
                null,null,null,
                Demo.class.getResource("shader/texture.frag"));
        textureshaderParam = new pmShaderParams(gl4, programTexture);
        factory = new pmNodeShapeFactory(gl4);
        canvas = factory.createNode(gl4, pmNodeShapeFactory.SHAPE_RECTANGLE);
//        canvas.setDefaultTexcoord(gl4);
        canvas.setScale(8.0f);
        canvas.setOrigin(new Vector3(0,0,0));
        int[] tmpHandle = new int[1];

        gl4.glGenFramebuffers(1,tmpHandle,0);
        frameBufferID = tmpHandle[0];
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, frameBufferID);

        gl4.glGenTextures(1,tmpHandle,0);
        textureID = tmpHandle[0];
        gl4.glBindTexture(GL4.GL_TEXTURE_2D_MULTISAMPLE, textureID);
        gl4.glTexImage2DMultisample(GL4.GL_TEXTURE_2D_MULTISAMPLE, sampleNum, GL.GL_RGB,textureWidth,textureHeight ,true);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D_MULTISAMPLE, GL4.GL_TEXTURE_MAG_FILTER,GL4.GL_LINEAR);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D_MULTISAMPLE, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl4.glBindTexture(GL4.GL_TEXTURE_2D_MULTISAMPLE, 0);
        gl4.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D_MULTISAMPLE, textureID, 0);

        gl4.glGenRenderbuffers(1,tmpHandle,0);
        int depthBuffer = tmpHandle[0];
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER, depthBuffer);
        gl4.glRenderbufferStorageMultisample(GL4.GL_RENDERBUFFER, sampleNum, GL4.GL_DEPTH24_STENCIL8, textureWidth, textureHeight);
        gl4.glBindRenderbuffer(GL4.GL_RENDERBUFFER,0);
        gl4.glFramebufferRenderbuffer(GL4.GL_FRAMEBUFFER, GL4.GL_DEPTH_STENCIL_ATTACHMENT, GL4.GL_RENDERBUFFER, depthBuffer);


        if (gl4.glCheckFramebufferStatus(GL4.GL_FRAMEBUFFER) != GL4.GL_FRAMEBUFFER_COMPLETE)
            System.out.println("ERROR::FRAMEBUFFER:: Framebuffer is not complete!");
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);

        // configure second post-processing framebuffer

        gl4.glGenFramebuffers(1,tmpHandle, 0);
        intermediateFBO = tmpHandle[0];
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, intermediateFBO);
        // create a color attachment texture

        gl4.glGenTextures(1, tmpHandle, 0);
        screenTexture = tmpHandle[0];
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, screenTexture);
        gl4.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGB, textureWidth, textureHeight, 0, GL4.GL_RGB, GL4.GL_UNSIGNED_BYTE, null);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
        gl4.glFramebufferTexture2D(GL4.GL_FRAMEBUFFER, GL4.GL_COLOR_ATTACHMENT0, GL4.GL_TEXTURE_2D, screenTexture, 0);	// we only need a color buffer
    }

    public void RenderToTexturePrepare(GL4 gl4){
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, frameBufferID);
        gl4.glViewport(0, 0, textureWidth,textureHeight);
        gl4.glEnable(GL4.GL_MULTISAMPLE);
        gl4.glEnable(GL4.GL_DEPTH_TEST);
        gl4.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
        gl4.glDisable(GL4.GL_DEPTH_TEST);

        // 2. now blit multisampled buffer(s) to normal colorbuffer of intermediate FBO. Image is stored in screenTexture
        gl4.glBindFramebuffer(GL4.GL_READ_FRAMEBUFFER, frameBufferID);
        gl4.glBindFramebuffer(GL4.GL_DRAW_FRAMEBUFFER, intermediateFBO);
        gl4.glBlitFramebuffer(0, 0, textureWidth, textureHeight, 0, 0, textureWidth, textureHeight, GL4.GL_COLOR_BUFFER_BIT, GL4.GL_NEAREST);
    }

    public void RenderToScreen(GL4 gl4){
        gl4.glBindFramebuffer(GL4.GL_FRAMEBUFFER, 0);
        gl4.glUseProgram(programTexture);
        gl4.glClearColor(1.0f,1.0f,1.0f,1.0f);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        gl4.glDisable(GL4.GL_MULTISAMPLE);
        gl4.glDisable(GL4.GL_DEPTH_TEST);
        //texture
        gl4.glActiveTexture(GL4.GL_TEXTURE0);
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, screenTexture);
        //fit uniforms
        gl4.glUniform1i(textureshaderParam.sampler_texture,0);
        gl4.glUniformMatrix4fv(textureshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(canvas.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(textureshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(canvas.viewMatrix.asArrayCM()));
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

