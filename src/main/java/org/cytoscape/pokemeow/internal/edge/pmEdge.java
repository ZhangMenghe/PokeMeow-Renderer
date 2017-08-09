package main.java.org.cytoscape.pokemeow.internal.edge;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmEdgeBuffer;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;

/**
 * Created by ZhangMenghe on 2017/7/25.
 */
public class pmEdge {
    public pmLineVisual _line;
    public pmBasicArrowShape _srcArrow;
    public pmBasicArrowShape _destArrow;
    private pmLineFactory lineFactory;
    private pmArrowShapeFactory arrowFactory;
    public Byte curveType;
    private float xMin,xMax,yMin,yMax;

    //No arrow
    public pmEdge(GL4 gl4, Byte lineType, Byte mcurveType,
                  float srcx, float srcy, float destx, float desty, boolean initBuffer){
        commonInitialForEdge(gl4,lineType,mcurveType,srcx,srcy,destx,desty,initBuffer);

    }
    public pmEdge(GL4 gl4, Byte lineType, Byte mcurveType,Byte destArrowType,
                  float srcx, float srcy, float destx, float desty, boolean initBuffer){
        commonInitialForEdge(gl4,lineType,mcurveType,srcx,srcy,destx,desty,initBuffer);
        arrowFactory = new pmArrowShapeFactory(gl4);
        _destArrow = arrowFactory.createArrow(destArrowType, initBuffer);
        setArrowPosAndRot();
    }
    public pmEdge(GL4 gl4, Byte lineType, Byte mcurveType, Byte srcArrowType, Byte destArrowType,
                  float srcx, float srcy, float destx, float desty, boolean initBuffer){
        commonInitialForEdge(gl4,lineType,mcurveType,srcx,srcy,destx,desty,initBuffer);
        arrowFactory = new pmArrowShapeFactory(gl4);
        _srcArrow = arrowFactory.createArrow(srcArrowType, initBuffer);
        _destArrow = arrowFactory.createArrow(destArrowType, initBuffer);
        setArrowPosAndRot();
    }

    public pmEdge(GL4 gl4, pmLineVisual line, pmBasicArrowShape srcArrow, pmBasicArrowShape destArrow){
        _line = line;
        curveType = line.curveType;
        xMin = Math.min(line.srcPos.x, line.destPos.x)-0.01f;
        xMax = Math.max(line.srcPos.x, line.destPos.x)+0.01f;
        yMin = Math.min(line.srcPos.y, line.destPos.y)-0.01f;
        yMax = Math.max(line.srcPos.y, line.destPos.y)+0.01f;
        _srcArrow = srcArrow;
        _destArrow = destArrow;
        lineFactory = new pmLineFactory(gl4);

        if(destArrow == null)
            return;

        arrowFactory = new pmArrowShapeFactory(gl4);
        setArrowPosAndRot();

    }
    private void commonInitialForEdge(GL4 gl4, Byte lineType, Byte mcurveType,
                                      float srcx, float srcy, float destx, float desty, boolean initBuffer){
        lineFactory = new pmLineFactory(gl4);
        _line = lineFactory.createLine(lineType, srcx, srcy, destx, desty, mcurveType, initBuffer);
        curveType = mcurveType;
        xMin = Math.min(srcx, destx)-0.01f; xMax = Math.max(srcx, destx)+0.01f;
        yMin = Math.min(srcy, desty)-0.01f; yMax = Math.max(srcy, desty)+0.01f;
    }
    private void setArrowPosAndRot(){
        if (_destArrow != null) {
            _destArrow.setScale(0.2f);
            _destArrow.setOrigin(new Vector3(_line.destPos.x, _line.destPos.y, _line.zorder));
        }
        if (_srcArrow != null) {
            _srcArrow.setScale(0.2f);
            _srcArrow.setOrigin(new Vector3(_line.srcPos.x, _line.srcPos.y, _line.zorder));
        }
        setArrowRotation();
    }

