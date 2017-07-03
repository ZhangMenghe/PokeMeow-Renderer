package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class pmArrowShapeFactory {
    public static final byte SHAPE_ARROWHEAD = 0;
    public Map<Byte, pmBasicArrowShape> arrowShapeCollector = null;

    public pmArrowShapeFactory(GL4 gl4){
        arrowShapeCollector = new HashMap<Byte, pmBasicArrowShape>();
        arrowShapeCollector.put(SHAPE_ARROWHEAD, new pmArrowheadShape(gl4));
        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
    }

    public pmBasicArrowShape createNode(Byte type){
        return arrowShapeCollector.get(type);
    }

    public void drawArrow(GL4 gl4, pmBasicArrowShape arrow, pmShaderParams gshaderParam){
        gl4.glUniformMatrix4fv(gshaderParam.mat4_modelMatrix, 1,false, Buffers.newDirectFloatBuffer(arrow.modelMatrix.asArrayCM()));
        gl4.glUniform4f(gshaderParam.vec4_color, arrow.color.x, arrow.color.y, arrow.color.z,arrow.color.w);
        gl4.glBindVertexArray(arrow.objects[arrow.VAO]);
        gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.VBO]);

        if(arrow.numOfIndices == -1)
            gl4.glDrawArrays(GL4.GL_LINE_LOOP, 0, arrow.numOfVertices);
        else{
            gl4.glBindBuffer(GL_ARRAY_BUFFER, arrow.objects[arrow.EBO]);
            gl4.glDrawElements(GL4.GL_TRIANGLES,arrow.numOfIndices, GL.GL_UNSIGNED_INT,0);
            gl4.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        }

        gl4.glBindVertexArray(0);
    }
    
    public void drawArrowList(GL4 gl4, pmBasicArrowShape[] arrowList, pmShaderParams gshaderParam){
        for(pmBasicArrowShape arrow: arrowList){
            drawArrow(gl4, arrow, gshaderParam);
        }
    }
}
