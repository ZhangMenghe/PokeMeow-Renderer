package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.*;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmSthForDraw;

import java.util.Arrays;

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

    public Vector4 color = new Vector4(.0f,.0f,.0f,1.0f);
    public Vector4 gradColor;
    public byte gradColorBorderType = -1;

    public int numOfVertices;
    public float zorder = .0f;
    public boolean useTexture = false;
    public boolean visible = true;
    public float[] vertices;
    protected float xMin, xMax, yMin, yMax;
    protected float xMinOri, xMaxOri, yMinOri, yMaxOri;
    public boolean dirty = false;
    public int bufferByteOffset = 0;
    public int bufferVerticeOffset = 0;

    public byte GRAD_UP_DOWN = 0;
    public byte GRAD_LEFT_RIGHT = 1;
    public byte GRAD_DIAGONAL_LEFT = 2;
    public byte GRAD_DIAGONAL_RIGHT = 3;

    public pmBasicNodeShape(){
        origin = new Vector3(.0f,.0f, zorder);
        scale = new Vector3(1.0f,1.0f,1.0f);
        rotMatrix = Matrix4.identity();
        modelMatrix = Matrix4.identity();
        viewMatrix = Matrix4.identity();
        gradColor = color;
        gsthForDraw = new pmSthForDraw();
    }

    public void setScale(Vector3 new_scale){
        if(new_scale.x == 1 && new_scale.y == 1)
            return;
        if(new_scale.x != 1)
            scale.x *= new_scale.x;
        if(new_scale.y != 1)
            scale.y *= new_scale.y;
        updateMatrix();
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
        updateMatrix();
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        updateMatrix();
    }
    public void setOrigin(Vector2 new_origin){
        origin.x = new_origin.x;
        origin.y = new_origin.y;
        updateMatrix();
    }
    public void setRotation(float radians){
        rotMatrix = Matrix4.rotationZ(radians);
        updateMatrix();
    }

    private void updateMatrix(){
        modelMatrix = Matrix4.mult(rotMatrix, Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(Matrix4.translation(origin), modelMatrix);
        float r11 = modelMatrix.e11;float r21 = modelMatrix.e21;
        float r12 = modelMatrix.e12;float r22 = modelMatrix.e22;
        float r14 = modelMatrix.e14;float r24 = modelMatrix.e24;

        float[]arr1 = {r11*xMinOri+r12*yMinOri+r14, r11*xMinOri+r12*yMaxOri+r14, r11*xMaxOri+r12*yMinOri+r14, r11*xMaxOri+r12*yMaxOri+r14};
        float[]arr2 = {r21*xMinOri+r22*yMinOri+r24, r21*xMaxOri+r22*yMaxOri+r24, r21*xMinOri+r22*yMinOri+r24, r21*xMaxOri+r22*yMaxOri+r24};
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        xMin = arr1[0]; xMax = arr1[3];
        yMin = arr2[0]; yMax = arr2[3];
    }

    public void setViewMatrix(Matrix4 new_viewMatrix){
        viewMatrix = new_viewMatrix;
    }

    public void setColor(Vector4 new_color){
        color = new_color;
        gradColor = new_color;
    }
    public void setColor(Vector4 color1, Vector4 color2, byte type){
        color = color1;
        gradColor = color2;
        gradColorBorderType = type;
    }

    public void setZorder(float new_z){
        zorder = new_z;
        for(int i=2;i<vertices.length;i+=3)
            vertices[i] = new_z;
        dirty = true;
    }
    public int[] setBufferOffset(int bufferOffset, int boundBuffer){
        int[] offset = {bufferOffset, boundBuffer};
        bufferByteOffset = bufferOffset;
        bufferVerticeOffset = bufferOffset/12;
        offset[0] += numOfVertices *12;
        if(offset[0]>boundBuffer)
            offset[1]  = -1;
        return offset;
    }
    public boolean isOutsideBoundingBox(float posx, float posy){
        return (posx<xMin || posx>xMax || posy<yMin || posy>yMax);
    }
    public boolean isHit(float posx, float posy){
        int nCross = 0;
        float currPosX, currPosY,lastPosX,lastPosY;
        Vector4 tmp = new Vector4(vertices[0], vertices[1], .0f, 1.0f);
        tmp  = Vector4.matrixMult(modelMatrix, tmp);
        float firstX = tmp.x;
        float firstY = tmp.y;
        currPosX = firstX;
        currPosY = firstY;
        for(int i=1; i<=numOfVertices; i++) {
            lastPosX = currPosX;
            lastPosY = currPosY;
            if(i==numOfVertices){
                currPosX = firstX;
                currPosY = firstY;
            }
            else{
                tmp = new Vector4(vertices[3 * i], vertices[3 * i + 1], .0f, 1.0f);
                tmp  = Vector4.matrixMult(modelMatrix, tmp);
                currPosX = tmp.x;
                currPosY = tmp.y;
            }

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
        return (nCross%2 == 1);
    }
}
