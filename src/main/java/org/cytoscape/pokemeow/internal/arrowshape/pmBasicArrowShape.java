package main.java.org.cytoscape.pokemeow.internal.arrowshape;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public abstract class pmBasicArrowShape {
    public int VAO = 0;
    public int VBO = 1;
    public int EBO = 2;//index buffer, maybe no use
    public float[] vertices;
    public int [] elements;
    public int[] objects = new int[3];
    public int numOfVertices = 0;
    public int numOfIndices = -1;
    public boolean dirty = true;
    public FloatBuffer data_buff;
    public IntBuffer indice_buff;
    public Vector3 origin = new Vector3(.0f,.0f,.0f);
    public Matrix4 modelMatrix = Matrix4.identity();
    public Matrix4 rotMatrix = Matrix4.identity();
    public Vector3 scale = new Vector3(1.0f,1.0f,1.0f);
    public Vector4 color = new Vector4(0.69f, 0.88f, 0.9f,1.0f);
    public float zorder = .0f;
    public int bufferByteOffset = 0;
    public int indexByteOffset = 0;
    public int bufferVerticeOffset = 0;
    protected float xMin, xMax, yMin, yMax;
    protected float xMinOri, xMaxOri, yMinOri, yMaxOri;

    public pmBasicArrowShape(){
        modelMatrix = Matrix4.mult(Matrix4.scale((scale)), Matrix4.translation(origin));
        modelMatrix = Matrix4.mult(modelMatrix,rotMatrix);
    }

    protected void initBuffer(GL4 gl4){
        data_buff = Buffers.newDirectFloatBuffer(vertices);
        gl4.glGenVertexArrays(1,objects,VAO);
        gl4.glGenBuffers(1,objects,VBO);

        gl4.glBindVertexArray(objects[VAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl4.glBufferData(GL.GL_ARRAY_BUFFER, data_buff.capacity() * Float.BYTES, data_buff, GL.GL_DYNAMIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3*Float.BYTES, 0);
        gl4.glBindVertexArray(0);
    }

    protected void initBuffer(GL4 gl, boolean useElement){
        data_buff = Buffers.newDirectFloatBuffer(vertices);
        IntBuffer indice_buff = Buffers.newDirectIntBuffer(elements);
        gl.glGenVertexArrays(1,objects,VAO);
        gl.glGenBuffers(1,objects,VBO);
        gl.glGenBuffers(1,objects,EBO);

        gl.glBindVertexArray(objects[VAO]);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, objects[VBO]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, data_buff.capacity() * Float.BYTES,data_buff, GL.GL_DYNAMIC_DRAW);

        gl.glEnableVertexAttribArray(0);
        gl.glVertexAttribPointer(0, 3, GL.GL_FLOAT, false, 3*Float.BYTES, 0);

        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER,objects[EBO]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, indice_buff.capacity() * Integer.BYTES,indice_buff,GL.GL_DYNAMIC_DRAW);

        gl.glBindBuffer(GL.GL_ARRAY_BUFFER,0);
        gl.glBindVertexArray(0);
    }

    public void setScale(Vector2 new_scale){
        scale.x *= new_scale.x;
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
    public void setOrigin(float gapx, float gapy){
        origin.x+=gapx;
        origin.y+=gapy;
        updateMatrix();
    }

    public void setRotation(float radians){
        rotMatrix = Matrix4.rotationZ(radians);
        updateMatrix();
    }

    public void updateMatrix(){
        modelMatrix = Matrix4.mult(rotMatrix, Matrix4.scale((scale)));
        modelMatrix = Matrix4.mult(Matrix4.translation(origin), modelMatrix);

//        float r11 = modelMatrix.e11;float r21 = modelMatrix.e21;
//        float r12 = modelMatrix.e12;float r22 = modelMatrix.e22;
//        float r14 = modelMatrix.e14;float r24 = modelMatrix.e24;
//
//        float[]arr1 = {r11*xMinOri+r12*yMinOri+r14, r11*xMinOri+r12*yMaxOri+r14, r11*xMaxOri+r12*yMinOri+r14, r11*xMaxOri+r12*yMaxOri+r14};
//        float[]arr2 = {r21*xMinOri+r22*yMinOri+r24, r21*xMaxOri+r22*yMaxOri+r24, r21*xMinOri+r22*yMinOri+r24, r21*xMaxOri+r22*yMaxOri+r24};
//        Arrays.sort(arr1);
//        Arrays.sort(arr2);
//        xMin = arr1[0]; xMax = arr1[3];
//        yMin = arr2[0]; yMax = arr2[3];
//        Vector4 tmp;
//        for(int i=0;i<numOfVertices;i++){
//            tmp = new Vector4(vertices[3 * i], vertices[3 * i + 1], .0f, 1.0f);
//            tmp  = Vector4.matrixMult(modelMatrix, tmp);
//            vertices[3 * i] = tmp.x;
//            vertices[3 * i+1] = tmp.y;
//        }
//        dirty = true;
    }

    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public void setZorder(GL4 gl4, float new_z){
    }

    public void dispose(GL4 gl4) {
        gl4.glDeleteBuffers(2, objects,0);
        gl4.glDeleteVertexArrays(1, objects, VAO);
    }
//    public boolean isHit(float posx, float posy){
//        return !(posx<xMin || posx>xMax || posy<yMin || posy>yMax);
//    }
}
