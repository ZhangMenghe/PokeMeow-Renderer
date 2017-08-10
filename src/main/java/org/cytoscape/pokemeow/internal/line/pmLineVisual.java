package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.utils.CubicBezier;
import main.java.org.cytoscape.pokemeow.internal.utils.QuadraticBezier;

/**
 * Created by ZhangMenghe on 2017/7/6.
 */
public class pmLineVisual extends pmBasicArrowShape {
    private float width = 1.0f;
    protected int lineSegments = 40;
    public int numOfPatterns = 15;
    protected float curveFactor = 2.0f;
    public Byte connectMethod = 0; //default to be connect strip
    public Byte curveType = 0;     //default to straight line
    public pmBasicArrowShape[] patternList;//used for arrow shape line
    public pmLineVisual [] plineList = null;//used for parallel
    //public float zorder = .0f;
    public float slope;

    public pmAnchor anchor = null;
    public pmAnchor anchor2 = null;
    public float[] controlPoints;
    public Vector2 srcPos = new Vector2();
    public Vector2 destPos = new Vector2();

    public static final byte CONNECT_STRIP = 0;//line to be drawn with each vertex connected to the next.
    public static final byte CONNECT_SEGMENTS = 1;//draw an independent line segment
    public static final byte CONNECT_DOTS = 2;//draw independent dots
    public static final byte CONNECT_PATTERN = 3;//draw triangle fans
    public static final byte CONNECT_PARALLEL = 4;//should specify an array of segment pairs

    public static final byte LINE_STRAIGHT = 0;
    public static final byte LINE_QUADRIC_CURVE = 1;
    public static final byte LINE_CUBIC_CURVE = 2;
    public boolean afterSetCurve = false;
    protected float baseLength = 1.0f;
    protected Vector2 _curveOffset = new Vector2(.0f,.0f);
    public float hitThreshold = 1.0f;
    public pmLineVisual(){super();}
    public pmLineVisual(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type, boolean initBuffer){
        curveType = type;
        if(curveType == LINE_QUADRIC_CURVE){
            numOfVertices = QuadraticBezier.resolution + 1;
            vertices = new float[3*numOfVertices];
            controlPoints = new float[2];
            if(initBuffer)
                anchor = new pmAnchor(gl4,.0f,.0f);
            else
                anchor = new pmAnchor(.0f,.0f);
        }
        if(curveType == LINE_CUBIC_CURVE){
            numOfVertices = CubicBezier.resolution + 1;
            vertices = new float[3*numOfVertices];
            controlPoints = new float[4];
            if(initBuffer){
                anchor = new pmAnchor(gl4,.0f,.0f);
                anchor2 = new pmAnchor(gl4,.0f,.0f);
            }else{
                anchor = new pmAnchor(.0f,.0f);
                anchor2 = new pmAnchor(.0f,.0f);
            }
        }
        setSrcAndDest(srcx,srcy,destx,desty);
    }

    public pmLineVisual(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        curveType = type;
        if(curveType == LINE_QUADRIC_CURVE){
            numOfVertices = QuadraticBezier.resolution + 1;
            vertices = new float[3*numOfVertices];
            controlPoints = new float[2];
            anchor = new pmAnchor(gl4,.0f,.0f);
        }
        if(curveType == LINE_CUBIC_CURVE){
            numOfVertices = CubicBezier.resolution + 1;
            vertices = new float[3*numOfVertices];
            controlPoints = new float[4];
            anchor = new pmAnchor(gl4,.0f,.0f);
            anchor2 = new pmAnchor(gl4,.0f,.0f);
        }
        setSrcAndDest(srcx,srcy,destx,desty);
    }

