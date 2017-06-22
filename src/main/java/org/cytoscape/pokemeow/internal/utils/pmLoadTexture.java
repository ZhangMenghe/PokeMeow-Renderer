package main.java.org.cytoscape.pokemeow.internal.utils;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * Load a simple 2d image as texture
 * Created by ZhangMenghe on 2017/6/21.
 */
public class pmLoadTexture {
    public Texture initialTexture(GL4 gl4, URL pathVS){
        try{
            System.err.println("Loading texture...");
            File pathToFile = new File(pathVS.getPath());

            Texture texture = TextureIO.newTexture(pathToFile, true);
            texture.setTexParameteri(gl4, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
            texture.setTexParameteri(gl4, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
            texture.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
            texture.setTexParameteri(gl4, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);

           return texture;

        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
