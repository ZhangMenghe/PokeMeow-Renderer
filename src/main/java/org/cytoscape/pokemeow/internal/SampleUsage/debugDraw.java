package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.util.texture.Texture;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector4;
import main.java.org.cytoscape.pokemeow.internal.rendering.pmShaderParams;
import main.java.org.cytoscape.pokemeow.internal.nodeshape. pmBasicNodeShape;
import main.java.org.cytoscape.pokemeow.internal.nodeshape.pmNodeShapeFactory;
import main.java.org.cytoscape.pokemeow.internal.utils.GLSLProgram;
import main.java.org.cytoscape.pokemeow.internal.utils.pmLoadTexture;

import java.util.ArrayList;
import java.util.Random;

public class debugDraw extends Demo {
    private pmShaderParams gshaderParam;
    private int[] programList;
    private pmBasicNodeShape[] NodeList;

    private int numOfNodes = 10;
    private pmLoadTexture textureLoader;
    private Texture texture;
    private ArrayList<Texture> textureList = new ArrayList<Texture>();
    private ArrayList<Integer> textureIds;
    private pmNodeShapeFactory nodesFactory;
    private ArrayList<Integer>flatNodeIndices = new ArrayList<Integer>();
    private ArrayList<Integer> textureNodeIndices=new ArrayList<Integer>();

    @Override
    public void create(GL4 gl4) {
        programList = new int[2];
        NodeList = new pmBasicNodeShape[numOfNodes];
        nodesFactory = new pmNodeShapeFactory(gl4);

        textureLoader = new pmLoadTexture();
        texture = textureLoader.initialTexture(gl4, debugDraw.class.getResource("Texture.jpg"));
        Texture texture2 = textureLoader.initialTexture(gl4, debugDraw.class.getResource("Texture2.jpg"));
        programList[0] = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/flat.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/flat.frag"));

        programList[1] = GLSLProgram.CompileProgram(gl4,
                debugDraw.class.getResource("shader/texture.vert"),
                null,null,null,
                debugDraw.class.getResource("shader/texture.frag"));

        gshaderParam = new pmShaderParams(gl4, programList[0]);
        int n=0;
        for(Byte idx = 0;idx<10;idx++)
            NodeList[n++] = nodesFactory.createNode(gl4, idx);

//        NodeList[1] = nodesFactory.createNode(gl4, pmNodeShapeFactory.SHAPE_VEE);


        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                float cx = -0.6f + y*0.5f;
                float cy = -0.6f + x*0.5f;
                int idx = 3*x+y;
                NodeList[idx].setOrigin(new Vector3(cx, cy, .0f));
            }
        }
        //test zOrder:the less z value the more front position
//        NodeList[2].setZorder(gl4,-1);
//        NodeList[5].setZorder(gl4,1);
//        NodeList[8].setZorder(gl4,2);

        NodeList[9].setOrigin(new Vector3(.0f,0.8f,.0f));
        NodeList[9].setRotation((float) Math.PI/8);
        Vector4 [] test = {new Vector4(1.0f,.0f,.0f,1.0f),new Vector4(.0f,.0f,1.0f,1.0f)};
        NodeList[0].setColor(gl4, test);
        NodeList[1].setColor(gl4, test);
        NodeList[2].setColor(gl4, test);
        NodeList[6].setColor(gl4, new Vector4(0.5f,0.5f,.0f,0.8f));
        NodeList[7].setColor(gl4, new Vector4(0.5f,0.5f,.0f,0.8f));
        NodeList[8].setColor(gl4, new Vector4(0.5f,0.5f,.0f,0.8f));

        NodeList[3].setDefaultTexcoord(gl4);
        NodeList[4].setDefaultTexcoord(gl4);
        NodeList[5].setDefaultTexcoord(gl4);
//        NodeList[1].setOrigin(new Vector3(0.5f,.0f,.0f));
        for(int i=0;i<numOfNodes;i++){
            if(NodeList[i].useTexture)
                textureNodeIndices.add(i);
            else
                flatNodeIndices.add(i);
        }
        textureList.add(texture);
        textureList.add(texture2);
        textureIds = new ArrayList<Integer>();
        Random rand  = new Random();
        for(int i=0;i<textureNodeIndices.size();i++)
            textureIds.add(rand.nextInt(2));
    }

    @Override
    public void render(GL4 gl4) {
        nodesFactory.drawNodeList(gl4,NodeList,
                programList,
                gshaderParam,
                textureList,
                flatNodeIndices,
                textureNodeIndices,
                textureIds);
    }

    @Override
    public void dispose(GL4 gl4) {
        for(int i=0;i<numOfNodes;i++)
            NodeList[i].gsthForDraw.dispose(gl4);
    }
    public void reSetMatrix(){
        for(int i=0;i<numOfNodes;i++)
            NodeList[i].setViewMattrix(viewMatrix);
    }
    @Override
    public void resize(GL4 gl4, int x, int y, int width, int height) {
        gl4.glViewport(x, y, width, height);
    }
}
