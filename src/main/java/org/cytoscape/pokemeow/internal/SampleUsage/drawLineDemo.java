package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineVisual;
import main.java.org.cytoscape.pokemeow.internal.line.pmLineFactory;
/**
 * Created by ZhangMenghe on 2017/7/6.
 */

public class drawLineDemo extends Demo {
    private pmShaderParams gshaderParam;
    private int program;
    private pmLineVisual[] lineList;
    private pmLineFactory factory;
    private Vector4[] colorList = {new Vector4(1.0f, 0.5f, 0.3f, 1.0f),
            new Vector4(0.2f, 0.5f, 0.5f, 1.0f),
            new Vector4(0.3f, 1.0f, 0.2f,1.0f)};
    public void create(GL4 gl4){
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/arrow.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/arrow.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmLineFactory(gl4);
        lineList = new pmLineVisual[13];
//        lineList[0] = factory.createLine_GL(pmLineFactory.LINE_SOLID);

        int n = 0;
        for(Byte i=0;i<13;i++)
            lineList[n++] = factory.createLine_GL(i);
        for(n=0;n<12;n++){
            float cy = -0.9f + 0.1f*n;
            lineList[n].setOrigin(new Vector3(.0f, cy, .0f));
            if(n>5 && n<11)
                lineList[n].setScale(0.25f);
            else
                lineList[n].setScale(0.5f);
        }
        lineList[12].setScale(0.5f);
        lineList[12].setOrigin(new Vector3(0.5f, .0f, .0f));
    }
    @Override
    public void render(GL4 gl4) {
        gl4.glUseProgram(program);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
//        factory.drawLine(gl4, lineList[0], gshaderParam);
        factory.drawLineList(gl4, lineList, gshaderParam);
    }
    public void reSetMatrix(boolean viewChanged){
    }
    @Override
    public void dispose(GL4 gl4) {
        for(pmLineVisual line : lineList)
            line.dispose(gl4);
    }
    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
