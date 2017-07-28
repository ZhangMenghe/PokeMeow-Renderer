package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
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
    public int numOfPatterns = 20;
    public Byte connectMethod = 0; //default to be connect strip
    public Byte curveType = 0;     //default to straight line
    private int[] connectArray = null;//if use CONNECT_ARRAY, then specify
    public pmBasicArrowShape[] patternList;//used for arrow shape line
    public pmLineVisual [] plineList = null;//used for parallel
    public float zorder = .0f;
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
    public static final byte CONNECT_ARRAY = 4;//should specify an array of segment pairs
    public static final byte CONNECT_PARALLEL = 5;//should specify an array of segment pairs

    public static final byte LINE_STRAIGHT = 0;
    public static final byte LINE_QUADRIC_CURVE = 1;
    public static final byte LINE_CUBIC_CURVE = 2;

    public pmLineVisual(){super();}
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

    public pmLineVisual(GL4 gl4, pmLineVisual line){
        super();
        width = line.width;
        connectMethod = line.connectMethod;
        connectArray = line.connectArray;
        patternList = line.patternList;
        plineList = line.plineList;
        vertices = new float[3*line.numOfVertices];
        for(int i=0;i<line.vertices.length;i++)
            vertices[i] = line.vertices[i];
        controlPoints = line.controlPoints;
        anchor = line.anchor;
        anchor2 = line.anchor2;
        initLineVisual(gl4);
    }

    public pmLineVisual(GL4 gl4, float[] pos, Byte mconnectMethod){
        super();
        if(mconnectMethod != CONNECT_ARRAY)
            connectMethod = mconnectMethod;
        initLineVisual(gl4);
    }

    public pmLineVisual(GL4 gl4, float[] pos, int [] mconnectArray){
        super();
        connectMethod = CONNECT_ARRAY;
        connectArray = mconnectArray;
        initLineVisual(gl4);
    }

    protected void initLineVisual(GL4 gl4, pmLineVisual line){
        plineList = new pmLineVisual[2];
        plineList[0] = line;
        plineList[1] = new pmLineVisual(gl4, line);
        if(Math.abs(line.slope) <= 1)
            plineList[1].setOrigin(new Vector3(line.origin.x, line.origin.y + 0.02f, line.origin.z));
        else
            plineList[1].setOrigin(new Vector3(line.origin.x + 0.02f, line.origin.y , line.origin.z));
    }

    protected void initLineVisual(GL4 gl4){
        elements = connectArray;
        numOfVertices = vertices.length/3;
        if(connectMethod != CONNECT_ARRAY)
            initBuffer(gl4);
        else{
            numOfIndices = connectArray.length;
            initBuffer(gl4, true);
        }

    }

    public void setLineWidth(GL4 gl4, float mwidth){
        width = mwidth;
        gl4.glLineWidth(width);
    }

    protected void setQuadraticBezierCurveVertices(float ctrx, float ctry){
        controlPoints[0] = ctrx;
        controlPoints[1] = ctry;

        QuadraticBezier curve = new QuadraticBezier(srcPos.x, srcPos.y, ctrx, ctry, destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();

        for(int k=0; k<numOfVertices; k++){
            vertices[3*k] = curvePoints[k].x;
            vertices[3*k+1] = curvePoints[k].y;
            vertices[3*k+2] = zorder;
        }
    }

    protected void setCubicBezierCurveVertices(float ctr1x, float ctr1y, float ctr2x, float ctr2y){
        controlPoints[0] = ctr1x; controlPoints[1] = ctr1y;
        controlPoints[2] = ctr2x; controlPoints[3] = ctr2y;
        CubicBezier curve = new CubicBezier(srcPos.x, srcPos.y, ctr1x, ctr1y, ctr2x, ctr2y, destPos.x, destPos.y);
        Vector2 [] curvePoints = curve.getPointsOnCurves();
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
            setQuadraticBezierCurveVertices(nctrx, nctry);
        else
            setCubicBezierCurveVertices(controlPoints[0], controlPoints[1], controlPoints[2], controlPoints[3]);
        dirty = true;
    }
    public void resetSrcAndDest(float srcx, float srcy, float destx, float desty){
        setSrcAndDest(srcx,srcy,destx,desty);
    }
    public void setRotation(float radians){
        float radia = radians;//- lastRadians;
        //lastRadians = radians;
        float cost = (float)Math.cos(radia);
        float sint = (float)Math.sin(radia);
        float tmpx,tmpy;

        for(int i=0; i<numOfVertices; i++){
            tmpx = vertices[3*i]-origin.x;
            tmpy = vertices[3*i+1]-origin.y;
            vertices[3*i] = tmpx*cost - tmpy*sint + origin.x;
            vertices[3*i+1] = tmpx*sint + tmpy*cost + origin.y;
        }
        tmpx = srcPos.x - origin.x;
        tmpy = srcPos.y - origin.y;
        srcPos.x = tmpx*cost - tmpy*sint + origin.x;
        srcPos.y = tmpx*sint + tmpy*cost + origin.y;

        tmpx = destPos.x - origin.x;
        tmpy = destPos.y - origin.y;
        destPos.x = tmpx*cost - tmpy*sint + origin.x;
        destPos.y = tmpx*sint + tmpy*cost + origin.y;
        slope = (destPos.y - srcPos.y) / (destPos.x - srcPos.x);
        dirty = true;
        if(curveType == LINE_STRAIGHT)
            return;
        tmpx = controlPoints[0] - origin.x;
        tmpy = controlPoints[1] - origin.y;
        controlPoints[0] = tmpx*cost - tmpy*sint + origin.x;
        controlPoints[1] = tmpx*sint + tmpy*cost + origin.y;
        tmpx = anchor.vertices[0] - origin.x;
        tmpy = anchor.vertices[1] - origin.y;
        anchor.vertices[0] = tmpx*cost - tmpy*sint + origin.x;
        anchor.vertices[1] = tmpx*sint + tmpy*cost + origin.y;
        if(anchor2!=null){
            tmpx = controlPoints[2] - origin.x;
            tmpy = controlPoints[3] - origin.y;
            controlPoints[2] = tmpx*cost - tmpy*sint + origin.x;
            controlPoints[3] = tmpx*sint + tmpy*cost + origin.y;
            tmpx = anchor2.vertices[0] - origin.x;
            tmpy = anchor2.vertices[1] - origin.y;
            anchor2.vertices[0] = tmpx*cost - tmpy*sint + origin.x;
            anchor2.vertices[1] = tmpx*sint + tmpy*cost + origin.y;
        }
    }
    protected void setSrcAndDest(float srcx, float srcy, float destx, float desty){
        dirty = true;
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        slope = (desty - srcy) / (destx - srcx);
        if(curveType == LINE_STRAIGHT)
            return;
        if(curveType == LINE_QUADRIC_CURVE){
            if(Math.abs(slope)<=1)
                setQuadraticBezierCurveVertices((srcx + destx)/2.0f,(srcy + desty)/2.0f+0.1f);
            else
                setQuadraticBezierCurveVertices((srcx + destx)/2.0f+0.1f,(srcy + desty)/2.0f);
            anchor.setPosition(controlPoints[0], controlPoints[1]);
        }
        if(curveType == LINE_CUBIC_CURVE){
            if(Math.abs(slope)<=1){
                float tmpx = (destx-srcx)/3.0f;
                float tmpy = (srcy + desty)/2.0f;
                setCubicBezierCurveVertices(tmpx+srcx, tmpy+0.1f, tmpx*2+srcx, tmpy+0.1f);
            }
            else{
                float tmpx = (destx+srcx)/2.0f;
                float tmpy = (desty-srcy)/3.0f;
                setCubicBezierCurveVertices(tmpx+0.1f, tmpy+srcy, tmpx+0.1f, 2*tmpy+srcy);
            }

            anchor.setPosition(controlPoints[0], controlPoints[1]);
            anchor2.setPosition(controlPoints[2], controlPoints[3]);
        }
    }

}
