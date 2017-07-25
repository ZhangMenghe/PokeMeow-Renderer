package main.java.org.cytoscape.pokemeow.internal.utils;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class QuadraticBezier {
    private Vector2 sourcePos;
    private Vector2 controlPos;
    private Vector2 destinationPos;
    public static final int resolution = 160;
    private float stepSize;

    public QuadraticBezier(float srcx, float srcy, float controlx, float controly, float destx, float desty){
        sourcePos = new Vector2(srcx, srcy);
        controlPos = new Vector2(controlx, controly);
        destinationPos = new Vector2(destx, desty);
        stepSize = 1.0f/(resolution-1);
    }

    public QuadraticBezier(Vector2 src, Vector2 control, Vector2 dest){
        sourcePos = src;
        controlPos = control;
        destinationPos = dest;
        stepSize = 1.0f/(resolution-1);
    }

    public Vector2 interpolate(float t){
        Vector2 intermediate1 = Vector2.add(Vector2.scalarMult((1-t), sourcePos),Vector2.scalarMult(t,controlPos));
        Vector2 intermediate2 = Vector2.add(Vector2.scalarMult((1-t), controlPos),Vector2.scalarMult(t,destinationPos));
        return Vector2.add(Vector2.scalarMult((1-t), intermediate1), Vector2.scalarMult(t, intermediate2));
    }

    public Vector2[] getPointsOnCurves(){
        Vector2 []points = new Vector2[resolution+1];
        float currentT = .0f;
        for(int i=0; i<resolution; i++,currentT+=stepSize){
            points[i] = interpolate(currentT);
        }
        points[resolution] = destinationPos;
        return points;
    }
    public float[] getPointsOnCurves(float z){
        float []points = new float[(resolution+1)*3];
        float currentT = .0f;
        for(int i=0; i<resolution; i++,currentT+=stepSize){
            Vector2 tmp = interpolate(currentT);
            points[3*i] = tmp.x;
            points[3*i+1] = tmp.y;
            points[3*i+2] = z;
        }
        points[3*resolution] = destinationPos.x;
        points[3*resolution+1] = destinationPos.y;
        points[3*resolution+2] = z;
        return points;
    }
//    public static void main(String[] args) {
//        Vector2 source = new Vector2((float) 1.0, (float) 0.0);
//        Vector2 control = new Vector2((float) 0.0, (float) 2.0);
//        Vector2 destination = new Vector2((float) 2.0, (float) 1.0);
//
//        QuadraticBezier bluh = new QuadraticBezier(source,control,destination);
//        Vector2[] points = bluh.getPointsOnCurves();
//
//        System.out.print("[");
//        for (Vector2 point : points) {
//            System.out.println(point.x + ", " + point.y + ";");
//        }
//        System.out.println("];");
//    }
}
