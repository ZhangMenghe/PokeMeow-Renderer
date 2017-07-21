package main.java.org.cytoscape.pokemeow.internal.utils;

import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class CubicBezier {
    private Vector2 sourcePos;
    private Vector2 controlPos1;
    private Vector2 controlPos2;
    private Vector2 destinationPos;
    public static final int resolution = 120;
    private float stepSize;

    public CubicBezier(float srcx, float srcy, float ctr1x,float ctr1y, float ctr2x, float ctr2y, float destx, float desty){
        sourcePos = new Vector2(srcx, srcy);
        controlPos1 = new Vector2(ctr1x, ctr1y);
        controlPos2 = new Vector2(ctr2x, ctr2y);
        destinationPos = new Vector2(destx, desty);
        stepSize = 1.0f/(resolution-1);
    }

    private Vector2 interpolate(float t){
        Vector2 intermediate1 = new QuadraticBezier(sourcePos,controlPos1,controlPos2).interpolate(t);
        Vector2 intermediate2 = new QuadraticBezier(controlPos1, controlPos2,destinationPos).interpolate(t);
        return Vector2.add(Vector2.scalarMult((1-t),intermediate1), Vector2.scalarMult(t,intermediate2));
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
//    public Vector2[] getPointsOnCurves(int res){
//        Vector2 []points = new Vector2[res+1];
//        stepSize = 1.0f/(res-1);
//        float currentT = .0f;
//        for(int i=0; i<res; i++,currentT+=stepSize){
//            points[i] = interpolate(currentT);
//        }
//        points[res] = destinationPos;
//        return points;
//    }
}
