package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmVerticalSlashLine extends pmPatternLineBasic{
    private float[] singlePattern = {
            -0.25f, 0.25f, .0f,
            -0.25f, -0.25f, .0f,
            0.25f, 0.25f, .0f,
            0.25f, -0.25f, .0f
    };

    public pmVerticalSlashLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type) {
        super(gl4, srcx, srcy, destx, desty, type);
        pointsPerPattern = 4;
        if (curveType == LINE_STRAIGHT)
            initVertices(singlePattern);
        else
            initCurveVertices(singlePattern);
        initLineVisual(gl4);
    }
    public void setControlPoints(float nctrx, float nctry, int anchorID){
        super.setControlPoints(nctrx,nctry,anchorID,singlePattern);
    }
    @Override
    public void resetSrcAndDest(float srcx, float srcy, float destx, float desty){
        dirty = true;
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        slope = (desty - srcy) / (destx - srcx);

        if(curveType == LINE_STRAIGHT){
            initVertices(singlePattern);
            return;
        }
        float [] curvePoints;
        if(curveType == LINE_QUADRIC_CURVE){
            if(Math.abs(slope)<=1){
                controlPoints[0] =(srcx + destx)/2.0f;
                controlPoints[1] =(srcy + desty)/2.0f+0.1f;
            }
            else{
                controlPoints[0] =(srcx + destx)/2.0f+0.1f;
                controlPoints[1] =(srcy + desty)/2.0f;
            }
            QuadraticBezier curve = new QuadraticBezier(srcx,srcy,controlPoints[0],controlPoints[1],destx,desty);
            curvePoints = curve.getPointsOnCurves(zorder);
            anchor.setPosition(controlPoints[0], controlPoints[1]);
        }
        else{
            if(Math.abs(slope)<=1){
                float tmpx = (destx-srcx)/3.0f;
                float tmpy = (srcy + desty)/2.0f;
                controlPoints[0] =tmpx+srcx;
                controlPoints[1] =tmpy+0.1f;
                controlPoints[2] =tmpx*2+srcx;
                controlPoints[4] =tmpy+0.1f;
            }
            else{
                float tmpx = (destx+srcx)/2.0f;
                float tmpy = (desty-srcy)/3.0f;
                controlPoints[0] = tmpx+0.1f;
                controlPoints[1] = tmpy+srcy;
                controlPoints[2] = tmpx+0.1f;
                controlPoints[4] = 2*tmpy+srcy;
            }
            CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3], destPos.x, destPos.y);
            curvePoints = curve.getPointsOnCurves(zorder);
            anchor.setPosition(controlPoints[0], controlPoints[1]);
            anchor2.setPosition(controlPoints[2], controlPoints[3]);
        }
        setCurveVerticesByPattern(curvePoints, singlePattern);
    }

}
