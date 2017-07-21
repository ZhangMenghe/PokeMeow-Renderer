package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmSeparateArrowLine extends pmPatternLineBasic {
    private pmBasicArrowShape[] arrowList;
    public pmSeparateArrowLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type) {
        super(gl4, srcx, srcy, destx, desty, type);
        if(curveType == LINE_QUADRIC_CURVE){
            numOfPatterns = QuadraticBezier.resolution/6;
            arrowList = new pmBasicArrowShape[numOfPatterns];
            for (int i = 0, n=0; i < numOfPatterns; i++,n+=6) {
                arrowList[i] = new pmLineSeparateArrowPattern(gl4);
                arrowList[i].setScale(0.05f);
                //should calculate tangent of curve
                float deltay = vertices[3*(i+1)+1] - vertices[3*i+1];
                float deltax = vertices[3*(i+1)] - vertices[3*i];
                float k = deltay / deltax;
                double theta = Math.atan(k);
                arrowList[i].setRotation((float) theta - 3.14f/2);
                arrowList[i].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
            }
        }
        if(curveType == LINE_STRAIGHT) {
            float deltay = desty - srcy;
            float deltax = destx - srcx;
            float k = deltay / deltax;
            double theta = Math.atan(k);
            double length2 =deltay*deltay + deltax*deltax;
            numOfPatterns  = 20*(int)(length2/2);

            shrink = (float) Math.sqrt(length2)/numOfPatterns;
            arrowList = new pmBasicArrowShape[numOfPatterns];
            for (int i = 0; i < numOfPatterns; i++) {
                arrowList[i] = new pmLineSeparateArrowPattern(gl4);
                arrowList[i].setScale(0.05f);
                arrowList[i].setRotation((float) theta - 3.14f/2);
                float tmpx = srcx+shrink*i;
                float tmpy = srcy+k*shrink*i;
                arrowList[i].setOrigin(new Vector3(tmpx, tmpy, zorder));
            }
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
    public void setControlPoints(float nctrx, float nctry, int anchorID){
        super.setControlPoints(nctrx,nctry,anchorID);
        if(curveType == LINE_QUADRIC_CURVE){
            numOfPatterns = QuadraticBezier.resolution/6;
            arrowList = new pmBasicArrowShape[numOfPatterns];
            double theta = 0;
            int n = 0;
            for (int i = 0; i < numOfPatterns-1; i++,n+=6) {
                float deltay = patternList[i+1].origin.y - patternList[i].origin.y;
                float deltax = patternList[i+1].origin.x - patternList[i].origin.x;
                float k = deltay / deltax;
                theta = Math.atan(k);
                patternList[i].setRotation((float) theta - 3.14f/2);
                patternList[i].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
            }
            patternList[numOfPatterns-1].setRotation((float) theta - 3.14f/2);
            patternList[numOfPatterns-1].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
        }
    }
}
