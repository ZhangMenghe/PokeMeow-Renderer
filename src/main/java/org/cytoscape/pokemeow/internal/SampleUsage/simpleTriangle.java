package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmTriangleNodeShape;
import java.util.ArrayList;

/**
 * Created by ZhangMenghe on 2017/6/26.
 */
public class simpleTriangle extends Demo{
    private pmShaderParams gshaderParam;
    private int program;
    private pmBasicNodeShape mtriangle;
    private pmNodeShapeFactory factory;
    @Override
    public void create(GL4 gl4) {
        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/flat.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/flat.frag"));
        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmNodeShapeFactory(gl4);
        mtriangle = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_TRIANGLE);
        mtriangle.setColor(gl4, new Vector4(1.0f,.0f,.0f,1.0f));

    }
    @Override
    public void render(GL4 gl4) {
        gl4.glUseProgram(program);
        factory.drawNode(gl4,mtriangle,gshaderParam);
    }
    public void reSetMatrix(){
        mtriangle.setViewMattrix(viewMatrix);
    }
    @Override
    public void dispose(GL4 gl4) {
        mtriangle.gsthForDraw.dispose(gl4);
    }
    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