    private void setArrowRotation(){
        double thetasrc, thetadest;
        if(_line.curveType == pmLineVisual.LINE_STRAIGHT){
            thetasrc = Math.atan(_line.slope);
            if(_line.destPos.x<_line.srcPos.x)
                thetasrc -= 3.14f;
            thetadest = thetasrc;
        }
        else if(_line.curveType == pmLineVisual.LINE_QUADRIC_CURVE){
            float k = (_line.srcPos.y - _line.controlPoints[1]) / (_line.srcPos.x - _line.controlPoints[0]);
            thetasrc = Math.atan(k);

            float k2 = (_line.controlPoints[1] - _line.destPos.y) / (_line.controlPoints[0] - _line.destPos.x);
            thetadest = Math.atan(k2);
            if (Math.abs(_line.slope) > 1) {
                if (_line.controlPoints[0] > _line.destPos.x)
                    thetadest -= 3.14f;
                if(_line.controlPoints[0] < _line.srcPos.x)
                    thetasrc-=3.14f;
            }
            else{
                if(_line.destPos.x < _line.srcPos.x){
                    thetadest -= 3.14f;
                    thetasrc -= 3.14f;
                }
            }
        }
        else {
            float k = (_line.srcPos.y - _line.controlPoints[1]) / (_line.srcPos.x - _line.controlPoints[0]);
            thetasrc = Math.atan(k);

            float k2 = (_line.controlPoints[3] - _line.destPos.y) / (_line.controlPoints[2] - _line.destPos.x);
            thetadest = Math.atan(k2);
            if (Math.abs(_line.slope) > 1) {
                if(_line.controlPoints[0]<_line.srcPos.x)
                    thetasrc-=3.14;
                if(_line.controlPoints[2]>_line.destPos.x)
                    thetadest-=3.14;
            }
            else{
                if(_line.destPos.x < _line.srcPos.x){
                    thetadest -= 3.14f;
                    thetasrc -= 3.14f;
                }
            }
        }

        if (_destArrow != null)
            _destArrow.setRotation((float) thetadest);
        if (_srcArrow != null)
            _srcArrow.setRotation((float) thetasrc - 3.14f);
    }

    public void draw(GL4 gl4, pmShaderParams gshaderParam){
        if(_destArrow != null){
            arrowFactory.drawArrow(gl4, _destArrow, gshaderParam);
        }
        if(_srcArrow != null)
            arrowFactory.drawArrow(gl4, _srcArrow, gshaderParam);
        lineFactory.drawLine(gl4, _line, gshaderParam);
    }

    public void draw(GL4 gl4, pmShaderParams gshaderParam, pmEdgeBuffer buffer){
        if(_destArrow != null){
            arrowFactory.drawArrow(gl4, _destArrow, gshaderParam, buffer);
        }
        if(_srcArrow != null)
            arrowFactory.drawArrow(gl4, _srcArrow, gshaderParam, buffer);
        lineFactory.drawLine(gl4, _line, gshaderParam, buffer);
    }

    public void setControlPoints(float nctrx, float nctry, int anchorID){
        _line.setControlPoints(nctrx, nctry, anchorID);
        setArrowRotation();
    }

    public boolean isAnchorHit(float posx, float posy, int anchorID){
        if(anchorID == 1)
            return _line.anchor.isHit(posx,posy);
        else
            return  ( _line.anchor2!=null && _line.anchor2.isHit(posx,posy));
    }

    public void dispose(GL4 gl4){
        _line.dispose(gl4);
        if(_srcArrow!=null)
            _srcArrow.dispose(gl4);
        if(_destArrow!=null)
            _destArrow.dispose(gl4);
    }

