package main.java.org.cytoscape.pokemeow.internal.nodeshape;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
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
    public Matrix4 viewMattrix;
    public int numOfVertices;
    public boolean useTexture = false;

    public pmBasicNodeShape(){
        origin = new Vector3(.0f,.0f,.0f);
        scale = new Vector3(0.5f,0.5f,0.5f);
        rotMatrix = Matrix4.identity();
        modelMatrix = Matrix4.mult(Matrix4.scale((scale)),Matrix4.translation(origin));
        modelMatrix = Matrix4.mult(modelMatrix,rotMatrix);
        viewMattrix = Matrix4.identity();
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

    public void setViewMattrix(Matrix4 new_viewMatrix){
        viewMattrix = new_viewMatrix;
    }
    public void setColor(GL4 gl4, float[] new_color){}
    public void setColor(GL4 gl4, Vector4 new_color){}
    public void setColor(GL4 gl4, Vector4 [] colorList){}
    public void setDefaultTexcoord(GL4 gl4){}
}
