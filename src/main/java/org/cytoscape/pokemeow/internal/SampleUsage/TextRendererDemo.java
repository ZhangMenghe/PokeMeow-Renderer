package org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import org.cytoscape.pokemeow.internal.algebra.Matrix4;
import org.cytoscape.pokemeow.internal.algebra.Vector2;
import org.cytoscape.pokemeow.internal.algebra.Vector3;
import org.cytoscape.pokemeow.internal.algebra.Vector4;
import org.cytoscape.pokemeow.internal.label.pmLabel;
import org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import org.cytoscape.pokemeow.internal.rendering.pmRenderToTexture;
import org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import org.cytoscape.pokemeow.internal.utils.GLSLProgram;

import java.net.URL;

/**
 * Created by ZhangMenghe on 2017/6/28.
 */
public class TextRendererDemo extends Demo{
        private pmBasicNodeShape mtriangle;
        private pmNodeShapeFactory factory;
        private pmRenderToTexture renderer_t;

        private String fontPath = "f://OpenSans-Regular.ttf";
        private pmLabel label;
        private Matrix4 lastViewMatrix = Matrix4.identity();

        @Override
        public void init(GLAutoDrawable drawable) {
            super.init(drawable);
//            fontPath = Demo.class.getResource("OpenSans-Regular.ttf").getPath();

            program = GLSLProgram.CompileProgram(gl4,
                    Demo.class.getResource("shader/flat.vert"),
                    null,null,null,
                    Demo.class.getResource("shader/flat.frag"));
            gshaderParam = new pmShaderParams(gl4, program);
            factory = new pmNodeShapeFactory(gl4);
            mtriangle = factory.createNode(gl4,pmNodeShapeFactory.SHAPE_TRIANGLE);
            mtriangle.setColor(new Vector4(0.69f, 0.88f, 0.9f,1.0f));
            //mtriangle.setOrigin(new Vector3(1.0f,1.0f,.0f));
            label = new pmLabel(gl4,fontPath);
            //label.setOrigin(new Vector3(0.2f,.0f,.0f));
            //label.setZorder(1);
            //label.setScale(new Vector2(2.0f,1.0f));
            label.setColor(new Vector4(1.0f,1.0f,1.0f,1.0f));
            label.setScale(1.5f);
            renderer_t = new pmRenderToTexture(gl4);
        }

        @Override
        public void display(GLAutoDrawable drawable) {
            super.display(drawable);
            renderer_t.RenderToTexturePrepare(gl4);
            gl4.glUseProgram(program);
            gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
            factory.drawNode(gl4,mtriangle,gshaderParam);
            label.drawLabel(gl4,"LABEL");
            renderer_t.RenderToScreen(gl4);
        }

        @Override
        public void reSetMatrix(boolean viewChanged){
            if(viewChanged){
                viewMatrix = Matrix4.mult(lastViewMatrix, viewMatrix);
                lastViewMatrix = viewMatrix;
            }
            renderer_t.canvas.setViewMatrix(Matrix4.mult(lastViewMatrix, zoomMatrix));
        }

        @Override
        public void dispose(GLAutoDrawable drawable) {
            mtriangle.gsthForDraw.dispose(gl4);
            label.dispose(gl4);
        }
    }

