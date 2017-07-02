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
    private final int resolution = 20;
    private float stepSize;

    public CubicBezier(Vector2 src, Vector2 control1, Vector2 control2, Vector2 dest){
        sourcePos = src;
        controlPos1 = control1;
        controlPos2 = control2;
        destinationPos = dest;
        stepSize = 1.0f/(resolution-1);
    }

    private Vector2 interpolate(float t){
        Vector2 intermediate1 = new QuadraticBezier(sourcePos,controlPos1,controlPos2).interpolate(t);
        Vector2 intermediate2 = new QuadraticBezier(controlPos1, controlPos2,destinationPos).interpolate(t);
        return Vector2.add(Vector2.scalarMult(t,intermediate1), Vector2.scalarMult(t,intermediate2));
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
}
