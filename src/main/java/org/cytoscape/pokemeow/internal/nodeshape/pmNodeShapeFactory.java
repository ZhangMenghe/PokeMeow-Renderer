package org.cytoscape.pokemeow.internal.nodeshape;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;

import org.cytoscape.pokemeow.internal.algebra.Vector4;
import org.cytoscape.pokemeow.internal.nodeshape.pmTriangleNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmRectangleNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmDiamondNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmEllipseNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmParallelogramNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmCircleNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmHexagonNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmOctagonNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmVeeNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmRoundedRectangle;
import org.cytoscape.pokemeow.internal.rendering.pmNodeBuffer;
import org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

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
    public pmBasicNodeShape createNode(GL4 gl4, byte type){
        switch (type) {
            case 0:
                return new pmRectangleNodeShape(gl4);
            case 1:
                return new pmDiamondNodeShape(gl4);
            case 2:
                return new pmParallelogramNodeShape(gl4);
            case 3:
                return new pmRoundedRectangle(gl4);
            case 4:
                return new pmCircleNodeShape(gl4);
            case 5:
                return new pmEllipseNodeShape(gl4);
            case 6:
                return new pmHexagonNodeShape(gl4);
            case 7:
                return new pmOctagonNodeShape(gl4);
            case 8:
                return new pmTriangleNodeShape(gl4);
            case 9:
                return new pmVeeNodeShape(gl4);
            default:
                return new pmRectangleNodeShape(gl4);
        }
    }
    public pmBasicNodeShape createNode(byte type){
        pmBasicNodeShape node;
        switch (type) {
            case 0:
                node = new pmRectangleNodeShape();
                break;
            case 1:
                node = new pmDiamondNodeShape();
                break;
            case 2:
                node = new pmParallelogramNodeShape();
                break;
            case 3:
                node = new pmRoundedRectangle();
                break;
            case 4:
                node = new pmCircleNodeShape();
                break;
            case 5:
                node = new pmEllipseNodeShape();
                break;
            case 6:
                node = new pmHexagonNodeShape();
                break;
            case 7:
                node = new pmOctagonNodeShape();
                break;
            case 8:
                node = new pmTriangleNodeShape();
                break;
            case 9:
                node = new pmVeeNodeShape();
                break;
            default:
                node = new pmRectangleNodeShape();
        }
        int[] offsets = node.setBufferOffset(
                nodeBuffer.dataOffset,
                nodeBuffer.capacity
        );

        nodeBuffer.dataOffset = offsets[0];
        if(offsets[1] == -1)
            nodeBuffer.shouldBeResize = true;
        node.dirty = true;
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
        gl4.glUniform4f(gshaderParam.vec4_color, node.color.x, node.color.y, node.color.z, node.color.w);
        gl4.glUniform4f(gshaderParam.vec4_gradColor, node.gradColor.x, node.gradColor.y, node.gradColor.z, node.gradColor.w);
        gl4.glUniform1i(gshaderParam.int_gradColorBorder, node.gradColorBorderType);
        gl4.glBindVertexArray(node.gsthForDraw.objects[node.gsthForDraw.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, node.gsthForDraw.objects[node.gsthForDraw.VBO]);

        if(node.dirty){
            node.gsthForDraw.data_buff = Buffers.newDirectFloatBuffer(node.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  node.gsthForDraw.objects[node.gsthForDraw.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, 0, node.gsthForDraw.dataCapacity, node.gsthForDraw.data_buff);
            node.dirty = false;
        }
        gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, 0, node.numOfVertices);
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
        if(nodeBuffer.shouldBeResize){
            nodeBuffer.doubleVBOSize(gl4);
            nodeBuffer.shouldBeResize  = false;
        }
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(node.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(node.viewMatrix.asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, node.color.x, node.color.y, node.color.z, node.color.w);
        gl4.glUniform4f(gshaderParam.vec4_gradColor, node.gradColor.x, node.gradColor.y, node.gradColor.z, node.gradColor.w);
        gl4.glUniform1i(gshaderParam.int_gradColorBorder, node.gradColorBorderType);
        gl4.glBindVertexArray(nodeBuffer.objects[nodeBuffer.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, nodeBuffer.objects[nodeBuffer.VBO]);

        if(node.dirty){
            node.gsthForDraw.data_buff = Buffers.newDirectFloatBuffer(node.vertices);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,  nodeBuffer.objects[nodeBuffer.VBO]);
            gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, node.bufferByteOffset, node.numOfVertices*28, node.gsthForDraw.data_buff);
            node.dirty = false;
        }
        gl4.glDrawArrays(GL4.GL_TRIANGLE_FAN, node.bufferVerticeOffset, node.numOfVertices);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl4.glBindVertexArray(0);
    }

    public void deleteNode(GL4 gl4, pmBasicNodeShape node){
        //TODO:Whether or not it is necessary to reuse deleted buffer?
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, nodeBuffer.objects[nodeBuffer.VBO]);
        gl4.glBufferSubData(GL.GL_ARRAY_BUFFER, node.bufferByteOffset,node.numOfVertices*12, null);
    }
}
