package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;

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

    public Map<Byte, pmBasicNodeShape> nodeShapeCollector = null;

    public pmNodeShapeFactory(GL4 gl4){
        nodeShapeCollector = new HashMap<Byte,pmBasicNodeShape>();
        nodeShapeCollector.put(SHAPE_RECTANGLE, new pmRectangleNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_CIRCLE, new pmCircleNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_ELLIPSE, new pmEllipseNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_ROUNDED_RECTANGLE, new pmRoundedRectangle(gl4));
        nodeShapeCollector.put(SHAPE_DIAMOND, new pmDiamondNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_HEXAGON, new pmHexagonNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_OCTAGON, new pmOctagonNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_PARALLELOGRAM, new pmParallelogramNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_TRIANGLE, new pmTriangleNodeShape(gl4));
        nodeShapeCollector.put(SHAPE_VEE, new pmVeeNodeShape(gl4));

        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
    }

    public pmBasicNodeShape createNode(GL4 gl4, Byte type){

        return nodeShapeCollector.get(type);
    }

    //Currently support only single texture
    public void drawNodeWithTexture(GL4 gl4, pmBasicNodeShape node, pmShaderParams gshaderParam, Texture texture){
        gl4.glActiveTexture(GL4.GL_TEXTURE0);
        texture.enable(gl4);
        texture.bind(gl4);

        gl4.glUniform1i(gshaderParam.sampler_texture,0);

        drawNode(gl4,node,gshaderParam);
    }

    //Currently support only single texture
    public void drawNode(GL4 gl4, pmBasicNodeShape node, pmShaderParams gshaderParam){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(node.modelMatrix.asArrayCM()));
        gl4.glUniformMatrix4fv(gshaderParam.mat4_viewMatrix, 1,false, Buffers.newDirectFloatBuffer(node.viewMattrix.asArrayCM()));
        gl4.glBindVertexArray(node.gsthForDraw.objects[node.gsthForDraw.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, node.gsthForDraw.objects[node.gsthForDraw.VBO]);

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
