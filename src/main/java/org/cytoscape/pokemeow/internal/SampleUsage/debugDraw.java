package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.nodeshape. pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;

import static com.jogamp.opengl.GL.GL_ARRAY_BUFFER;

public class debugDraw implements Demo {
    private pmShaderParams gshaderParam;
    private int program;
    private pmBasicNodeShape[] NodeList;
//    private pmRectangleNodeShape[] triangleNodeList;
    private int numOfNodes = 10;
    private pmLoadTexture textureLoader;
    private Texture texture;
    private pmNodeShapeFactory nodesFactory;

    @Override
    public void create(GL4 gl4) {
        NodeList = new pmBasicNodeShape[numOfNodes];
        nodesFactory = new pmNodeShapeFactory(gl4);

        textureLoader = new pmLoadTexture();
        texture = textureLoader.initialTexture(gl4, debugDraw.class.getResource("Texture.jpg"));

        program = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/texture.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/texture.frag"));
        gshaderParam = new pmShaderParams(gl4,program);
        int n=0;
        for(Byte idx = 0;idx<10;idx++)
            NodeList[n++] = nodesFactory.createNode(gl4, idx);

//        NodeList[1] = nodesFactory.createNode(gl4, pmNodeShapeFactory.SHAPE_VEE);

        gl4.glUseProgram(program);
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                float cx = -0.6f +y*0.6f;
                float cy = -0.6f+ x*0.5f;
                int idx = 3*x+y;
                NodeList[idx].setOrigin(new Vector3(cx,cy,.0f));
            }
        }
        NodeList[9].setOrigin(new Vector3(.0f,0.8f,.0f));
        NodeList[9].setRotation((float) Math.PI/8);
        Vector4 [] test = {new Vector4(1.0f,.0f,.0f,1.0f),new Vector4(.0f,.0f,1.0f,1.0f)};
        NodeList[0].setColor(gl4, test);
//
        NodeList[3].setDefaultTexcoord(gl4);
        NodeList[4].setDefaultTexcoord(gl4);
//        NodeList[1].setOrigin(new Vector3(0.5f,.0f,.0f));

    }

    @Override
    public void render(GL4 gl4) {
        nodesFactory.drawNodeList(gl4,NodeList,gshaderParam,texture);
    }

    @Override
    public void dispose(GL4 gl4) {
        for(int i=0;i<numOfNodes;i++)
            NodeList[i].gsthForDraw.dispose(gl4);
    }

    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
