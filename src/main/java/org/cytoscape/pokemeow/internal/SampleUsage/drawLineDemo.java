package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
/**
 * Created by ZhangMenghe on 2017/7/6.
 */

public class drawLineDemo extends Demo {
    private pmLineVisual[] lineList;
    private pmLineFactory factory;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        numOfItems = 13;
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/arrow.vert"),
                null,null,null,
                Demo.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmLineFactory(gl4);
        lineList = new pmLineVisual[numOfItems];

        int n = 0;
        for(Byte i=0;i<numOfItems;i++)
            lineList[n++] = factory.createLine(i, -1,-1.0f,1.0f,1.0f, pmLineVisual.LINE_STRAIGHT);
        for(n=0; n<numOfItems; n++){
            float cy = -0.9f + 0.1f*n;
            lineList[n].setOrigin(new Vector3(.0f, cy, .0f));
        }
//        lineList[0] = factory.createLine(pmLineFactory.LINE_SEPARATE_ARROW, -1,.0f,1.0f,.0f, pmLineVisual.LINE_STRAIGHT);
//        lineList[0].setOrigin(new Vector3(.0f, 1.0f, .0f));
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        factory.drawLineList(gl4, lineList, gshaderParam);
    }

    public void reSetMatrix(boolean viewChanged){
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for(pmLineVisual line : lineList)
            line.dispose(gl4);
    }

}
