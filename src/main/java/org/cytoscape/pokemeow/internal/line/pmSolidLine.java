package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmSolidLine extends pmLineVisual {
    float[] vertices = {-1.0f, .0f, .0f, 1.0f, .0f, .0f};
    public pmSolidLine(GL4 gl4){
        super(gl4);
        initLineVisual(gl4, vertices);
    }

    public pmSolidLine(GL4 gl4, float srcx, float srcy, float destx, float desty){
        super(gl4);
        float ctrx = (srcx+destx)/2.0f;
        float ctry = (srcy + desty)/2.0f+0.125f;
        QuadraticBezier curve = new QuadraticBezier(srcx, srcy, ctrx, ctry, destx, desty);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        numOfVertices = curve.resolution+1;
        vertices = new float[3*numOfVertices];
        for(int k=0; k<numOfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = .0f;//z
        }
        initLineVisual(gl4, vertices);
        anchor = new pmAnchor(gl4, ctrx, ctry+0.01f);
    }
}
