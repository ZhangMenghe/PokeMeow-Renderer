package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmTriangleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

public class debugDraw implements Demo {
    private pmShaderParams gshaderParam;
    private int program;
    private pmTriangleNodeShape[] triangleNodeList;
    private int numOfTri=2;
    private pmLoadTexture textureLoader;
    private Texture texture;

    @Override
    public void create(GL4 gl4) {
        triangleNodeList = new pmTriangleNodeShape[numOfTri];

        textureLoader = new pmLoadTexture();
        texture = textureLoader.initialTexture(gl4, debugDraw.class.getResource("Texture.jpg"));

        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/texture.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/texture.frag"));
        gshaderParam = new pmShaderParams(gl4,program);
        for(int i=0;i<numOfTri;i++)
            triangleNodeList[i] = new pmTriangleNodeShape(gl4);

        gl4.glUseProgram(program);

        Vector4 [] test = {new Vector4(.0f,1.0f,.0f,1.0f),new Vector4(1.0f,.0f,.0f,1.0f)};
        triangleNodeList[0].setColor(gl4, test);

        triangleNodeList[1].setDefaultTexcoord(gl4);
        triangleNodeList[1].setOrigin(new Vector3(-0.5f,.0f,.0f));
    }

    @Override
    public void render(GL4 gl4) {
        for(int i=0;i<numOfTri;i++){
            gl4.glActiveTexture(GL4.GL_TEXTURE0);
            texture.enable(gl4);
            texture.bind(gl4);

            gl4.glUniform1i(gshaderParam.sampler_texture,0);

            gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(triangleNodeList[i].modelMatrix.asArrayCM()));
            gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(triangleNodeList[i].viewMattrix.asArrayCM()));
            gl4.glBindVertexArray(triangleNodeList[i].gsthForDraw.objects[triangleNodeList[i].gsthForDraw.VAO]);
            gl4.glBindBuffer(GL_ARRAY_BUFFER, triangleNodeList[i].gsthForDraw.objects[triangleNodeList[i].gsthForDraw.VBO]);
            gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, triangleNodeList[i].numOfVertices);
            gl4.glBindVertexArray(0);
        }

    }

    @Override
    public void dispose(GL4 gl4) {
        for(int i=0;i<numOfTri;i++)
            triangleNodeList[i].gsthForDraw.dispose(gl4);
    }

    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
