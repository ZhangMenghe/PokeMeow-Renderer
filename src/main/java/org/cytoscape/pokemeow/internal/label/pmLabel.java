package main.java.org.cytoscape.pokemeow.internal.label;

import main.java.org.cytoscape.pokemeow.internal.SampleUsage.debugDraw;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;

import com.pvporbit.freetype.*;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.commonUtil;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static com.pvporbit.freetype.FreeTypeConstants.FT_LOAD_RENDER;

/**
 * Created by ZhangMenghe on 2017/6/29.
 */
public class pmLabel{
    private Library ft_library;
    private Face ft_face;
    private GlyphSlot ft_glyph;

    private int fontTexId;
    private int fontProgram;
    private int fontVAO = 0;
    private int fontVBO = 1;

    private int[] tmpHandle= new int[2];

    private Vector3 origin = new Vector3();
    private Matrix4 modelMatrix = Matrix4.identity();
    private float zorder = -0.8f;
    private Vector3 scale = new Vector3(1.0f);

    private Vector4  color = new Vector4(.0f,1.0f,.0f,1.0f);
    public class mCharacter{
        public int TextureId;// ID handle of the glyph texture
        public Vector2 Size;// Size of glyph
        public Vector2 Bearing;// Offset from baseline to left/top of glyph
        public int Advance;// Horizontal offset to advance to next glyph
    }
    public Map<Character,mCharacter> Characters;

    public pmLabel(GL4 gl4, String fontPath){
        Characters = new HashMap<Character, mCharacter>();
        ft_library = com.pvporbit.freetype.FreeType.newLibrary();
        ft_face = ft_library.newFace(fontPath, 0);
        ft_face.setPixelSizes(0,48);

        gl4.glPixelStorei(GL4.GL_UNPACK_ALIGNMENT, 1);
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

        gl4.glEnable( GL4.GL_DEPTH_TEST );
        gl4.glDepthFunc( GL4.GL_LEQUAL );
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
    public void drawLabel(GL4 gl4, String content){
        gl4.glUseProgram(fontProgram);
        gl4.glUniform1i(gl4.glGetUniformLocation(fontProgram,"texSampler"),0);
        gl4.glUniform3f(gl4.glGetUniformLocation(fontProgram,"textColor"), color.x,color.y,color.z);

        gl4.glUniformMatrix4fv(gl4.glGetUniformLocation(fontProgram,"modelMatrix"),1,false, Buffers.newDirectFloatBuffer(modelMatrix.asArrayCM()));
        gl4.glActiveTexture(GL4.GL_TEXTURE0);
        gl4.glBindVertexArray(tmpHandle[fontVAO]);

        char [] charArray = content.toCharArray();
        float x = .0f;
        float y = .0f;
        for(char c:charArray){
            mCharacter ch = Characters.get(c);
            float xpos = x + ch.Bearing.x*scale.x;
            float ypos = y - (ch.Size.y - ch.Bearing.y)*scale.y;
            Vector2 relPos = commonUtil.getRelativePos(xpos, ypos);

            float w = ch.Size.x * scale.x;
            float h = ch.Size.y * scale.y;
            Vector2 relPos_s = commonUtil.getRelativePos(w, h);

            //update VBO for each character
            float []vertices = {
                    relPos.x,     relPos.y + relPos_s.y,  zorder, 0.0f, 0.0f ,
                    relPos.x,     relPos.y,       zorder,0.0f, 1.0f ,
                    relPos.x + relPos_s.x, relPos.y,     zorder,  1.0f, 1.0f ,

                    relPos.x,     relPos.y + relPos_s.y, zorder,  0.0f, 0.0f,
                    relPos.x + relPos_s.x, relPos.y,      zorder, 1.0f, 1.0f ,
                    relPos.x + relPos_s.x, relPos.y + relPos_s.y, zorder,  1.0f, 0.0f
            };
            FloatBuffer vertice_buf = Buffers.newDirectFloatBuffer(vertices);
            gl4.glBindTexture(GL4.GL_TEXTURE_2D, ch.TextureId);

            gl4.glBindBuffer(GL4.GL_ARRAY_BUFFER, tmpHandle[fontVBO]);
            gl4.glBufferSubData(GL4.GL_ARRAY_BUFFER, 0, Float.BYTES * 30, vertice_buf);
            gl4.glDrawArrays(GL4.GL_TRIANGLES, 0, 6);
            x += (ch.Advance >>6) * scale.x;
        }
        gl4.glBindVertexArray(0);
        gl4.glBindTexture(GL4.GL_TEXTURE_2D, 0);
    }

    public void setScale(Vector2 new_scale){
        scale.x *= new_scale.x;
        scale.y *= new_scale.y;
    }

    public void setScale(float s_scale){
        scale.x *= s_scale;
        scale.y *= s_scale;
    }

    public void setOrigin(Vector3 new_origin){
        origin = new_origin;
        modelMatrix = Matrix4.mult(Matrix4.translation(origin),Matrix4.scale((scale)));
    }

    public void setColor(Vector4 new_color){
        color = new_color;
    }

    public void setZorder(float new_z){
        zorder = new_z;
    }

    public void dispose(GL4 gl4) {
        gl4.glDeleteBuffers(2,tmpHandle,0);
        gl4.glDeleteVertexArrays(1,tmpHandle,fontVAO);
    }
}
