package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class drawArrowDemo extends Demo {
    private pmBasicArrowShape[] arrowList;
    private pmArrowShapeFactory factory;
    private Vector4[] colorList = {new Vector4(1.0f, 0.5f, 0.3f, 1.0f),
    new Vector4(0.2f, 0.5f, 0.5f, 1.0f),
    new Vector4(0.3f, 1.0f, 0.2f,1.0f)};

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        numOfItems = 12;
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmArrowShapeFactory(gl4);
        arrowList = new pmBasicArrowShape[numOfItems];

        int n = 0;
        for(Byte i=0;i<numOfItems;i++)
            arrowList[n++] = factory.createArrow(i);
//        arrowList[0].setOrigin(new Vector3(1.0f,1.0f,.0f));
        for(int x=0;x<4;x++){
            for(int y=0;y<3;y++){
                float cx = -0.5f + y*0.5f;
                float cy = -0.6f + x*0.3f;
                int idx = 3*x+y;
                arrowList[idx].setOrigin(new Vector3(cx, cy, .0f));
                arrowList[idx].setColor(colorList[y]);
                arrowList[idx].setScale(0.5f);
            }
        }
        arrowList[0].setRotation(3.14f);
        //arrowList[2].setZorder(gl4, 1);
    }
    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        factory.drawArrowList(gl4, arrowList, gshaderParam);
//        factory.drawArrow(gl4, arrowList[0], gshaderParam);
    }

    public void reSetMatrix(boolean viewChanged){
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for(pmBasicArrowShape arrow : arrowList)
            arrow.dispose(gl4);
    }
}
