package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.label.pmLabel;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmRenderToTexture;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;

/**
 * Created by ZhangMenghe on 2017/6/28.
 */
public class TextRendererDemo extends Demo{
        private pmShaderParams gshaderParam;
        private int program;
        private pmBasicNodeShape mtriangle;
        private pmNodeShapeFactory factory;
        private pmRenderToTexture renderer_t;

        private String fontPath = "f://OpenSans-Regular.ttf";
        private pmLabel label;
        private Matrix4 lastViewMatrix = Matrix4.identity();
        @Override
        public void create(GL4 gl4) {
            program = GLSLProgram.CompileProgram(gl4,
                    debugDraw.class.getResource("shader/flat.vert"),
                    null,null,null,
                    debugDraw.class.getResource("shader/flat.frag"));
            gshaderParam = new pmShaderParams(gl4, program);
            factory = new pmNodeShapeFactory(gl4);
            mtriangle = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_TRIANGLE);
            mtriangle.setColor(gl4, new Vector4(205f/255,205f/255,.0f,1.0f));
            //mtriangle.setOrigin(new Vector3(1.0f,1.0f,.0f));
            label = new pmLabel(gl4,fontPath);
            //label.setOrigin(new Vector3(0.2f,.0f,.0f));
            //label.setZorder(1);
            //label.setScale(new Vector2(2.0f,1.0f));
            label.setColor(new Vector4(1.0f,0.5f,0.3f,1.0f));
            label.setScale(1.5f);
            renderer_t = new pmRenderToTexture(gl4);
        }

        @Override
        public void render(GL4 gl4) {
            renderer_t.RenderToTexturePrepare(gl4);
            gl4.glUseProgram(program);
            gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
            factory.drawNode(gl4,mtriangle,gshaderParam);
            label.drawLabel(gl4,"LABEL");
            renderer_t.RenderToScreen(gl4);
        }

    public void reSetMatrix(boolean viewChanged){
        if(viewChanged){
            viewMatrix = Matrix4.mult(lastViewMatrix, viewMatrix);
            lastViewMatrix = viewMatrix;
        }
        renderer_t.canvas.setViewMatrix(Matrix4.mult(lastViewMatrix, zoomMatrix));
    }

        @Override
        public void dispose(GL4 gl4) {
            mtriangle.gsthForDraw.dispose(gl4);
            label.dispose(gl4);
        }

        @Override
        public void resize(GL4 gl4, int x, int y, int width, int height) {
            gl4.glViewport(x, y, width, height);
        }
    }

