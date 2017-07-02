package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;
/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class pmArrowheadShape extends pmBasicArrowShape{
    public float[] vertices;
    private QuadraticBezier curve;

    public pmArrowheadShape(GL4 gl4){
        super();
        curve = new QuadraticBezier(-1.0f, -0.5f,.0f,.0f,1.0f,.0f);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        int halfVertices = curve.resolution + 1;
        vertices = new float[6 * halfVertices];

        for(int k=0;k<halfVertices;k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = .0f;//z
        }

        curve = new QuadraticBezier(1.0f,.0f,.0f,.0f,-1.0f, 0.5f);
        curvePoints = curve.getPointsOnCurves();
        int base = 3*halfVertices;
        for(int k=0;k<halfVertices;k++){
            vertices[3*k+base] = curvePoints[k].x;
            vertices[3*k+1+base] = curvePoints[k].y;
            vertices[3*k+2+base] = .0f;//z
        }
        this.initBuffer(gl4, vertices);
    }

    public void setZorder(GL4 gl4, float new_z){
        int length = vertices.length;
        for(int i =2; i<length; i+=3)
            vertices[i] = new_z;
        this.initBuffer(gl4, vertices);
    }
}
