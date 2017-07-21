package main.java.org.cytoscape.pokemeow.internal.line;

import com.jogamp.opengl.GL4;

/**
 * Created by ZhangMenghe on 2017/7/10.
 */
public class pmPatternLineBasic extends pmLineVisual {
    protected int pointsPerPattern;
    protected float shrink;

    public pmPatternLineBasic(){
        super();
        connectMethod = CONNECT_SEGMENTS;
    }
    public pmPatternLineBasic(GL4 gl4, float srcx, float srcy, float destx, float desty, Byte type){
        super(gl4, srcx, srcy, destx, desty, type);
        connectMethod = CONNECT_SEGMENTS;
    }

    protected void initVertices(GL4 gl4, float []singlePattern){
        float rlen = Math.abs(srcPos.x-destPos.x) + Math.abs(srcPos.y-destPos.y);
        shrink = 1.0f/numOfPatterns;
        numOfPatterns = (int)(rlen * numOfPatterns);
        numOfVertices = 3*pointsPerPattern*numOfPatterns;
        vertices = new float[numOfVertices];
        int base = 3*pointsPerPattern;
        float k = (srcPos.y-destPos.y) / (srcPos.x-destPos.x);
        double theta = Math.atan(k);
        float cost = (float)Math.cos(theta);
        float sint = (float)Math.sin(theta);

        for(int i=0;i<pointsPerPattern;i++){
            float tmpx = singlePattern[3*i]*cost-singlePattern[3*i+1]*sint;
            float tmpy = singlePattern[3*i]*sint+singlePattern[3*i+1]*cost;
            singlePattern[3*i] = tmpx;
            singlePattern[3*i+1] = tmpy;
        }
        for(int j=0;j<pointsPerPattern;j++){
            vertices[3*j] = singlePattern[3*j] * shrink+srcPos.x;
            vertices[3*j+1] = singlePattern[3*j +1] * shrink+srcPos.y;
            vertices[3*j+2] = zorder;
        }
        float lastx, lasty;
        for(int i=1;i<numOfPatterns;i++){
            for(int j=0;j<pointsPerPattern;j++) {
                lastx = vertices[base * (i-1) +3*j];
                lasty = vertices[base * (i-1) +3*j+1];
                vertices[base * i + 3*j] =  lastx + shrink;
                vertices[base * i + 3*j+1] = lasty + shrink * k;
                vertices[base * i + 3*j+2] = zorder;
            }
        }
        initLineVisual(gl4, vertices);
//        setRotation(3.14f/4);
//        setRotation((float)Math.atan((srcPos.y-destPos.y) / (srcPos.x-destPos.x)));

    }
}
