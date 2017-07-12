package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
import main.java.org.cytoscape.pokemeow.internal.line.pmAggLineVisual;
/**
 * Created by ZhangMenghe on 2017/7/6.
 */

public class drawAggLineDemo extends Demo {
    private pmShaderParams gshaderParam;
    private int program;
    private pmLineVisual[] lineList;
    private pmLineFactory factory;
    private Vector4[] colorList = {new Vector4(1.0f, 0.5f, 0.3f, 1.0f),
            new Vector4(0.2f, 0.5f, 0.5f, 1.0f),
            new Vector4(0.3f, 1.0f, 0.2f,1.0f)};
    private pmAggLineVisual aggLine;
    public void create(GL4 gl4){
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/arrow.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        aggLine = new pmAggLineVisual(gl4);
    }
    @Override
    public void render(GL4 gl4) {
        gl4.glUseProgram(program);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        aggLine.drawAggLine(gl4);
    }
    public void reSetMatrix(boolean viewChanged){
    }
    @Override
    public void dispose(GL4 gl4) {
    }
    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
