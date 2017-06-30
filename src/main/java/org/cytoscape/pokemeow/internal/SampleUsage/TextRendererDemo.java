package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import com.pvporbit.freetype.*;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmRenderToTexture;
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

        private Library ft_library;
        private Face ft_face;
        private GlyphSlot ft_glyph;
        private String fontPath = "f://OpenSans-Regular.ttf";
        private int fontTexId;
        private pmLabel label;
        private int fontVAO = 0;
        private int fontVBO = 1;
        private int fontProgram;
        private int[] tmpHandle= new int[2];
        public class mCharacter{
            public int TextureId;// ID handle of the glyph texture
            public Vector2 Size;// Size of glyph
            public Vector2 Bearing;// Offset from baseline to left/top of glyph
            public int Advance;// Horizontal offset to advance to next glyph
        }
        public Map<Character,mCharacter> Characters;

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
            mtriangle.setOrigin(new Vector3(1.0f,1.0f,.0f));

            initLabels(gl4);
        }

    private void initLabels(GL4 gl4){
        Characters = new HashMap<Character,mCharacter>();
        ft_library = com.pvporbit.freetype.FreeType.newLibrary();
        ft_face = ft_library.newFace(fontPath, 0);
        //label = new pmLabel();
        ft_face.setPixelSizes(0,48);
        gl4.glPixelStorei(GL4.GL_UNPACK_ALIGNMENT, 1);

        // Load first 128 characters of ASCII set
        for(int i=33;i<127;i++){
            char c = (char) i;
            ft_face.loadChar(c,FT_LOAD_RENDER);

            ft_glyph = ft_face.getGlyphSlot();
            Bitmap bitmap = ft_glyph.getBitmap();

            gl4.glGenTextures(1,tmpHandle,0);
            fontTexId = tmpHandle[0];
            gl4.glBindTexture(GL4.GL_TEXTURE_2D, fontTexId);
            gl4.glTexImage2D(GL4.GL_TEXTURE_2D,
                    0,
                    GL4.GL_RED,
                    bitmap.getWidth(),
                    bitmap.getRows(),
                    0,
                    GL4.GL_RED,
                    GL4.GL_UNSIGNED_BYTE,
                    ft_glyph.getBitmap().getBuffer());

            // Set texture options
            gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
            gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
            gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
            gl4.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);


            mCharacter tmpC = new mCharacter();
            tmpC.TextureId = fontTexId;
            tmpC.Size = new Vector2(bitmap.getWidth(), bitmap.getRows());
            tmpC.Bearing = new Vector2(ft_glyph.getBitmapLeft(), ft_glyph.getBitmapTop());
            tmpC.Advance = ft_glyph.getAdvance().getX();

            Characters.put(c,tmpC);
        }
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, 0);
		/* --- Delete face --- */
        ft_face.delete();
		/* --- Destroy FreeType --- */
        ft_library.delete();

//        gl4.glEnable(GL4.GL_CULL_FACE);
        gl4.glEnable(GL4.GL_BLEND);
        gl4.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);

        fontProgram = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/text.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/text.frag"));

        gl4.glGenVertexArrays(1, tmpHandle, fontVAO);
        gl4.glGenBuffers(1, tmpHandle, fontVBO);

        gl4.glBindVertexArray(tmpHandle[fontVAO]);
        gl4.glBindBuffer(GL.GL_ARRAY_BUFFER, tmpHandle[fontVBO]);

        gl4.glBufferData(GL4.GL_ARRAY_BUFFER, Float.BYTES*30, null, GL4.GL_DYNAMIC_DRAW);

        gl4.glEnableVertexAttribArray(0);
        gl4.glVertexAttribPointer(0, 3, GL4.GL_FLOAT, false, 5 * Float.BYTES, 0);

        gl4.glEnableVertexAttribArray(1);
        gl4.glVertexAttribPointer(1, 2, GL4.GL_FLOAT, false, 5 * Float.BYTES, 3*Float.BYTES);

        gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
        gl4.glBindVertexArray(0);
    }
    public static BitmapFont getFont(String name, int size) {
        FreeTypeFontGenerator generator = null;
        try {
            generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/" + name));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
            parameter.size = size;
            return generator.generateFont(parameter);
        } finally {
            if (generator != null) generator.dispose();
        }
    }
        @Override
        public void render(GL4 gl4) {
            gl4.glUseProgram(program);
            gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
            factory.drawNode(gl4,mtriangle,gshaderParam);

            renderLabel(gl4,fontProgram, "M",.0f,.0f,0.5f, new Vector4(.0f,1.0f,.0f,1.0f));

        }
        private void renderLabel(GL4 gl4, int program, String text, float x, float y, float scale, Vector4 color){
            gl4.glUseProgram(program);
            gl4.glUniform1i(gl4.glGetUniformLocation(program,"texSampler"),0);
            gl4.glUniform3f(gl4.glGetUniformLocation(program,"textColor"), color.x,color.y,color.z);

            Matrix4 modelMatrix = Matrix4.translation(new Vector3(.0f,.0f,.0f));
            gl4.glUniformMatrix4fv(gl4.glGetUniformLocation(program,"modelMatrix"),1,false, Buffers.newDirectFloatBuffer(modelMatrix.asArrayCM()));
            gl4.glActiveTexture(GL4.GL_TEXTURE0);
            gl4.glBindVertexArray(tmpHandle[fontVAO]);
            char [] charArray = text.toCharArray();
            for(char c:charArray){
                mCharacter ch = Characters.get(c);
                float xpos =x; //.0f;//x+ch.Bearing.x*scale;
                float ypos = .0f;//y-(ch.Size.y - ch.Bearing.y)*scale;

                float w = 0.25f;//ch.Size.x * scale;
                float h = 0.25f;//ch.Size.y * scale;

                //update VBO for each character
                float []vertices = {
                         xpos,     ypos + h,  0.0f, 0.0f, 0.0f ,
                        xpos,     ypos,       0.0f,0.0f, 1.0f ,
                         xpos + w, ypos,     0.0f,  1.0f, 1.0f ,

                         xpos,     ypos + h, 0.0f,  0.0f, 0.0f,
                         xpos + w, ypos,      0.0f, 1.0f, 1.0f ,
                         xpos + w, ypos + h, 0.0f,  1.0f, 0.0f
                };
                FloatBuffer vertice_buf = Buffers.newDirectFloatBuffer(vertices);
                gl4.glBindTexture(GL4.GL_TEXTURE_2D, ch.TextureId);

                gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, tmpHandle[fontVBO]);
                gl4.glBufferSubData(GL4.GL_ARRAY_BUFFER, 0, Float.BYTES * 30, vertice_buf);
                //gl4.glBufferSubData(GL4.GL_ARRAY_BUFFER, 3*Float.BYTES, Float.BYTES * 30, vertice_buf);
                //gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);

                gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, 6);
                x +=0.4f; //(ch.Advance >>6) * scale;
            }
            gl4.glBindVertexArray(0);
            gl4.glBindTexture(GL4.GL_TEXTURE_2D, 0);
        }
        public void reSetMatrix(boolean viewChanged){
            //mtriangle.setViewMatrix(viewMatrix);
        }

        @Override
        public void dispose(GL4 gl4) {
            //mtriangle.gsthForDraw.dispose(gl4);
        }

        @Override
        public void resize(GL4 gl4, int x, int y, int width, int height) {
            //gl4.glViewport(x, y, width, height);
        }
    }

