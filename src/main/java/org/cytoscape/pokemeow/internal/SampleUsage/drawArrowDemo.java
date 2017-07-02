package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmBasicArrowShape;
import main.java.org.cytoscape.pokemeow.internal.arrowshape.pmArrowShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

/**
 * Created by ZhangMenghe on 2017/7/2.
 */
public class drawArrowDemo extends Demo {
    private pmShaderParams gshaderParam;
    private int program;
    private pmBasicArrowShape arrow;
    private pmArrowShapeFactory factory;

    public void create(GL4 gl4){
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/arrow.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmArrowShapeFactory(gl4);
        arrow = factory.createNode(pmArrowShapeFactory.SHAPE_ARROWHEAD);
        //arrow.setColor(gl4, new Vector4(1.0f,.0f,.0f,1.0f));
    }
    @Override
    public void render(GL4 gl4) {
        gl4.glUseProgram(program);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        factory.drawArrow(gl4, arrow, gshaderParam);
    }
    public void reSetMatrix(boolean viewChanged){
    }
    @Override
    public void dispose(GL4 gl4) {
        arrow.dispose(gl4);
    }
    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
