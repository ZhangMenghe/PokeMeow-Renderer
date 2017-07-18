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

    public pmNodeShapeFactory(GL4 gl){
        gl4 = gl;
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
    }

    public pmBasicNodeShape createNode(GL4 gl4, Byte type){
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
            gl4.glBufferData(GL.GL_ARRAY_BUFFER, node.gsthForDraw.dataCapacity,node.gsthForDraw.data_buff, GL.GL_STATIC_DRAW);
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

}
