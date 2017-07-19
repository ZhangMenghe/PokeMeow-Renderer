package main.java.org.cytoscape.pokemeow.internal.viewport;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;

import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector2;
import main.java.org.cytoscape.pokemeow.internal.algebra.Vector3;
import main.java.org.cytoscape.pokemeow.internal.camera.Camera;

import main.java.org.cytoscape.pokemeow.internal.SampleUsage.Demo;

import com.jogamp.nativewindow.NativeSurface;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.math.FloatUtil;

/**
 * A viewport takes care of initializing OpenGL, creating a visible control,
 * and registering user interactions with it, i. e. mouse/keyboard actions.
 * 
 */
public class Viewport implements GLEventListener, MouseListener, MouseMotionListener, MouseWheelListener
{
	// Event listeners
	private HashSet<ViewportEventListener> viewportEventListeners = new HashSet<>();
	private HashSet<ViewportMouseEventListener> viewportMouseEventListeners = new HashSet<>();
	
	// Current GL context
	private GL4 gl;
	
	// Panel that presents GL's frame buffer
	private GLJPanel panel;
	
	// Camera object that is controlled by user actions in this viewport
	// and that determines the viewport's view and projection matrices
	private Camera camera;
	
	private float scaleDPI;
	
	// Mouse handling
	private Vector2 lastMousePosition;

	private static class MouseStates
	{
		public static int IDLE = 0;
		public static int PAN = 1;
		public static int ROTATE = 2;
		public static int SELECT = 3;
	}
	private int mouseState = MouseStates.IDLE;

	/*Parameters for DEBUG*/
	public Boolean DEBUGMODE = true;
	public Boolean triggered = true;
	private Vector2 lastclickPos;
	public Demo demo = null;
	private float zoomFactor = 2.0f;
	private float currentAngle = .0f;
	public Viewport(JComponent container)
	{
		GLProfile profile = GLProfile.getDefault(); // Use the system's default version of OpenGL
		GLCapabilities capabilities = new GLCapabilities(profile);
		capabilities.setDepthBits(24);
		capabilities.setSampleBuffers(true);
		capabilities.setNumSamples(8);
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		
		panel = new GLJPanel(capabilities);
		panel.setIgnoreRepaint(true);
		panel.addGLEventListener(this);		
		panel.addMouseListener(this);
		panel.addMouseMotionListener(this);
		panel.addMouseWheelListener(this);

		camera = new Camera(this);

		if (container instanceof JInternalFrame) 
		{
			container.setSize(300,300);
			JInternalFrame JInframe = (JInternalFrame) container;
            JInframe.getContentPane().setLayout(new BorderLayout());
            JInframe.getContentPane().add(panel, BorderLayout.CENTER);
		} 
		else 
		{
			container.setLayout(new BorderLayout());
			container.add(panel, BorderLayout.CENTER);
		}

	}
	
	/**
	 * Gets current GL context
	 * 
	 * @return GL context
	 */
	public GL4 getContext()
	{
		return gl;
	}
	
	/**
	 * Gets the panel that presents the framebuffer
	 * 
	 * @return Panel control
	 */
	public GLJPanel getPanel()
	{
		return panel;
	}
	
	/**
	 * Gets the camera managed by this viewport
	 * 
	 * @return Camera object
	 */
	public Camera getCamera()
	{
		return camera;
	}
	
	// GLEventListener methods:

	/**
	 * Callback method invoked when the GLJPanel needs to be initialized.
	 * Sets up permanent GL parameters
	 * 
	 * @param drawable GLJPanel handle
	 */
	@Override
	public void init(GLAutoDrawable drawable) 
	{ 
		gl = drawable.getGL().getGL4();
		
		gl.glEnable(GL4.GL_DEPTH_TEST);		
		gl.glDisable(GL4.GL_CULL_FACE);
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glEnable(GL4.GL_BLEND);
		gl.glBlendFunc(GL4.GL_SRC_ALPHA, GL4.GL_ONE_MINUS_SRC_ALPHA);

		gl.glViewport(0, 0, drawable.getSurfaceWidth(), drawable.getSurfaceHeight());
		NativeSurface surface = drawable.getNativeSurface();
		int[] windowUnits = new int[] {100, 100};
		windowUnits = surface.convertToPixelUnits(windowUnits);
		scaleDPI = (float)windowUnits[0] / 100.0f;
		demo.init(drawable);
		invokeViewportInitializeEvent(drawable);
	}
	
