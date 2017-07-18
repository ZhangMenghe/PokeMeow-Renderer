package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmParallelLine extends pmLineVisual {
    public pmParallelLine(GL4 gl4, pmLineVisual line){
        super(gl4);
        connectMethod = CONNECT_PARALLEL;
        initLineVisual(gl4, line);
    }
    public void setScale(Vector2 new_scale){
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
    }
    public void setRotation(float radians){
        rotMatrix = Matrix4.rotationZ(radians);
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
    }
    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public void setZorder(GL4 gl4, float new_z){
    }
}