    public static pmLineVisual getCloneLine(pmLineVisual line){
        pmLineVisual cloned = new pmLineVisual();
        cloned.width = line.width;
        cloned.numOfVertices = line.numOfVertices;
        cloned.connectMethod = line.connectMethod;
        cloned.patternList = line.patternList;
        cloned.plineList = line.plineList;
        cloned.vertices = new float[3*line.numOfVertices];
        for(int i=0;i<line.vertices.length;i++)
            cloned.vertices[i] = line.vertices[i];
        cloned.modelMatrix = Matrix4.getClone(line.modelMatrix);
        cloned.srcPos = line.srcPos;
        cloned.destPos = line.destPos;
        cloned.controlPoints = line.controlPoints;
        cloned.anchor = line.anchor;
        cloned.anchor2 = line.anchor2;
        return cloned;
    }

    protected void initLineVisual(GL4 gl4, pmLineVisual line){
        plineList = new pmLineVisual[2];
        plineList[0] = line;
        plineList[1] = getCloneLine(line);
        plineList[1].initBuffer(gl4);
    }

    protected void initLineVisual(GL4 gl4){
        initBuffer(gl4);
    }

    public void setLineWidth(GL4 gl4, float mwidth){
        width = mwidth;
        gl4.glLineWidth(width);
    }

    protected void setQuadraticBezierCurveVertices(){
        setControlPoints();
        setQuadraticBezierCurveVertices(true);
    }

    protected void setCubicBezierCurveVertices(){
        setControlPoints();
        setCubicBezierCurveVertices(true);
    }

    protected void setQuadraticBezierCurveVertices(boolean skip){
        QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        modelMatrix = Matrix4.identity();
        _curveOffset.x = origin.x;
        _curveOffset.y = origin.y;
        //afterSetCurve = true;
        for(int k=0; k<numOfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = zorder;
        }
    }

    protected void setCubicBezierCurveVertices(boolean skip){
        CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3], destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
        modelMatrix = Matrix4.identity();
        _curveOffset.x = origin.x;
        _curveOffset.y = origin.y;
        //afterSetCurve = true;
        for(int k=0; k<numOfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = zorder;
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
            setQuadraticBezierCurveVertices(true);
        else
            setCubicBezierCurveVertices(true);
        dirty = true;
    }
    public void setControlPoints(){
        if(curveType == LINE_QUADRIC_CURVE){
            if(Math.abs(slope)<=1){
                controlPoints[0] = (srcPos.x + destPos.x) / 2.0f;
                controlPoints[1] = (srcPos.y + destPos.y) / 2.0f+0.1f;
            }
            else{
                controlPoints[0] = (srcPos.x + destPos.x) / 2.0f+0.1f;
                controlPoints[1] = (srcPos.y + destPos.y) / 2.0f;
            }
        }
        else{
            if(Math.abs(slope)<=1){
                float tmpx = (destPos.x-srcPos.x)/3.0f;
                float tmpy = (srcPos.y + destPos.y)/2.0f;
                controlPoints[0] = tmpx+srcPos.x; controlPoints[1] = tmpy+0.1f;
                controlPoints[2] = tmpx*2+srcPos.x; controlPoints[3] = controlPoints[1];
            }
            else{
                float tmpx = (destPos.x+srcPos.x)/2.0f;
                float tmpy = (destPos.y-srcPos.y)/3.0f;
                controlPoints[0] = tmpx+0.1f; controlPoints[1] = tmpy+srcPos.y;
                controlPoints[2] = controlPoints[0]; controlPoints[3] = 2*tmpy+srcPos.y;
            }
        }
    }