	/**
	 * Callback method invoked when the GLJPanel needs to be redrawn.
	 * Clears framebuffer and raises the ViewportDisplay event.
	 * 
	 * @param drawable GLJPanel handle
	 */
	@Override
	public void display(GLAutoDrawable drawable) 
	{ 
		long timeStart = System.nanoTime();
		
		gl = drawable.getGL().getGL4();
		
		gl.glClearColor(0.2f, 0.2f, 0.2f, 1.0f);
		gl.glClearDepthf(1.0f);
		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);

//		if(triggered)
		demo.display(drawable);

		invokeViewportDisplayEvent(drawable);
		
		long timeFinish = System.nanoTime();
		float FPS = 1.0f / ((float)(timeFinish - timeStart) * 1e-9f);
		//System.out.println(FPS + " fps");
	}

	/**
	 * Callback method invoked when the GLJPanel is resized.
	 * Sets viewport size for GL and raises the ViewportResized event.
	 * 
	 * @param drawable GLJPanel handle
	 * @param x Horizontal offset of the left edge
	 * @param y Vertical offset of the top edge
	 * @param width New viewport width
	 * @param height New viewport height
	 */
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
	{
		gl = drawable.getGL().getGL4();
		
		gl.glViewport(x, y, width, height);

		Vector2 newRawSize = new Vector2(width, height);
		ViewportResizedEvent e = new ViewportResizedEvent(newRawSize, Vector2.scalarMult(scaleDPI, newRawSize));
		invokeViewportReshapeEvent(drawable, e);
	}
	
	/**
	 * Forces the viewport to redraw its contents.
	 */
	public void redraw(boolean viewChanged)
	{
		demo.reSetMatrix(viewChanged);
		panel.repaint();
	}

	/**
	 * Frees all resources associated with this viewport
	 * and raises the ViewportDispose event. 
	 */
	@Override
	public void dispose(GLAutoDrawable drawable) 
	{ 
		invokeViewportDisposeEvent(drawable);
	}
	
	// Handle mouse events from GLJPanel:
	@Override
	public void mouseClicked(MouseEvent e)
	{
		System.out.println("click");
		ViewportMouseEvent event = new ViewportMouseEvent(e, new Vector2(), scaleDPI, camera);
		invokeViewportMouseDownEvent(event);
		if(!triggered){
			triggered = true;
			redraw(false);
		}
		if (event.handled)
			return;
	}

	@Override
	public void mouseEntered(MouseEvent e) 
	{
		lastMousePosition = new Vector2(e.getX(), e.getY());
		ViewportMouseEvent event = new ViewportMouseEvent(e, new Vector2(), scaleDPI, camera);
		invokeViewportMouseEnterEvent(event);
		if (event.handled)
			return;
	}

	@Override
	public void mouseExited(MouseEvent e) 
	{
		lastMousePosition = null;
		ViewportMouseEvent event = new ViewportMouseEvent(e, new Vector2(), scaleDPI, camera);
		invokeViewportMouseLeaveEvent(event);
		if (event.handled)
			return;
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		ViewportMouseEvent event = new ViewportMouseEvent(e, new Vector2(), scaleDPI, camera);
		invokeViewportMouseDownEvent(event);
		if (event.handled)
			return;
		
		if (event.m2)
		{
			if (event.keyCtrl)
				mouseState = MouseStates.ROTATE;
			else
				mouseState = MouseStates.PAN;
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
		ViewportMouseEvent event = new ViewportMouseEvent(e, new Vector2(), scaleDPI, camera);
		invokeViewportMouseUpEvent(event);
		if (event.handled)
			return;
		
		if (mouseState == MouseStates.SELECT)
		{
			// Selection handling
		}
		else
		{
			mouseState = MouseStates.IDLE;
		}
	}

	/**
	 * Callback method invoked through mouse cursor dragging.
	 * Unless hijacked by a listener, the camera is rotated around its
	 * target (with left button), or panned within the focal plane (with middle button). 
	 */
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		Vector2 diff = new Vector2();
		
		if (lastMousePosition == null)
		{
			lastMousePosition = new Vector2(e.getX(), e.getY());
		}
		else
		{
			Vector2 newPosition = new Vector2(e.getX(), e.getY());
			diff = Vector2.subtract(newPosition, lastMousePosition);
			diff.y *= -1.0f;
			lastMousePosition = newPosition;
		}
		
		ViewportMouseEvent event = new ViewportMouseEvent(e, diff, scaleDPI, camera);
		invokeViewportMouseMoveEvent(event);
		if (event.handled)
			return;
		
		if (mouseState == MouseStates.PAN)
		{
			System.out.println("PAN");
			demo.viewMatrix = Matrix4.translation(new Vector3(diff.x/100,diff.y/100,.0f));
			//camera.panByPixels(new Vector2(-diff.x, -diff.y));
			redraw(true);
		}
		else if (mouseState == MouseStates.ROTATE)
		{
			currentAngle =(float) Math.atan(diff.x/diff.y);
			demo.viewMatrix = Matrix4.rotationZ(currentAngle);
			redraw(true);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) 
	{
		Vector2 diff = new Vector2();
		
		if (lastMousePosition == null)
		{
			lastMousePosition = new Vector2(e.getX(), e.getY());
		}
		else
		{
			Vector2 newPosition = new Vector2(e.getX(), e.getY());
			diff = Vector2.subtract(newPosition, lastMousePosition);
			lastMousePosition = newPosition;
		}
		
		ViewportMouseEvent event = new ViewportMouseEvent(e, diff, scaleDPI, camera);
		invokeViewportMouseDragEvent(event);
		if (event.handled)
			return;
	}

	/**
	 * Callback method invoked through mouse wheel scrolling.
	 * Unless hijacked by a listener, the camera's zoom level is changed, 
	 * while keeping the position under the mouse cursor constant.
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) 
	{
		ViewportMouseEvent event = new ViewportMouseEvent(e, new Vector2(),scaleDPI, camera);
		invokeViewportMouseScrollEvent(event);
		if (event.handled)
			return;
		
		// Zoom in or out while keeping the same point under the mouse pointer
		//camera.zoomBy(event.positionRay, panel.getWidth(),panel.getHeight(),event.delta);
		if(event.delta>0 && zoomFactor<20.0f)
			zoomFactor+=0.05f;
		else if(event.delta<0 && zoomFactor>.0f)
			zoomFactor-=0.05f;
		demo.zoomMatrix = Matrix4.projectionOrthogonal(zoomFactor,zoomFactor,1,-1);
		redraw(false);
	}
	
	// General events:
	
	public void addViewportEventListener(ViewportEventListener listener)
	{
		viewportEventListeners.add(listener);
	}
	
	public void removeViewportEventListener(ViewportEventListener listener)
	{
		viewportEventListeners.remove(listener);
	}
	
	private void invokeViewportInitializeEvent(GLAutoDrawable drawable)
	{
		for (ViewportEventListener listener : viewportEventListeners)
			listener.viewportInitialize(drawable);
	}
	
	private void invokeViewportReshapeEvent(GLAutoDrawable drawable, ViewportResizedEvent e)
	{
		for (ViewportEventListener listener : viewportEventListeners)
			listener.viewportReshape(drawable, e);
	}
	
	private void invokeViewportDisplayEvent(GLAutoDrawable drawable)
	{
		for (ViewportEventListener listener : viewportEventListeners)
			listener.viewportDisplay(drawable);
	}
	
	private void invokeViewportDisposeEvent(GLAutoDrawable drawable)
	{
		for (ViewportEventListener listener : viewportEventListeners)
			listener.viewportDispose(drawable);
	}
	
	// Mouse events:
	
	public void addViewportMouseEventListener(ViewportMouseEventListener listener)
	{
		viewportMouseEventListeners.add(listener);
	}
	
	public void removeViewportMouseEventListener(ViewportMouseEventListener listener)
	{
		viewportMouseEventListeners.remove(listener);
	}
	
	private void invokeViewportMouseDownEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseDown(e);
	}
	
	private void invokeViewportMouseEnterEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseEnter(e);
	}
	
	private void invokeViewportMouseLeaveEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseLeave(e);
	}
	
	private void invokeViewportMouseUpEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseUp(e);
	}
	
	private void invokeViewportMouseMoveEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseMove(e);
	}
	
	private void invokeViewportMouseDragEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseDrag(e);
	}
	
	private void invokeViewportMouseScrollEvent(ViewportMouseEvent e)
	{
		for (ViewportMouseEventListener listener : viewportMouseEventListeners)
			listener.viewportMouseScroll(e);
	}
}