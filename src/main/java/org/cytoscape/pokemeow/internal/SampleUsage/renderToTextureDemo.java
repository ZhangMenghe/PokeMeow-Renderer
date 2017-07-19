package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmRenderToTexture;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

/**
 * Created by ZhangMenghe on 2017/6/27.
 */
public class renderToTextureDemo extends Demo {
    private pmBasicNodeShape mtriangle;
    private pmNodeShapeFactory factory;
    private pmRenderToTexture renderer_t;

    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        program = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/flat.vert"),
                null,null,null,
                Demo.class.getResource("shader/flat.frag"));

        gshaderParam = new pmShaderParams(gl4, program);
        factory = new pmNodeShapeFactory(gl4);
        mtriangle = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_TRIANGLE);


        mtriangle.setColor(gl4, new Vector4(1.0f,.0f,.0f,1.0f));
        renderer_t = new pmRenderToTexture(gl4);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        renderer_t.RenderToTexturePrepare(gl4);

        gl4.glUseProgram(program);
        factory.drawNode(gl4,mtriangle,gshaderParam);

        renderer_t.RenderToScreen(gl4);
    }
    public void reSetMatrix(boolean viewChanged){
        mtriangle.setViewMatrix(viewMatrix);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {mtriangle.gsthForDraw.dispose(gl4);}
}
