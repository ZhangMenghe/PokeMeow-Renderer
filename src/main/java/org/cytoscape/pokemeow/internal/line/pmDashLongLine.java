package org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import org.cytoscape.pokemeow.internal.algebra.Matrix4;
import org.cytoscape.pokemeow.internal.algebra.Vector2;
import org.cytoscape.pokemeow.internal.utils.CubicBezier;
import org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */

public class pmDashLongLine extends pmLineVisual{
    public pmDashLongLine(GL4 gl4, float srcx, float srcy, float destx, float desty, byte type, boolean initBuffer){
        super(gl4, srcx, srcy, destx, desty, type, initBuffer);
        connectMethod = CONNECT_SEGMENTS;
        if(curveType == LINE_STRAIGHT){
            lineSegments=15;
            float rlen;
            if(slope>1 || slope<-1)
                rlen = desty - srcy;
            else
                rlen = destx - srcx;
            numOfVertices = (int)(lineSegments * Math.abs(rlen)) +1;
            int numOfPoints = 3*numOfVertices;
            vertices = new float[numOfPoints];
            float shrink = 0.25f/(numOfVertices-1);
            vertices[0]=-0.5f; vertices[1]=.0f; vertices[2]=zorder;
            for (int i = 3, n = 1; i < numOfPoints; i += 3, n++) {
                if (n % 2 == 1)
                    vertices[i] = vertices[i - 3] + shrink * 5;
                else
                    vertices[i] = vertices[i - 3] + shrink * 3;
                vertices[i + 1] = .0f;
                vertices[i + 2] = zorder;
            }
        }
        if(initBuffer)
            initLineVisual(gl4);
    }

    @Override
    protected void setQuadraticBezierCurveVertices(boolean skip){
        numOfVertices = QuadraticBezier.resolution + 1;
        vertices = new float[3*numOfVertices];

        QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], destPos.x, destPos.y);
        Vector2[] curvePoints = curve.getPointsOnCurves(QuadraticBezier.resolution * 4);
        modelMatrix = Matrix4.identity();
        _curveOffset.x = origin.x;
        _curveOffset.y = origin.y;
        //afterSetCurve = true;
        for(int k=0, n=0; k<numOfVertices; k++,n++){
            vertices[3*k] = curvePoints[n].x;
            vertices[3*k+1] = curvePoints[n].y;
            vertices[3*k+2] = zorder;//z
            if(k%2==0)
                n+=6;
        }
    }

    @Override
    protected void setCubicBezierCurveVertices(boolean skip){
        numOfVertices = CubicBezier.resolution + 1;
        vertices = new float[3*numOfVertices];
        CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1],controlPoints[2], controlPoints[3], destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves(CubicBezier.resolution*4);
        modelMatrix = Matrix4.identity();
        _curveOffset.x = origin.x;
        _curveOffset.y = origin.y;
        //afterSetCurve = true;
        for(int k=0, n=0; k<numOfVertices; k++,n++){
            vertices[3*k] = curvePoints[n].x;
            vertices[3*k+1] = curvePoints[n].y;
            vertices[3*k+2] = zorder;
            if(k%2==0)
                n+=6;
        }
    }
}

