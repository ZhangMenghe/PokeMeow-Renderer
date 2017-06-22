package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmTriangleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmRectangleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmDiamondNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmParallelogramNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmCircleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

public class debugDraw implements Demo {

    private pmShaderParams gshaderParam;
    private int program;
    private pmBasicNodeShape[] NodeList;
//    private pmRectangleNodeShape[] triangleNodeList;
    private int numOfNodes=2;
    private pmLoadTexture textureLoader;
    private Texture texture;

    @Override
    public void create(GL4 gl4) {
        NodeList = new pmBasicNodeShape[numOfNodes];

        textureLoader = new pmLoadTexture();
        texture = textureLoader.initialTexture(gl4, debugDraw.class.getResource("Texture.jpg"));

        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/texture.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/texture.frag"));
        gshaderParam = new pmShaderParams(gl4,program);

        NodeList[0] = new pmCircleNodeShape(gl4);
        NodeList[1] = new pmParallelogramNodeShape(gl4);

        gl4.glUseProgram(program);

        Vector4 [] test = {new Vector4(.0f,1.0f,.0f,1.0f),new Vector4(1.0f,.0f,.0f,1.0f)};
        NodeList[1].setColor(gl4, test);

        NodeList[0].setDefaultTexcoord(gl4);
        NodeList[0].setOrigin(new Vector3(0.5f,.0f,.0f));
//        NodeList[1].setRotation((float) Math.PI/4);
    }

    @Override
    public void render(GL4 gl4) {
        for(int i=0;i<numOfNodes;i++){
            gl4.glActiveTexture(GL4.GL_TEXTURE0);
            texture.enable(gl4);
            texture.bind(gl4);

            gl4.glUniform1i(gshaderParam.sampler_texture,0);

            gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(NodeList[i].modelMatrix.asArrayCM()));
            gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(NodeList[i].viewMattrix.asArrayCM()));
            gl4.glBindVertexArray(NodeList[i].gsthForDraw.objects[NodeList[i].gsthForDraw.VAO]);
            gl4.glBindBuffer(GL_ARRAY_BUFFER, NodeList[i].gsthForDraw.objects[NodeList[i].gsthForDraw.VBO]);
            if(i==0)
                gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, 0, NodeList[i].numOfVertices);

            else{
//                gl4.glDrawArrays(GL4.GL_QUADS,0,4);
                gl4.glBindBuffer(GL_ARRAY_BUFFER, NodeList[i].gsthForDraw.objects[NodeList[i].gsthForDraw.EBO]);
                gl4.glDrawElements(GL4.GL_TRIANGLES,6, GL.GL_UNSIGNED_INT,0);
                gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
            }
            gl4.glBindVertexArray(0);
        }

    }

    @Override
    public void dispose(GL4 gl4) {
        for(int i=0;i<numOfNodes;i++)
            NodeList[i].gsthForDraw.dispose(gl4);
    }

    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
