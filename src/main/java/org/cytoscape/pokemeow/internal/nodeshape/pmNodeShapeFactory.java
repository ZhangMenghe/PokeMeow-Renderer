package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmTriangleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmRectangleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmDiamondNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmEllipseNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmParallelogramNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmCircleNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmHexagonNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmOctagonNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmVeeNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmRoundedRectangle;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmNodeBuffer;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * This is a collection of all the nodes shapes extends from BasicNodeShape
 * Created in template of RendererNodeShape.java created by zugzwang
 * Created by ZhangMenghe on 2017/6/22.
 */
public class pmNodeShapeFactory {
    /**
     * Node shape constants
     */
    public static final byte SHAPE_RECTANGLE = 0;
    public static final byte SHAPE_DIAMOND = 1;
    public static final byte SHAPE_PARALLELOGRAM = 2;
    public static final byte SHAPE_ROUNDED_RECTANGLE = 3;

    public static final byte SHAPE_CIRCLE = 4;
    public static final byte SHAPE_ELLIPSE = 5;
    public static final byte SHAPE_HEXAGON = 6;
    public static final byte SHAPE_OCTAGON = 7;

    public static final byte SHAPE_TRIANGLE = 8;
    public static final byte SHAPE_VEE = 9;
    private GL4 gl4;
    private pmNodeBuffer nodeBuffer;
    public pmNodeShapeFactory(GL4 gl){
        gl4 = gl;
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
        nodeBuffer = new pmNodeBuffer(gl4);
    }

    public pmBasicNodeShape createNode(GL4 gl4, Byte type){
        pmBasicNodeShape node;
        switch (type) {
            case 0:
                node = new pmRectangleNodeShape(gl4);
                break;
            case 1:
                node = new pmDiamondNodeShape(gl4);
                break;
            case 2:
                node = new pmParallelogramNodeShape(gl4);
                break;
            case 3:
                node = new pmRoundedRectangle(gl4);
                break;
            case 4:
                node = new pmCircleNodeShape(gl4);
                break;
            case 5:
                node = new pmEllipseNodeShape(gl4);
                break;
            case 6:
                node = new pmHexagonNodeShape(gl4);
                break;
            case 7:
                node = new pmOctagonNodeShape(gl4);
                break;
            case 8:
                node = new pmTriangleNodeShape(gl4);
                break;
            case 9:
                node = new pmVeeNodeShape(gl4);
                break;
            default:
                node = new pmRectangleNodeShape(gl4);
        }
        int[] offsets = node.setBufferOffset(
                nodeBuffer.dataOffset,
                nodeBuffer.indexOffset,
                nodeBuffer.capacity,
                nodeBuffer.capacityIdx

        );

        nodeBuffer.dataOffset = offsets[0];
        nodeBuffer.indexOffset = offsets[1];

        if(offsets[2] == -1)
            nodeBuffer.shouldBeResize = 0;
        if(offsets[3] == -1)
            nodeBuffer.shouldBeResize = 1;
        return node;
    }

    public void drawNodeWithTexture(GL4 gl4, pmBasicNodeShape node, pmShaderParams gshaderParam, Texture texture){
        gl4.glActiveTexture(GL4.GL_TEXTURE0);
        texture.enable(gl4);
        texture.bind(gl4);

        gl4.glUniform1i(gshaderParam.sampler_texture,0);

        drawNode(gl4,node,gshaderParam);
    }

