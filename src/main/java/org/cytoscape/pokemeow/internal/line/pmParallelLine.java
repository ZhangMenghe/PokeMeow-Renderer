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
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
        plineList[0].updateMatrix(true);
        plineList[1].updateMatrix(true);
        dirty = true;
    }

    public void setRotation(float radians){
        rotMatrix = Matrix4.rotationZ(radians);
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
        plineList[0].modelMatrix = Matrix4.mult(modelMatrix, plineList[0].modelMatrix);
        plineList[1].modelMatrix = Matrix4.mult(modelMatrix, plineList[1].modelMatrix);
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
        plineList[1].vertices = plineList[0].vertices;
        dirty = true;
    }
}
