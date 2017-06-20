package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;

public interface Demo {

    void create(GL4 gl4);

    void render(GL4 gl4);

    void dispose(GL4 gl4);

    void resize(GL4 gl4, int x, int y, int width, int height);
}