    public boolean isHit(float posx, float posy){
        if(_line.curveType == pmLineVisual.LINE_STRAIGHT)
            return isHitStraightLine(posx, posy);
        xMin = Math.min(xMin, _line.controlPoints[0]); xMax = Math.max(xMax,  _line.controlPoints[0]);
        yMin = Math.min(yMin, _line.controlPoints[1]); yMax = Math.max(yMax,  _line.controlPoints[1]);
        if(_line.curveType == pmLineVisual.LINE_CUBIC_CURVE){
            xMin = Math.min(xMin, _line.controlPoints[2]); xMax = Math.max(xMax,  _line.controlPoints[2]);
            yMin = Math.min(yMin, _line.controlPoints[3]); yMax = Math.max(yMax,  _line.controlPoints[3]);
        }
        if(posx<xMin || posx>xMax || posy<yMin || posy>yMax)
            return false;
        if(_line.slope>0){
            for(int i=0; i<_line.numOfVertices-1; i++){
                if(_line.vertices[3*i+1]<(posy-0.02f) || _line.vertices[3*i] > (posx+0.02f))
                    continue;
                float deltay = _line.vertices[3*i+1]-posy;
                float deltax = _line.vertices[3*i]-posx;
                float length =deltay*deltay + deltax*deltax;
                if(length <= _line.hitThreshold * 0.0002f)
                    return true;
            }
        }
        else{
            for(int i=0; i<_line.numOfVertices-1; i++){
                if(_line.vertices[3*i]<(posx-0.02f) || _line.vertices[3*i+1] > (posy+0.02f))
                    continue;
                float deltay = _line.vertices[3*i+1]-posy;
                float deltax = _line.vertices[3*i]-posx;
                float length =deltay*deltay + deltax*deltax;
                if(length <= 0.0002f)
                    return true;

            }
        }
        return false;
    }

    private boolean isHitStraightLine(float posx, float posy){
        if(posx<xMin || posx>xMax || posy<yMin || posy>yMax)
            return false;

        float tmpx = posx-(xMin+xMax)/2;
        float tmpy = posy-(yMin+yMax)/2;
        float length = Math.abs(_line.slope*tmpx - tmpy) / (float)Math.sqrt(_line.slope * _line.slope +1);
        if(length <= 0.01f)
            return true;
        else
            return false;
    }

    public void setColor(Vector4 ncolor){
        _line.setColor(ncolor);
        if(_srcArrow!=null)
            _srcArrow.setColor(ncolor);
        if(_destArrow!=null)
            _destArrow.setColor(ncolor);
    }

    public void setRotation(float radians){
        _line.setRotation(radians);
        if(_srcArrow!=null)
            _srcArrow.setOrigin(new Vector3(_line.srcPos.x, _line.srcPos.y, _line.zorder));
        if(_destArrow!=null)
            _destArrow.setOrigin(new Vector3(_line.destPos.x, _line.destPos.y, _line.zorder));
        setArrowRotation();
    }

    public void setOrigin(Vector2 new_origin) {
        float deltax = new_origin.x - _line.origin.x;
        float deltay = new_origin.y - _line.origin.y;

        _line.setOrigin(new_origin);
        _line.srcPos.x +=deltax;
        _line.srcPos.y +=deltay;
        _line.destPos.x +=deltax;
        _line.destPos.y +=deltay;

        if (curveType != pmLineVisual.LINE_STRAIGHT) {
            _line.controlPoints[0]+=deltax;_line.controlPoints[1]+=deltay;
            _line.anchor.setPosition(_line.controlPoints[0], _line.controlPoints[1]);
        }
        if (curveType == pmLineVisual.LINE_CUBIC_CURVE){
            _line.controlPoints[2]+=deltax;_line.controlPoints[3]+=deltay;
            _line.anchor2.setPosition(_line.controlPoints[2], _line.controlPoints[3]);
        }

        if (_srcArrow != null) {
            _srcArrow.setOrigin(new Vector3(_line.srcPos.x, _line.srcPos.y, _line.zorder));

            if (_destArrow != null)
                _destArrow.setOrigin(new Vector3(_line.destPos.x, _line.destPos.y, _line.zorder));
        }
    }

