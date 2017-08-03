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
    public final static int arrDensity = 1;
    private GL4 gl;
    public pmSeparateArrowLine(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type) {
        super(gl4, srcx, srcy, destx, desty, type);
        origin.x = (srcx+destx)/2.0f;
        origin.y = (srcy+desty)/2.0f;
        gl = gl4;
        if(curveType == LINE_STRAIGHT){
            numOfPatterns = lineSegments/arrDensity;
            setPatternOnStraightLine(srcx,srcy,destx,desty);
        }
        else{
            numOfPatterns = QuadraticBezier.resolution/arrDensity;
            setPatternOnCurve();
        }

        connectMethod = CONNECT_PATTERN;
        dirty = true;
    }

    private void setPatternOnCurve(){
        float rlen = destPos.x - srcPos.x;
        int absNumOfPatterns = (int)(rlen * numOfPatterns);
        int step = numOfVertices / absNumOfPatterns;
        patternList = new pmBasicArrowShape[absNumOfPatterns];
        float deltax, deltay;
        for (int i = 0, n=0; i < absNumOfPatterns; i++,n+=step) {
            patternList[i] = new pmLineSeparateArrowPattern(gl);
            patternList[i].setScale(0.05f);
            deltay = vertices[3*(i+1)+1] - vertices[3*i+1];
            deltax = vertices[3*(i+1)] - vertices[3*i];
            float k = deltay / deltax;
            double theta = Math.atan(k);
            patternList[i].setRotation((float) theta - 3.14f/2);
            patternList[i].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
        }
    }
    private void setPatternOnStraightLine(float srcx, float srcy, float destx, float desty){
        double theta = Math.atan(slope);
        if(slope<0)
            theta -= 3.14f;

        float rlen = destPos.x - srcPos.x;
        numOfVertices = (int) (lineSegments * Math.abs(rlen)) + 1;
        int numOfPoints = 3*numOfVertices;
        vertices = new float[numOfPoints];
        float shrink = rlen/(numOfVertices-1);
        for (int i = 0, n = 2; i < numOfPoints; i += 3, n++) {
            vertices[i] = srcPos.x + shrink * n;
            vertices[i + 1] = srcPos.y + slope * (vertices[i] - srcPos.x);
            vertices[i + 2] = zorder;
        }

        int absNumOfPatterns = (int)(rlen * numOfPatterns)/2;
        int step = numOfVertices / absNumOfPatterns;
        patternList = new pmBasicArrowShape[absNumOfPatterns];
        for (int i = 0, n=0; i < absNumOfPatterns; i++,n+=step) {
            patternList[i] = new pmLineSeparateArrowPattern(gl);
            patternList[i].setScale(0.05f);
            patternList[i].setRotation((float) theta - 3.14f/2);
            patternList[i].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
        }
        //afterSetCurve = true;
    }

    public void setScale(Vector2 new_scale) {
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
        for (int i = 0; i < patternList.length; i++)
            patternList[i].setScale(new_scale);
    }
    public void setScale(float s_scale){
        scale.x = s_scale;
    }
    public void setOrigin(Vector2 new_origin){
        float gapx = new_origin.x-origin.x; float gapy = new_origin.y-origin.y;
        origin.x=new_origin.x;
        origin.y=new_origin.y;
        if(patternList!=null){
            for(pmBasicArrowShape arrow: patternList){
                arrow.setOrigin(gapx, gapy);
            }

        }
    }
    public void setOrigin(float gapx, float gapy){
        origin.x+=gapx;
        origin.y+=gapy;
        if(patternList!=null){
            for(pmBasicArrowShape arrow: patternList){
                arrow.setOrigin(gapx, gapy);
            }
        }
    }
    public void setRotation(float radians){
//        for (int i = 0; i < patternList.length; i++)
//            patternList[i].setRotation(radians);

    }
    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public void setZorder(GL4 gl4, float new_z){
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
            setQuadraticBezierCurveVertices(true);
        else
            setCubicBezierCurveVertices(true);
        dirty = true;

        double theta = 0;
        int n = 0;
        for (int i = 0; i < numOfPatterns-1; i++, n+=arrDensity) {
            float deltay = patternList[i+1].origin.y - patternList[i].origin.y;
            float deltax = patternList[i+1].origin.x - patternList[i].origin.x;
            float k = deltay / deltax;
            theta = Math.atan(k);
            if(k <0)
                theta -= 3.14f;
            patternList[i].setRotation((float) theta - 3.14f/2);
            patternList[i].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
        }
        patternList[numOfPatterns-1].setRotation((float) theta - 3.14f/2);
        patternList[numOfPatterns-1].setOrigin(new Vector3(vertices[3*n], vertices[3*n+1], zorder));
    }

    public void resetSrcAndDest(float srcx, float srcy, float destx, float desty){
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        origin.x = (srcx+destx)/2.0f;
        origin.y = (srcy+desty)/2.0f;
        float deltax = destx-srcx; float deltay = desty-srcy;
        slope = deltay/deltax;
        double theta = Math.atan(slope);
        int absNumOfPatterns = patternList.length;
        int step = numOfVertices / absNumOfPatterns;
        if(curveType == LINE_STRAIGHT) {
            if (destPos.x - srcPos.x < 0)
                theta -= 3.14f;


            float shrink = (destPos.x - srcPos.x) / (numOfVertices - 1);
            int numOfPoints = 3 * numOfVertices;
            for (int i = 0, n = 0; i < numOfPoints; i += 3, n++) {
                vertices[i] = srcPos.x + shrink * n;
                vertices[i + 1] = srcPos.y + slope * (vertices[i] - srcPos.x);
                vertices[i + 2] = zorder;
            }
            for (int i = 0, n = 2; i < absNumOfPatterns; i++, n += step) {
                patternList[i].setRotation((float) theta - 3.14f / 2);
                patternList[i].setOrigin(new Vector3(vertices[3 * n], vertices[3 * n + 1], zorder));
            }
            return;
        }
        else{
            if(curveType == LINE_QUADRIC_CURVE){
                setQuadraticBezierCurveVertices();
                anchor.setPosition(controlPoints[0], controlPoints[1]);
            }
            else{
                setCubicBezierCurveVertices();
                anchor.setPosition(controlPoints[0], controlPoints[1]);
                anchor2.setPosition(controlPoints[2], controlPoints[3]);
            }
            for (int i = 0, n = 0; i < absNumOfPatterns; i++, n += step) {
                patternList[i].setRotation((float) theta - 3.14f / 2);
                patternList[i].setOrigin(new Vector3(vertices[3 * n], vertices[3 * n + 1], zorder));
            }
        }
    }
}
