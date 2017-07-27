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

    public void setOrigin(Vector3 new_origin){
        float gapx = new_origin.x-origin.x;
        float gapy = new_origin.y-origin.y;
        plineList[0].setOrigin(gapx, gapy);
        plineList[1].setOrigin(gapx, gapy);
        origin = new_origin;
        dirty = true;
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
        dirty = true;
    }
}
