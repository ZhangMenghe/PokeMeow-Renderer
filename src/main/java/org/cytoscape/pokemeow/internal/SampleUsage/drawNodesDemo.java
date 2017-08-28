package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmRenderToTexture;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.nodeshape. pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;

import javax.xml.soap.Node;
import java.util.ArrayList;
import java.util.Random;

public class drawNodesDemo extends Demo {
    private pmShaderParams flatShaderParam;
    private pmShaderParams texShaderParam;
    private int[] programList;
    private pmBasicNodeShape[] NodeList;

    private pmLoadTexture textureLoader;
    private Texture texture;
    private ArrayList<Texture> textureList = new ArrayList<Texture>();
    private ArrayList<Integer> textureIds;
    private pmNodeShapeFactory nodesFactory;
    private ArrayList<Integer>flatNodeIndices = new ArrayList<Integer>();
    private ArrayList<Integer> textureNodeIndices=new ArrayList<Integer>();
    public pmRenderToTexture renderer_t;
    private boolean changed = true;


    @Override
    public void init(GLAutoDrawable drawable) {
        super.init(drawable);
        numOfItems = 10;
        programList = new int[2];
        NodeList = new pmBasicNodeShape[numOfItems];
        nodesFactory = new pmNodeShapeFactory(gl4);

        textureLoader = new pmLoadTexture();
        texture = textureLoader.initialTexture(gl4, Demo.class.getResource("Texture.jpg"));
        Texture texture2 = textureLoader.initialTexture(gl4, Demo.class.getResource("Texture2.jpg"));
        programList[0] = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/flat.vert"),
                null,null,null,
                Demo.class.getResource("shader/flat.frag"));

        programList[1] = GLSLProgram.CompileProgram(gl4,
                Demo.class.getResource("shader/texture.vert"),
                null,null,null,
                Demo.class.getResource("shader/texture.frag"));

        flatShaderParam = new pmShaderParams(gl4, programList[0]);
        texShaderParam = new pmShaderParams(gl4, programList[1]);


        int n=0;
        for(byte idx = 0;idx<10;idx++)
            NodeList[n++] = nodesFactory.createNode(gl4, idx);

        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                float cx = -0.6f + y*0.5f;
                float cy = -0.6f + x*0.5f;
                int idx = 3*x+y;
                NodeList[idx].setOrigin(new Vector3(cx, cy, .0f));
                NodeList[idx].setScale(0.5f);
                NodeList[idx].useTexture = true;
            }
        }
        //test zOrder:the less z value the more front position
//        NodeList[2].setZorder(-0.1f);
//        NodeList[5].setZorder(0.1f);
//        NodeList[8].setZorder(0.2f);

        NodeList[9].setOrigin(new Vector3(.0f,0.8f,.0f));
        NodeList[9].setRotation((float) Math.PI/8);
        NodeList[9].setScale(0.5f);
        NodeList[9].setColor(new Vector4(1.0f,.0f,.0f,1.0f));

        for(int i=0; i<numOfItems; i++){
            if(NodeList[i].useTexture)
                textureNodeIndices.add(i);
            else
                flatNodeIndices.add(i);
        }
        textureList.add(texture);
        textureList.add(texture2);
        textureIds = new ArrayList<Integer>();
        for(int i=0;i<textureNodeIndices.size();i++)
            textureIds.add(0);
        renderer_t = new pmRenderToTexture(gl4, (int)viewportSize.x, (int)viewportSize.y);
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        super.display(drawable);
        gl4.glUseProgram(programList[0]);
        gl4.glClear(GL4.GL_DEPTH_BUFFER_BIT | GL4.GL_COLOR_BUFFER_BIT);
        if(changed){
            renderer_t.RenderToTexturePrepare(gl4);

            nodesFactory.drawNodeList(gl4,NodeList,
                    programList,
                    texShaderParam,
                    flatShaderParam,
                    textureList,
                    flatNodeIndices,
                    textureNodeIndices,
                    textureIds);
            changed = false;
        }
        renderer_t.RenderToScreen(gl4);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        for(int i=0;i<numOfItems;i++)
            NodeList[i].gsthForDraw.dispose(gl4);
    }

    @Override
    public void reSetMatrix(boolean viewChanged){
        super.reSetMatrix(viewChanged);
        renderer_t.canvas.setViewMatrix(Matrix4.mult(lastViewMatrix, zoomMatrix));
    }

    @Override
    public void setReshapeMatrix(){
        for(pmBasicNodeShape node: NodeList)
            node.setViewMatrix(viewMatrix);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        super.reshape(drawable, x, y, width, height);
        renderer_t = new pmRenderToTexture(gl4,width,height);//change to Syn?
        changed = true;
    }
}
