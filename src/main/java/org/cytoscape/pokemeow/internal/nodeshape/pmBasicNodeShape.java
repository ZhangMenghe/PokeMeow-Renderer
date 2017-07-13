package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.*;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmSthForDraw;

/**
 * This is the basic stuff to draw nodes, you can set origin, scale and viewMatirx for a single node
 *
 * Created by ZhangMenghe on 2017/6/21.
 *
 */
public class pmBasicNodeShape{
    public pmSthForDraw gsthForDraw;//stuff to draw using gl4,VAO&VBO included
    public Vector3 origin;//origin of node
    public Matrix4 rotMatrix;
    public Vector3 scale;//scale of node
    public Matrix4 modelMatrix;//translation*scale
    public Matrix4 viewMatrix;
    public int numOfVertices;
    public int zorder = 0;
    public boolean useTexture = false;
    public float[] vertices;
    protected float xMin, xMax, yMin, yMax;

    public pmBasicNodeShape(){
        origin = new Vector3(.0f,.0f,.0f);
        scale = new Vector3(1.0f,1.0f,1.0f);
        rotMatrix = Matrix4.identity();
        modelMatrix = Matrix4.mult(Matrix4.scale((scale)),Matrix4.translation(origin));
        modelMatrix = Matrix4.mult(modelMatrix,rotMatrix);
        viewMatrix = Matrix4.identity();
        gsthForDraw = new pmSthForDraw();
    }

    public void setScale(Vector3 new_scale){
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
        scale.z *= new_scale.z;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
        scale.z *= s_scale;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
    }

    public void setRotation(float radians){
        rotMatrix = Matrix4.rotationZ(radians);
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(modelMatrix, rotMatrix);
    }

    public void setViewMatrix(Matrix4 new_viewMatrix){
        viewMatrix = new_viewMatrix;
    }
    public void setColor(GL4 gl4, float[] new_color){}
    public void setColor(GL4 gl4, Vector4 new_color){}
    public void setColor(GL4 gl4, Vector4 [] colorList){}
    public void setDefaultTexcoord(GL4 gl4){}
    public void setZorder(GL4 gl4, int new_z){}

    public boolean isHit(Vector2 pos){
        float posx = 2*(float) pos.x/commonUtil.DEMO_VIEWPORT_SIZE.x-1;
        float posy = 1.0f-(2*(float) pos.y/commonUtil.DEMO_VIEWPORT_SIZE.y);
        if(posx<xMin || posx>xMax || posy<yMin || posy>yMax)
            return false;
        int nCross = 0;
        float currPosX, currPosY;
        float lastPosX = vertices[0];
        float lastPosY = vertices[1];
        for(int i=1; i<numOfVertices; i++) {
            currPosX = vertices[7 * i];
            currPosY = vertices[7 * i + 1];

            if (lastPosY == currPosY)
                continue;
            if (posy < Math.min(lastPosY, currPosY))
                continue;
            if (posy > Math.max(lastPosY, currPosY))
                continue;
            double x = (double) (posy - lastPosY) * (double) (currPosX - lastPosX) / (double) (currPosY - lastPosY) + lastPosX;
            if (x > posx)
                nCross++;
        }
        if(nCross%2 == 1)
            return true;
        else
            return false;
    }
}
