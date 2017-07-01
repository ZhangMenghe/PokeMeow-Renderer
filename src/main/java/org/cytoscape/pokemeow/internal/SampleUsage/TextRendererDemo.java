package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import com.pvporbit.freetype.*;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.label.pmLabel;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.pvporbit.freetype.FreeTypeConstants.*;

/**
 * Created by ZhangMenghe on 2017/6/28.
 */
public class TextRendererDemo extends Demo{
        private pmShaderParams gshaderParam;
        private int program;
        private pmBasicNodeShape mtriangle;
        private pmNodeShapeFactory factory;


        private String fontPath = "f://OpenSans-Regular.ttf";
        private pmLabel label;

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
            //mtriangle.setOrigin(new Vector3(1.0f,1.0f,.0f));
            label = new pmLabel(gl4,fontPath);
        }


        @Override
        public void render(GL4 gl4) {
            gl4.glUseProgram(program);
            gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
            factory.drawNode(gl4,mtriangle,gshaderParam);
            label.drawLabel(gl4,"TEST");

        }
        public void reSetMatrix(boolean viewChanged){

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

