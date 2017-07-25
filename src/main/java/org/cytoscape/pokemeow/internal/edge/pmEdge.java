package main.java.org.cytoscape.pokemeow.internal.edge;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

/**
 * Created by ZhangMenghe on 2017/7/25.
 */
public class pmEdge {
    private pmLineVisual _line;
    private pmBasicArrowShape _srcArrow;
    private pmBasicArrowShape _destArrow;
    private pmLineFactory lineFactory;
    private pmArrowShapeFactory arrowFactory;
    public Byte curveType;
    public pmEdge(GL4 gl4, Byte lineType, Byte curveType, Byte srcArrowType, Byte destArrowType,
                  float srcx, float srcy, float destx, float desty){

    }
    public pmEdge(GL4 gl4, pmLineVisual line, pmBasicArrowShape srcArrow, pmBasicArrowShape destArrow){
        _line = line;
        curveType = line.curveType;
        _srcArrow = srcArrow;
        _destArrow = destArrow;
        lineFactory = new pmLineFactory(gl4);
        if(srcArrow==null && destArrow==null)
            return;

        arrowFactory = new pmArrowShapeFactory(gl4);
        double thetasrc, thetadest;
        if(line.curveType == pmLineVisual.LINE_STRAIGHT){
            thetasrc = Math.atan(line.slope);
            if(line.slope<0)
                thetasrc -= 3.14f;
            thetadest = thetasrc;
        }
        else{
            float k = (line.srcPos.y - line.controlPoints[1]) / (line.srcPos.x - line.controlPoints[0]);
            thetasrc = Math.atan(k);
            float k2 = (line.controlPoints[1]-line.destPos.y) / (line.controlPoints[0]-line.destPos.x);
            thetadest= Math.atan(k2);
//            if(Math.abs(k2)>=1)
//                thetadest += 3.14f;
        }


        if(srcArrow != null){
            srcArrow.setOrigin(new Vector3(line.srcPos.x, line.srcPos.y,line.zorder));
            srcArrow.setRotation((float) thetasrc-3.14f);
            srcArrow.setScale(0.1f);
        }

        if(destArrow != null){
            destArrow.setOrigin(new Vector3(line.destPos.x, line.destPos.y,line.zorder));
            destArrow.setScale(0.1f);
            destArrow.setRotation((float) thetadest);
        }
    }
    public void draw(GL4 gl4, pmShaderParams gshaderParam){
        if(_srcArrow != null)
            arrowFactory.drawArrow(gl4, _srcArrow, gshaderParam);
        if(_destArrow != null)
            arrowFactory.drawArrow(gl4, _destArrow, gshaderParam);
        lineFactory.drawLine(gl4, _line, gshaderParam);
    }
    public void setControlPoints(float nctrx, float nctry, int anchorID){
        _line.setControlPoints(nctrx, nctry, anchorID);
        double theta;
        if(_srcArrow!=null){
            float k = (_line.srcPos.y - _line.controlPoints[1]) / (_line.srcPos.x - _line.controlPoints[0]);
            theta = Math.atan(k);
            _srcArrow.setRotation((float) theta-3.14f);
        }
        if(_destArrow!=null){
            float k2 = (_line.controlPoints[1]-_line.destPos.y) / (_line.controlPoints[0]-_line.destPos.x);
            theta = Math.atan(k2);
//            if(Math.abs(k2)>=100)
//                theta += 3.14f;
            _destArrow.setRotation((float) theta);
        }
    }
    public boolean isAnchorHit(float posx, float posy, int anchorID){
        if(anchorID == 1)
            return _line.anchor.isHit(posx,posy);
        else
            return  ( _line.anchor2!=null && _line.anchor2.isHit(posx,posy));
    }
}
