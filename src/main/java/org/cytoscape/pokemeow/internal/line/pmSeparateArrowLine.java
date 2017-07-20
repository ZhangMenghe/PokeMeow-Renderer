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
    public pmSeparateArrowLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type) {
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_STRAIGHT) {
            float deltay = desty - srcy;
            float deltax = destx - srcx;
            float k = deltay / deltax;
            double theta = Math.atan(k);
            double length2 =deltay*deltay + deltax*deltax;
            numOfPatterns  = 20*(int)(length2/2);

            shrink = (float) Math.sqrt(length2)/numOfPatterns;
            pmBasicArrowShape[] arrowList = new pmBasicArrowShape[numOfPatterns];
            for (int i = 0; i < numOfPatterns; i++) {
                arrowList[i] = new pmLineSeparateArrowPattern(gl4);
                arrowList[i].setScale(0.05f);
                arrowList[i].setRotation((float) theta - 3.14f/2);
                float tmpx = srcx+shrink*i;
                float tmpy = srcy+k*shrink*i;
                arrowList[i].setOrigin(new Vector3(tmpx, tmpy, zorder));
            }
            initLineVisual(gl4, arrowList);
        }
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
