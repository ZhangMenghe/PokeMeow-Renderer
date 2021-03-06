package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmRenderToTextureAA;
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
    private pmRenderToTextureAA renderer_tAA;
    private boolean changed = true;
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


        mtriangle.setColor(new Vector4(1.0f,.0f,.0f,1.0f));
        renderer_t = new pmRenderToTexture(gl4);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);

        if(changed){
            renderer_t.RenderToTexturePrepare(gl4);
            gl4.glUseProgram(program);
            factory.drawNode(gl4,mtriangle,gshaderParam);
            changed = false;
        }
        renderer_t.RenderToScreen(gl4);
    }

    @Override
    public void reSetMatrix(boolean viewChanged){
        super.reSetMatrix(viewChanged);
        renderer_t.canvas.setViewMatrix(Matrix4.mult(lastViewMatrix, zoomMatrix));
    }

    @Override
    public void setReshapeMatrix(){
        mtriangle.setViewMatrix(viewMatrix);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        super.reshape(drawable, x, y, width, height);
        renderer_t = new pmRenderToTexture(gl4, width, height);//change to Syn?
        changed = true;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {mtriangle.gsthForDraw.dispose(gl4);}
}
