package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmSeparateArrowLine extends pmPatternLineBasic {
    public pmSeparateArrowLine(GL4 gl4) {
        super(gl4);
        pmBasicArrowShape[]arrowList = new pmBasicArrowShape[5];
        for(int i=0;i<5;i++){
            arrowList[i] = new pmLineSeparateArrowPattern(gl4);
            arrowList[i].setScale(0.2f);
            arrowList[i].setOrigin(new Vector3(.0f, -0.6f+0.2f*i,.0f));
        }
        initLineVisual(gl4, arrowList);
    }
    public void setScale(Vector2 new_scale) {
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin), Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        for (int i = 0; i < patternList.length; i++)
            patternList[i].modelMatrix = Matrix4.mult(modelMatrix, patternList[i].modelMatrix);
    }
    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        for (int i = 0; i < patternList.length; i++)
            patternList[i].modelMatrix = Matrix4.mult(modelMatrix, patternList[i].modelMatrix);
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        for (int i = 0; i < patternList.length; i++)
            patternList[i].modelMatrix = Matrix4.mult(modelMatrix, patternList[i].modelMatrix);
    }
    public void setRotation(float radians){
        rotMatrix = Matrix4.rotationZ(radians);
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        for (int i = 0; i < patternList.length; i++)
            patternList[i].modelMatrix = Matrix4.mult(modelMatrix, patternList[i].modelMatrix);
    }
    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public void setZorder(GL4 gl4, float new_z){
    }
}