    public void setRotation(float radians){
        rotMatrix = Matrix4.mult(rotMatrix,Matrix4.rotationZ(radians));

        float cost = (float)Math.cos(radians);
        float sint = (float)Math.sin(radians);
        float tmpx,tmpy;
        tmpx = srcPos.x - origin.x;
        tmpy = srcPos.y - origin.y;
        srcPos.x = tmpx*cost - tmpy*sint + origin.x;
        srcPos.y = tmpx*sint + tmpy*cost + origin.y;

        tmpx = destPos.x - origin.x;
        tmpy = destPos.y - origin.y;
        destPos.x = tmpx*cost - tmpy*sint + origin.x;
        destPos.y = tmpx*sint + tmpy*cost + origin.y;

        slope = (destPos.y - srcPos.y) / (destPos.x - srcPos.x);


        if(curveType == LINE_STRAIGHT){
            updateMatrix();
            return;
        }
        //TODO:Is there a rotation way without reseting?
        //TODO:ROTATE+afterSetCurve+TRANSLATE = BUG
        if(afterSetCurve){
//            setOrigin(new Vector2(-_deltax, -_deltay));
//            updateMatrix();
//            setOrigin(_deltax, _deltay);
            //afterSetCurve = false;
            resetSrcAndDest(srcPos.x,srcPos.y,destPos.x,destPos.y);
        }
        else{
            updateMatrix();

        }
        //        tmpx = controlPoints[0] - origin.x;
//        tmpy = controlPoints[1] - origin.y;
//        controlPoints[0] = tmpx*cost - tmpy*sint + origin.x;
//        controlPoints[1] = tmpx*sint + tmpy*cost + origin.y;
//        tmpx = anchor.vertices[0] - origin.x;
//        tmpy = anchor.vertices[1] - origin.y;
//        anchor.setPosition(tmpx*cost - tmpy*sint + origin.x, tmpx*sint + tmpy*cost + origin.y);
//        if(anchor2!=null){
//            tmpx = controlPoints[2] - origin.x;
//            tmpy = controlPoints[3] - origin.y;
//            controlPoints[2] = tmpx*cost - tmpy*sint + origin.x;
//            controlPoints[3] = tmpx*sint + tmpy*cost + origin.y;
//            tmpx = anchor2.vertices[0] - origin.x;
//            tmpy = anchor2.vertices[1] - origin.y;
//            anchor2.setPosition(tmpx*cost - tmpy*sint + origin.x, tmpx*sint + tmpy*cost + origin.y);
//        }

    }
    public void setScale(float s_scale){
        scale.x = s_scale;
        updateMatrix();
    }

    public void setOrigin(Vector2 new_origin){
        origin.x=new_origin.x;
        origin.y=new_origin.y;
        updateMatrix();
        modelMatrix.e14 -= _curveOffset.x;
        modelMatrix.e24 -= _curveOffset.y;
    }
    public void setOrigin(float gapx, float gapy){
        origin.x+=gapx;
        origin.y+=gapy;
        updateMatrix();
        modelMatrix.e14 -= _curveOffset.x;
        modelMatrix.e24 -= _curveOffset.y;
    }
    public void resetSrcAndDest(float srcx, float srcy, float destx, float desty){
        setSrcAndDest(srcx,srcy,destx,desty);
    }

    protected void setSrcAndDest(float srcx, float srcy, float destx, float desty){
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        float deltax = destx-srcx; float deltay = desty-srcy;
        slope = deltay/deltax;
        float theta = (float) Math.atan(slope);
//        float lastorix = origin.x;float lastoriy=origin.y;
        origin.x = (srcx+destx)/2.0f; origin.y = (srcy+desty)/2.0f;
        if(curveType == LINE_STRAIGHT){
            setOrigin(new Vector3((srcx+destx)/2.0f, (srcy+desty)/2.0f, .0f));
            super.setRotation(theta);
            float length = (float)Math.sqrt(deltax*deltax + deltay*deltay) * baseLength;
            setScale(length);
            return;
        }
        dirty = true;


//        float _deltax = lastorix-origin.x; float _deltay = lastoriy-origin.y;
        if(curveType == LINE_QUADRIC_CURVE){
            setQuadraticBezierCurveVertices();
            anchor.setPosition(controlPoints[0], controlPoints[1]);
        }
        else{
            setCubicBezierCurveVertices();
            anchor.setPosition(controlPoints[0], controlPoints[1]);
            anchor2.setPosition(controlPoints[2], controlPoints[3]);
        }
    }

}