    public void drawNode(GL4 gl4, pmBasicNodeShape node, pmShaderParams gshaderParam){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(node.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(node.viewMatrix.asArrayCM()));
        gl4.glBindVertexArray(node.gsthForDraw.objects[node.gsthForDraw.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, node.gsthForDraw.objects[node.gsthForDraw.VBO]);

        if(node.dirty){
            node.gsthForDraw.data_buff = Buffers.newDirectFloatBuffer(node.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  node.gsthForDraw.objects[node.gsthForDraw.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, node.gsthForDraw.dataCapacity, node.gsthForDraw.data_buff);
            node.dirty = false;
        }
        if(node instanceof pmRectangleNodeShape){
            gl4.glBindBuffer(GL_ARRAY_BUFFER, node.gsthForDraw.objects[node.gsthForDraw.EBO]);
            gl4.glDrawElements(GL4.GL_TRIANGLES,node.gsthForDraw.numOfIndices, GL.GL_UNSIGNED_INT,0);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        }
        else{
            gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, 0, node.numOfVertices);
        }
        gl4.glBindVertexArray(0);
    }

    public void drawNodeList(GL4 gl4, pmBasicNodeShape[] NodeList, int[]programList, pmShaderParams gshaderParam, Texture texture, ArrayList<Integer>flatindices,ArrayList<Integer>textureindices){
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        if(flatindices!=null) {
            gl4.glUseProgram(programList[0]);
            for (int i : flatindices)
                drawNode(gl4, NodeList[i], gshaderParam);
        }
        if(textureindices != null){
            gl4.glUseProgram(programList[1]);
            for(int i :textureindices)
                drawNodeWithTexture(gl4, NodeList[i], gshaderParam, texture);
        }
    }

    public void drawNodeList(GL4 gl4, pmBasicNodeShape[] NodeList, int[]programList, pmShaderParams gshaderParam,
                             ArrayList<Texture> textureList, ArrayList<Integer>flatindices,ArrayList<Integer>textureindices,
                             ArrayList<Integer> textureIdx){
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        if(flatindices!=null) {
            gl4.glUseProgram(programList[0]);
            for (int i : flatindices)
                drawNode(gl4, NodeList[i], gshaderParam);
        }
        if(textureindices != null){
            gl4.glUseProgram(programList[1]);
            for(int i=0;i<textureindices.size();i++){
                int nodeId = textureindices.get(i);
                int texId = textureIdx.get(i);
                drawNodeWithTexture(gl4, NodeList[nodeId], gshaderParam, textureList.get(texId));
            }

        }
    }
    public void drawNode(GL4 gl4, pmBasicNodeShape node, pmShaderParams gshaderParam, boolean useUniformBuffer){
        if(nodeBuffer.shouldBeResize!=-1){
            if(nodeBuffer.shouldBeResize == 0)
                nodeBuffer.doubleVBOSize(gl4);
            else
                nodeBuffer.doubleEBOSize(gl4);
            nodeBuffer.shouldBeResize  = -1;
        }
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(node.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(node.viewMatrix.asArrayCM()));
//        gl4.glBindVertexArray(nodeBuffer.objects[nodeBuffer.VAO]);
        if(node.numOfIndices != -1 && !node.isfirst) {
            gl4.glGenVertexArrays(1, node.objects, 0);
            gl4.glBindVertexArray(node.objects[0]);
            gl4.glEnableVertexAttribArray(0);
            gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 7 * Float.BYTES, node.bufferByteOffset);

            gl4.glEnableVertexAttribArray(1);
            gl4.glVertexAttribPointer(1, 4, GL.GL_FLOAT, false, 7 * Float.BYTES, node.bufferByteOffset + 3 * Float.BYTES);
            node.isNew = false;
        }
        else
            gl4.glBindVertexArray(nodeBuffer.objects[nodeBuffer.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, nodeBuffer.objects[nodeBuffer.VBO]);

        if(node.dirty){
            node.gsthForDraw.data_buff = Buffers.newDirectFloatBuffer(node.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  nodeBuffer.objects[nodeBuffer.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, node.bufferByteOffset, node.numOfVertices*28, node.gsthForDraw.data_buff);

            if(node.numOfIndices != -1){
                node.gsthForDraw.indice_buff = Buffers.newDirectIntBuffer(node.indices);
                gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, nodeBuffer.objects[nodeBuffer.EBO]);
                gl4.glBufferSubData(GL.GL_ELEMENT_ARRAY_BUFFER, node.indexByteOffset, node.numOfIndices*4, node.gsthForDraw.indice_buff);
            }
//            node.dirty = false;
        }
        if(node instanceof pmRectangleNodeShape){
            gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, nodeBuffer.objects[nodeBuffer.EBO]);
            gl4.glDrawElements(GL4.GL_TRIANGLES, node.numOfIndices, GL.GL_UNSIGNED_INT, node.indexByteOffset);
            gl4.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,0);
        }
        else{
            gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, node.bufferVerticeOffset, node.numOfVertices);
        }
        gl4.glBindVertexArray(0);
    }
}
