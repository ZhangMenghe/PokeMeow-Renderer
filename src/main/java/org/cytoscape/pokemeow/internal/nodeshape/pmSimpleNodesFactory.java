package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.line.pmAnchor;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmNodeBuffer;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

/**
 * Created by ZhangMenghe on 2017/8/24.
 */
public class pmSimpleNodesFactory {
    private GL4 gl4;
    private pmNodeBuffer nodeBuffer;
    public pmSimpleNodesFactory(GL4 gl){
        gl4 = gl;
        nodeBuffer = new pmNodeBuffer(gl4);
    }
    public pmAnchor createSimpleNode(GL4 gl4, float posx, float posy){
        return new pmAnchor(gl4, posx, posy);
    }
    public pmAnchor createSimpleNode(float posx, float posy){
        pmAnchor node = new pmAnchor(posx, posy);
        node.bufferByteOffset = nodeBuffer.dataOffset;
        node.bufferVerticeOffset = nodeBuffer.dataOffset/12;
        nodeBuffer.dataOffset += 12;
        if(nodeBuffer.dataOffset > nodeBuffer.capacity)
            nodeBuffer.shouldBeResize = true;
        return node;
    }

    public void drawSimpleNode(pmAnchor node, pmShaderParams gshaderParam, boolean useUniformBuffer){
        gl4.glPointSize(10.0f);
//        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(Matrix4.identity().asArrayCM()));
//        gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(Matrix4.identity().asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, node.color.x, node.color.y, node.color.z, node.color.w);
//        gl4.glUniform1i(gshaderParam.int_gradColorBorder, -1);
        if(useUniformBuffer){
            if(nodeBuffer.shouldBeResize){
                nodeBuffer.doubleVBOSize(gl4);
                nodeBuffer.shouldBeResize  = false;
            }
            gl4.glBindVertexArray(nodeBuffer.objects[nodeBuffer.VAO]);
            node.data_buff = Buffers.newDirectFloatBuffer(node.vertices);
            gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER,  nodeBuffer.objects[nodeBuffer.VBO]);
            gl4.glBufferSubData(GL4.GL_ARRAY_BUFFER, node.bufferByteOffset, 12, node.data_buff);
            gl4.glDrawArrays(GL4.GL_POINTS, node.bufferVerticeOffset, 1);
        }
        else{
            gl4.glBindVertexArray(node.objects[node.VAO]);
            node.data_buff = Buffers.newDirectFloatBuffer(node.vertices);
            gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER,  node.objects[node.VBO]);
            gl4.glBufferSubData(GL4.GL_ARRAY_BUFFER, 0, 12, node.data_buff);
            gl4.glDrawArrays(GL4.GL_POINTS, 0, 1);
        }

    }
}
