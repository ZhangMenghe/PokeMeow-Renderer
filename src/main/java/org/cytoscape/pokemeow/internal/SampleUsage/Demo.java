package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmRenderToTexture;

public abstract class Demo {

    public Matrix4 viewMatrix = Matrix4.identity();
    public Matrix4 zoomMatrix= Matrix4.identity();

    public abstract void create(GL4 gl4);

    public abstract void render(GL4 gl4);

    public abstract void dispose(GL4 gl4);

    public abstract void resize(GL4 gl4, int x, int y, int width, int height);
    public abstract void reSetMatrix(boolean viewChanged);
}
