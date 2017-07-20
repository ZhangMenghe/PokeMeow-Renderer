package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
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

    public pmSolidLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4);
        curveType = type;
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        if(curveType == LINE_STRAIGHT){
            numOfVertices = 2;
            vertices = new float[6];
            vertices[0]=srcx; vertices[1]=srcy; vertices[2]=zorder;
            vertices[3]=destx; vertices[4]=desty; vertices[5]=zorder;
        }
        if(curveType == LINE_QUADRIC_CURVE){
            numOfVertices = QuadraticBezier.resolution + 1;
            vertices = new float[3*numOfVertices];
            controlPoints = new float[2];
            setQuadraticBezierCurveVertices((srcx + destx)/2.0f,(srcy + desty)/2.0f+0.125f);
            anchor = new pmAnchor(gl4, controlPoints[0], controlPoints[1]);
        }

        if(curveType == LINE_CUBIC_CURVE){
            numOfVertices = CubicBezier.resolution + 1;
            vertices = new float[3*numOfVertices];
            controlPoints = new float[4];
            float tmpx = (destx-srcx)/3.0f; float tmpy = (srcy + desty)/2.0f+0.125f;
            setCubicBezierCurveVertices(tmpx+srcx, tmpy, tmpx*2+srcx, tmpy);
            anchor = new pmAnchor(gl4, controlPoints[0], controlPoints[1]);
            anchor2 = new pmAnchor(gl4, controlPoints[2], controlPoints[3]);
        }
        initLineVisual(gl4, vertices);
    }

    private void setQuadraticBezierCurveVertices(float ctrx, float ctry){
        controlPoints[0] = ctrx;
        controlPoints[1] = ctry;

        QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, ctrx, ctry, destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();

        for(int k=0; k<numOfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = .0f;//z
        }
    }

    private void setCubicBezierCurveVertices(float ctr1x, float ctr1y, float ctr2x, float ctr2y){
        controlPoints[0] = ctr1x; controlPoints[1] = ctr1y;
        controlPoints[2] = ctr2x; controlPoints[3] = ctr2y;
        CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, ctr1x, ctr1y, ctr2x, ctr2y, destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        for(int k=0; k<numOfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = .0f;//z
        }
    }

    public void setControlPoints(float nctrx, float nctry, int anchorID){
        if(anchorID == 1){
            controlPoints[0] = nctrx; controlPoints[1] = nctry;
            anchor.setPosition(nctrx, nctry);
        }
        else{
            controlPoints[2] = nctrx; controlPoints[3] = nctry;
            anchor2.setPosition(nctrx, nctry);
        }
        if(curveType == LINE_QUADRIC_CURVE)
            setQuadraticBezierCurveVertices(nctrx, nctry);
        else
            setCubicBezierCurveVertices(controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3]);
        dirty = true;
    }
}
