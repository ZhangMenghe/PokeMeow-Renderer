package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GLAutoDrawable;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

/**
 * Created by ZhangMenghe on 2017/6/26.
 */
public class simpleTriangleDemo extends Demo{
    private pmBasicNodeShape mtriangle;
    private pmNodeShapeFactory factory;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/flat.vert"),
                null,null,null,
                Demo.class.getResource("shader/flat.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmNodeShapeFactory(gl4);
        mtriangle = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_RECTANGLE);
        mtriangle.setColor(new Vector4(1.0f,.0f,.0f,1.0f),
                            new Vector4(.0f,.0f,1.0f,1.0f),(byte)3);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        factory.drawNode(gl4,mtriangle,gshaderParam);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        mtriangle.gsthForDraw.dispose(gl4);
    }

    @Override
    public void reSetMatrix(boolean viewChanged){
        mtriangle.setViewMatrix(viewMatrix);
    }
}
