package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmParallelLine extends pmLineVisual {
    public pmParallelLine(GL4 gl4, pmLineVisual line, boolean initBuffer){
        super();
        hitThreshold = 3.0f;
        curveType = line.curveType;
        srcPos.x = line.srcPos.x; srcPos.y = line.srcPos.y;
        destPos.x = line.destPos.x; destPos.y = line.destPos.y;
        slope = line.slope;
        if (Math.abs(slope) <= 1)
            line.setSrcAndDest(srcPos.x, srcPos.y-0.01f, destPos.x,destPos.y-0.01f);
        else
            line.setSrcAndDest(srcPos.x - 0.01f, srcPos.y, destPos.x - 0.01f, destPos.y);
        connectMethod = CONNECT_PARALLEL;
        if(initBuffer)
            initLineVisual(gl4, line);
        else{
            plineList = new pmLineVisual[2];
            plineList[0] = line;
            plineList[1] = getCloneLine(line);
        }

        if(slope>1 || slope<-1)
            plineList[1].modelMatrix.e14 += 0.02f;
        else
            plineList[1].modelMatrix.e24 += 0.02f;

        if(curveType == LINE_QUADRIC_CURVE){
            controlPoints = line.controlPoints;
            SynchronizeLine();
            if(initBuffer)
                anchor = new pmAnchor(gl4, controlPoints[0], controlPoints[1]);
            else
                anchor = new pmAnchor(controlPoints[0], controlPoints[1]);
        }
        if(curveType == LINE_CUBIC_CURVE){
            controlPoints = line.controlPoints;
            SynchronizeLine();
            if(initBuffer){
                anchor = new pmAnchor(gl4, controlPoints[0], controlPoints[1]);
                anchor2 = new pmAnchor(gl4, controlPoints[2], controlPoints[3]);
            }
            else{
                anchor = new pmAnchor(controlPoints[0], controlPoints[1]);
                anchor2 = new pmAnchor(controlPoints[2], controlPoints[3]);
            }
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
    public void setOrigin(Vector2 new_origin){
        float gapx = new_origin.x - origin.x;
        float gapy = new_origin.y- origin.y;
        origin.x = new_origin.x; origin.y = new_origin.y;
         plineList[0].setOrigin(gapx, gapy);
        plineList[1].setOrigin(gapx, gapy);
    }
    public void setOrigin(float gapx, float gapy){
        int a = 0;
//        origin.x+=gapx;
//        origin.y+=gapy;
//        if( plineList[0].afterSetCurve){
//            plineList[0].setOrigin(new Vector2(gapx, gapy));
//            plineList[1].setOrigin(new Vector2(gapx, gapy));
//            plineList[0].afterSetCurve = false;
//            plineList[1].afterSetCurve = false;
//            afterSetCurve = false;
//        }
//        else{
//            plineList[0].setOrigin(gapx, gapy);
//            plineList[1].setOrigin(gapx, gapy);
//        }

    }
    public void setRotation(float radians){
        plineList[0].setRotation(radians);
        plineList[1].setRotation(radians);
    }

    public void setColor(Vector4 new_color){
        plineList[0].color = new_color;
        plineList[1].color = new_color;
    }

    public void setControlPoints(float nctrx, float nctry, int anchorID){
        if(anchorID == 1){
            plineList[0].controlPoints[0] = nctrx; plineList[0].controlPoints[1] = nctry;
            anchor.setPosition(nctrx, nctry);
        }
        else{
            plineList[0].controlPoints[2] = nctrx; plineList[0].controlPoints[3] = nctry;
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
        origin.x = (srcx+destx)/2.0f; origin.y = (srcy+desty)/2.0f;
        if(curveType == LINE_STRAIGHT) {
            if (Math.abs(slope) <= 1) {
                plineList[0].setSrcAndDest(srcx, srcy - 0.01f, destx, desty - 0.01f);
                plineList[1].setSrcAndDest(srcx, srcy + 0.01f, destx, desty + 0.01f);
            } else {
                plineList[0].setSrcAndDest(srcx - 0.01f, srcy, destx - 0.01f, desty);
                plineList[1].setSrcAndDest(srcx + 0.01f, srcy, destx + 0.01f, desty);
            }
            plineList[0]._curveOffset.x = origin.x;
            plineList[0]._curveOffset.y = origin.y;
            return;
        }

        dirty = true;
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
                    plineList[0].setQuadraticBezierCurveVertices();
                else
                    plineList[0].setQuadraticBezierCurveVertices();
                anchor.setPosition(controlPoints[0], controlPoints[1]);
            }
            else{
                if(Math.abs(slope)<=1){
                    float tmpx = (destx-srcx)/3.0f;
                    float tmpy = (srcy + desty)/2.0f;
                    plineList[0].setCubicBezierCurveVertices();
                }
                else{
                    float tmpx = (destx+srcx)/2.0f;
                    float tmpy = (desty-srcy)/3.0f;
                    plineList[0].setCubicBezierCurveVertices();
                }

                anchor.setPosition(controlPoints[0], controlPoints[1]);
                anchor2.setPosition(controlPoints[2], controlPoints[3]);
            }
            SynchronizeLine();
            plineList[1].modelMatrix = Matrix4.identity();
            //plineList[1].afterSetCurve = true;
            plineList[1]._curveOffset.x = plineList[0]._curveOffset.x;
            plineList[1]._curveOffset.y = plineList[0]._curveOffset.y;
    }
}