    public int[] setBufferOffset(int bufferOffset, int indexOffset, int boundBuffer, int boundBufferIdx){
        int[] offset = {bufferOffset,indexOffset,boundBuffer, boundBufferIdx};
        if(_line.plineList != null) {//parallal line
            _line.plineList[0].bufferByteOffset = bufferOffset;
            _line.plineList[0].bufferVerticeOffset  = bufferOffset/12;
            _line.plineList[1].bufferByteOffset = bufferOffset +  _line.plineList[0].numOfVertices * 12;
            _line.plineList[1].bufferVerticeOffset  = _line.plineList[1].bufferByteOffset/12;
            offset[0] += _line.plineList[0].numOfVertices *24;
        }
        else if(_line.patternList != null){
            int n=0;
            int bufferVertices = bufferOffset/12;
            for(pmBasicArrowShape arrow: _line.patternList){
                arrow.bufferByteOffset = bufferOffset + 84 * n;//7*12
                arrow.bufferVerticeOffset = bufferVertices + 7 * n;
                arrow.indexByteOffset = indexOffset + 60*n;//15*4
                n++;
            }
            offset[0] += 84*n;
            offset[1] += 60*n;
        }
        else{
            _line.bufferByteOffset = bufferOffset;
            _line.bufferVerticeOffset  = bufferOffset/12;
            offset[0] += _line.numOfVertices *12;
        }

        if(_srcArrow!=null){
            _srcArrow.bufferByteOffset = offset[0];
            _srcArrow.bufferVerticeOffset  = offset[0]/12;
            offset[0]+=_srcArrow.numOfVertices *12;
            if(_srcArrow.numOfIndices!=-1){
                _srcArrow.indexByteOffset = offset[1];
                offset[1] += _srcArrow.numOfIndices *4;
            }
        }
        if(_destArrow!=null){
            _destArrow.bufferByteOffset = offset[0];
            _destArrow.bufferVerticeOffset  = offset[0]/12;
            offset[0]+=_destArrow.numOfVertices *12;
            if(_destArrow.numOfIndices!=-1){
                _destArrow.indexByteOffset = offset[1];
                offset[1] += _destArrow.numOfIndices * 4;
            }
        }
        if(offset[0]>boundBuffer)
            offset[2]  = -1;
        if(offset[1]>boundBufferIdx)
            offset[3] = -1;
        return offset;
    }

    public void resetSrcAndDest(float srcx, float srcy, float destx, float desty){
        _line.resetSrcAndDest(srcx,srcy,destx,desty);
        if(_srcArrow != null)
            _srcArrow.setOrigin(new Vector3(srcx, srcy, _line.zorder));
        if(_destArrow != null)
            _destArrow.setOrigin(new Vector3(destx, desty, _line.zorder));
        setArrowRotation();
        xMin = Math.min(srcx, destx)-0.01f; xMax = Math.max(srcx, destx)+0.01f;
        yMin = Math.min(srcy, desty)-0.01f; yMax = Math.max(srcy, desty)+0.01f;
    }

    public void resetSrcAndDest(float posx, float posy, int srcOrdest){
        float srcx, srcy, destx, desty;
        if(srcOrdest == 1){
            srcx = posx; srcy=posy;destx =_line.destPos.x;desty =_line.destPos.y;
            if(_srcArrow != null)
                _srcArrow.setOrigin(new Vector3(srcx, srcy, _line.zorder));
        }
        else{
            srcx = _line.srcPos.x ; srcy=_line.srcPos.y ;destx =posx;desty =posy;
            if(_destArrow != null)
                _destArrow.setOrigin(new Vector3(srcx, srcy, _line.zorder));
        }
        _line.resetSrcAndDest(srcx,srcy,destx,desty);
        setArrowRotation();
        xMin = Math.min(srcx, destx)-0.01f; xMax = Math.max(srcx, destx)+0.01f;
        yMin = Math.min(srcy, desty)-0.01f; yMax = Math.max(srcy, desty)+0.01f;
    }
}
