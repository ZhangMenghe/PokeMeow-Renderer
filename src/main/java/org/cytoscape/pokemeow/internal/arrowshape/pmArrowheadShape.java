package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;
/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class pmArrowheadShape extends pmBasicArrowShape{
//    public float[] vertices;
//    public int [] elements;
    protected QuadraticBezier curve = new QuadraticBezier(-0.5f, -0.25f,.0f,.0f,0.5f,.0f);
    protected QuadraticBezier curve2 = new QuadraticBezier(0.5f,.0f,.0f,.0f,-0.5f, 0.25f);

    public pmArrowheadShape(GL4 gl4){
        super();
        initPoints(gl4);
        setScale(0.5f);
    }

    public pmArrowheadShape(GL4 gl4, boolean skip){
        super();
        setScale(0.5f);
    }

    protected void initPoints(GL4 gl4){
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        int halfVertices = curve.resolution + 1;//include both first and last point
        numOfVertices = halfVertices*2;
        numOfIndices = 3*(numOfVertices-2);
        vertices = new float[6 * halfVertices];
        elements = new int[numOfIndices];

        int []tmpHandle = new int[numOfVertices];
        for(int k=0; k<halfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = .0f;//z
            tmpHandle[2*k] = k;
        }

        curvePoints = curve2.getPointsOnCurves();
        int base = 3 * halfVertices;
        for(int k=0;k<halfVertices;k++){
            vertices[3*k+base] = curvePoints[k].x;
            vertices[3*k+1+base] = curvePoints[k].y;
            vertices[3*k+2+base] = .0f;//z
            tmpHandle[2*k+1] = numOfVertices-1-k;
        }
        for(int i=0; i< numOfVertices-2;i++){
            elements[3*i] = tmpHandle[i];
            elements[3*i+1] = tmpHandle[i+1];
            elements[3*i+2] = tmpHandle[i+2];
        }
        this.initBuffer(gl4, true);
    }

    public void setZorder(GL4 gl4, float new_z){
        int length = vertices.length;
        for(int i =2; i<length; i+=3)
            vertices[i] = new_z;
        this.initBuffer(gl4, true);
    }
}
