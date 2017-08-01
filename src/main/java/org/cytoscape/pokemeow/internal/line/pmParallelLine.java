package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmParallelLine extends pmLineVisual {
    public pmParallelLine(GL4 gl4, pmLineVisual line){
        super();
        curveType = line.curveType;
        srcPos.x = line.srcPos.x; srcPos.y = line.srcPos.y;
        destPos.x = line.destPos.x; destPos.y = line.destPos.y;
        slope = line.slope;
        connectMethod = CONNECT_PARALLEL;
        initLineVisual(gl4, line);
        dirty = true;
        if(curveType == LINE_QUADRIC_CURVE){
            controlPoints = line.controlPoints;
            anchor = new pmAnchor(gl4, controlPoints[0], controlPoints[1]);
        }
        if(curveType == LINE_CUBIC_CURVE){
            controlPoints = line.controlPoints;
            anchor = new pmAnchor(gl4, controlPoints[0], controlPoints[1]);
            anchor2 = new pmAnchor(gl4, controlPoints[2], controlPoints[3]);
        }
    }
    public void setScale(Vector2 new_scale){
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
        plineList[0].setScale(new_scale);
        plineList[1].setScale(new_scale);
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
        plineList[0].setScale(s_scale);
        plineList[1].setScale(s_scale);
    }

//    public void setOrigin(Vector2 new_origin){
//        origin.x = new_origin.x;
//        origin.y = new_origin.y;
//
//        if(Math.abs(slope) <= 1){
//            plineList[0].setOrigin(new Vector2(new_origin.x, new_origin.y - 0.01f));
//            plineList[1].setOrigin(new Vector2(new_origin.x, new_origin.y + 0.01f));
//        }
//
//        else{
//            plineList[0].setOrigin(new Vector2(new_origin.x - 0.01f, new_origin.y));
//            plineList[1].setOrigin(new Vector2(new_origin.x + 0.01f, new_origin.y));
//        }
//
//    }
    public void setOrigin(float gapx, float gapy){
        origin.x+=gapx;
        origin.y+=gapy;
        if( plineList[0].afterSetCurve){
            plineList[0].setOrigin(new Vector2(gapx, gapy));
            plineList[1].setOrigin(new Vector2(gapx, gapy));
            plineList[0].afterSetCurve = false;
            plineList[1].afterSetCurve = false;
            afterSetCurve = false;
        }
        else{

            plineList[0].setOrigin(gapx, gapy);
            plineList[1].setOrigin(gapx, gapy);
        }

    }
    public void setRotation(float radians){
        plineList[0].setRotation(radians);
        plineList[1].setRotation(radians);
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
        plineList[0].setControlPoints(nctrx,nctry,anchorID);
        SynchronizeLine();
        dirty = true;
    }
    private void SynchronizeLine(){
        if(Math.abs(slope) <= 1){
            for(int i=0;i<plineList[0].numOfVertices;i++){
                plineList[1].vertices[3*i]=plineList[0].vertices[3*i];
                plineList[1].vertices[3*i+1]=plineList[0].vertices[3*i+1]+0.02f;
            }
        }
        else{
            for(int i=0;i<plineList[0].numOfVertices;i++){
                plineList[1].vertices[3*i]=plineList[0].vertices[3*i] +0.02f;
                plineList[1].vertices[3*i+1]=plineList[0].vertices[3*i+1];
            }
        }
        plineList[1].dirty = true;
    }
    protected void setSrcAndDest(float srcx, float srcy, float destx, float desty){
        srcPos.x = srcx; srcPos.y = srcy;
        destPos.x = destx; destPos.y = desty;
        float deltax = destx-srcx; float deltay = desty-srcy;
        slope = deltay/deltax;
        if(curveType == LINE_STRAIGHT) {
            if (Math.abs(slope) <= 1) {
                plineList[0].setSrcAndDest(srcx, srcy - 0.01f, destx, desty - 0.01f);
                plineList[1].setSrcAndDest(srcx, srcy + 0.01f, destx, desty + 0.01f);
            } else {
                plineList[0].setSrcAndDest(srcx - 0.01f, srcy, destx - 0.01f, desty);
                plineList[1].setSrcAndDest(srcx + 0.01f, srcy, destx + 0.01f, desty);
            }
            return;
        }
            dirty = true;
            origin.x = (srcx+destx)/2.0f; origin.y = (srcy+desty)/2.0f;
        if(Math.abs(slope)<=1){
            plineList[0].srcPos.x = srcx; plineList[0].srcPos.y = srcy - 0.01f;
            plineList[0].destPos.x = destx; plineList[0].destPos.y = desty- 0.01f;
        }
        else{
            plineList[0].srcPos.x = srcx- 0.01f; plineList[0].srcPos.y = srcy ;
            plineList[0].destPos.x = destx- 0.01f; plineList[0].destPos.y = desty;
        }
            deltax = destx-srcx; deltay = desty-srcy;
            plineList[0].slope = deltay/deltax;
            if(curveType == LINE_QUADRIC_CURVE){
                if(Math.abs(slope)<=1)
                    plineList[0].setQuadraticBezierCurveVertices((srcx + destx)/2.0f,(srcy + desty)/2.0f+0.1f- 0.01f);
                else
                    plineList[0].setQuadraticBezierCurveVertices((srcx + destx)/2.0f+0.1f- 0.01f,(srcy + desty)/2.0f);
                anchor.setPosition(controlPoints[0], controlPoints[1]);
            }
            else{
                if(Math.abs(slope)<=1){
                    float tmpx = (destx-srcx)/3.0f;
                    float tmpy = (srcy + desty)/2.0f;
                    plineList[0].setCubicBezierCurveVertices(tmpx+srcx, tmpy+0.1f, tmpx*2+srcx, tmpy+0.1f);
                }
                else{
                    float tmpx = (destx+srcx)/2.0f;
                    float tmpy = (desty-srcy)/3.0f;
                    plineList[0].setCubicBezierCurveVertices(tmpx+0.1f, tmpy+srcy, tmpx+0.1f, 2*tmpy+srcy);
                }

                anchor.setPosition(controlPoints[0], controlPoints[1]);
                anchor2.setPosition(controlPoints[2], controlPoints[3]);
            }
            SynchronizeLine();
            plineList[1].modelMatrix = Matrix4.identity();
            plineList[1].afterSetCurve = true;
    }
}
