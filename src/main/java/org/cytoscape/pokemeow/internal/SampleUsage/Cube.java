package main.java.org.cytoscape.pokemeow.internal.SampleUsage;

import java.awt.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.JFrame;//lightweight graphical frame
import com.jogamp.opengl.util.FPSAnimator;

public class Cube implements GLEventListener {

    public static DisplayMode dm, dm_old;
    private GLU glu = new GLU();
    private float rquad = 0.0f;

    /**********************************************/
    /*GLEventListener Interface Method*/
    /*display: draw elements using OpenGL*/
    /*dispose: signals the listener to perform the release of all OpenGL resources per each GLContext*/
    /*init: immediately after the OpenGL context is initialized - 自动调after GLCanvas */
    /*reshape*/
    /**********************************************/
    //GLAutoDrawable Interface
    //GLAutoDrawable automatically creates a primary rendering context which is associated with GLAutoDrawable
    //GL getGL()
    //destroy()
    //void addGLEventListener(GLEventListener Listener)
    /**********************************************/
    @Override
    public void display( GLAutoDrawable drawable ) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT );
        gl.glLoadIdentity();//replace the current matrix with the identity matrix
        gl.glTranslatef( 0f, 0f, -5.0f );

        // Rotate The Cube On X, Y & Z
        gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f);

        //giving different colors to different sides
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube
        gl.glColor3f(1f,0f,0f); //red color
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
        gl.glVertex3f( -1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
        gl.glVertex3f( -1.0f, 1.0f, 1.0f ); // Bottom Left Of The Quad (Top)
        gl.glVertex3f( 1.0f, 1.0f, 1.0f ); // Bottom Right Of The Quad (Top)

        gl.glColor3f( 0f,1f,0f ); //green color
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Top Right Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Top Left Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Bottom Right Of The Quad

        gl.glColor3f( 0f,0f,1f ); //blue color
        gl.glVertex3f( 1.0f, 1.0f, 1.0f ); // Top Right Of The Quad (Front)
        gl.glVertex3f( -1.0f, 1.0f, 1.0f ); // Top Left Of The Quad (Front)
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Bottom Right Of The Quad

        gl.glColor3f( 1f,1f,0f ); //yellow (red + green)
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Bottom Right Of The Quad
        gl.glVertex3f( -1.0f, 1.0f, -1.0f ); // Top Right Of The Quad (Back)
        gl.glVertex3f( 1.0f, 1.0f, -1.0f ); // Top Left Of The Quad (Back)

        gl.glColor3f( 1f,0f,1f ); //purple (red + green)
        gl.glVertex3f( -1.0f, 1.0f, 1.0f ); // Top Right Of The Quad (Left)
        gl.glVertex3f( -1.0f, 1.0f, -1.0f ); // Top Left Of The Quad (Left)
        gl.glVertex3f( -1.0f, -1.0f, -1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( -1.0f, -1.0f, 1.0f ); // Bottom Right Of The Quad

        gl.glColor3f( 0f,1f, 1f ); //sky blue (blue +green)
        gl.glVertex3f( 1.0f, 1.0f, -1.0f ); // Top Right Of The Quad (Right)
        gl.glVertex3f( 1.0f, 1.0f, 1.0f ); // Top Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, 1.0f ); // Bottom Left Of The Quad
        gl.glVertex3f( 1.0f, -1.0f, -1.0f ); // Bottom Right Of The Quad
        gl.glEnd(); // Done Drawing The Quad
        gl.glFlush();
        rquad -= 0.15f;

        //Lighting
        gl.glEnable( GL2.GL_LIGHTING );
        gl.glEnable( GL2.GL_LIGHT0 );
        gl.glEnable( GL2.GL_NORMALIZE );

        // weak RED ambient
        float[] ambientLight = { 0.1f, 0.f, 0.f,0f };
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambientLight, 0);

        // multicolor diffuse
        float[] diffuseLight = { 1f,2f,1f,0f };
        gl.glLightfv( GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuseLight, 0 );

    }

    @Override
    public void dispose( GLAutoDrawable drawable ) {
        // TODO Auto-generated method stub
    }

    @Override
    public void init( GLAutoDrawable drawable ) {

        final GL2 gl = drawable.getGL().getGL2();
        gl.glShadeModel( GL2.GL_SMOOTH );
        gl.glClearColor( 0f, 0f, 0f, 0f );
        gl.glClearDepth( 1.0f );
        gl.glEnable( GL2.GL_DEPTH_TEST );
        gl.glDepthFunc( GL2.GL_LEQUAL );
        gl.glHint( GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST );
    }

    @Override
    public void reshape( GLAutoDrawable drawable, int x, int y, int width, int height ) {

        // TODO Auto-generated method stub
        final GL2 gl = drawable.getGL().getGL2();
        if( height <= 0 )
            height = 1;

        final float h = ( float ) width / ( float ) height;
        gl.glViewport( 0, 0, width, height );
        gl.glMatrixMode( GL2.GL_PROJECTION );
        gl.glLoadIdentity();

        glu.gluPerspective( 45.0f, h, 1.0, 20.0 );
        gl.glMatrixMode( GL2.GL_MODELVIEW );
        gl.glLoadIdentity();


    }

    public static void main( String[] args ) {
        //specify the exact version of OpenGL API being used
        final GLProfile profile = GLProfile.get( GLProfile.GL2 );
        //specifies a set of OpenGL capabilities
        GLCapabilities capabilities = new GLCapabilities( profile );
        Cube cube = new Cube();
        // The GLcanvas&GLJpanel JOGL GUI that implement GLAutoDrawable interface

        //METHOD 1: GLCanvas

//        final GLCanvas glcanvas = new GLCanvas( capabilities );
//        glcanvas.addGLEventListener( cube );
//        glcanvas.setSize( 400, 400 );


        //METHOD 2: GLJPanel
        GLJPanel gljPanel = new GLJPanel(capabilities);
        gljPanel.addGLEventListener(cube);
        gljPanel.setSize(400,400);

        //Method1: Frame
//        final Frame mframe = new Frame("test");
//        mframe.add(glcanvas);
//        mframe.setVisible(true);

        //Method2: JFrame
        final JFrame frame = new JFrame ( " Multicolored cube" );
        frame.getContentPane().add( gljPanel );
        frame.setSize( 800,800 );
        frame.setVisible( true );

        //glcanvas.display();
        //call display in animation way
        //final FPSAnimator animator = new FPSAnimator(gljPanel, 300,true);

        //animator.start();
    }

}