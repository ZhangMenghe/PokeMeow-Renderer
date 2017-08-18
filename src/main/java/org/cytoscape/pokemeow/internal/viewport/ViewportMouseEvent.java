package org.cytoscape.pokemeow.internal.viewport;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import org.cytoscape.pokemeow.internal.algebra.Ray3;
import org.cytoscape.pokemeow.internal.algebra.Vector2;
import org.cytoscape.pokemeow.internal.camera.Camera;


/**
 * Mouse action-related events raised by a viewport.
 */
public class ViewportMouseEvent 
{
	public final boolean m1;	// Left button
	public final boolean m2;	// Right button
	public final boolean m3;	// Wheel button
	
	public final boolean keyShift;	// Shift key pressed
	public final boolean keyCtrl;	// Ctrl key pressed
	public final boolean keyAlt;	// Alt key pressed
	
	public final Vector2 offsetRaw;		// Mouse movement in raw pixels, for drag event
	public final Vector2 offsetScaled;	// Mouse movement considering DPI scaling
	public final int delta;				// Wheel scrolling
	
	public final Vector2 positionRaw;	// Mouse position in raw pixels
	public final Vector2 positionScaled;// Mouse position considering DPI scaling
	public final Ray3 positionRay;		// Ray originating at camera position, going through the mouse position, normalized
	
	public boolean handled = false;	// Indicates that the event was handled by one of the subscribers

	public ViewportMouseEvent (MouseEvent e, Vector2 offset, float scaleDPI, Camera camera)
	{
		this.m1 = (e.getButton() & MouseEvent.BUTTON1) > 0;
		this.m2 = (e.getButton() & MouseEvent.BUTTON2) > 0;
		this.m3 = (e.getButton() & MouseEvent.BUTTON3) > 0;
		
		this.keyShift = e.isShiftDown();
		this.keyCtrl = e.isControlDown();
		this.keyAlt = e.isAltDown();
		
		this.offsetRaw = offset;
		this.offsetScaled = Vector2.scalarMult(scaleDPI, offset);

		if(e instanceof MouseWheelEvent)
			this.delta = ((MouseWheelEvent) e).getWheelRotation();
		else
			this.delta = 0;
		this.positionRaw = new Vector2(e.getX(), e.getY());
		this.positionScaled = Vector2.scalarMult(scaleDPI, positionRaw);
		this.positionRay = camera.getRayThroughPixel(positionRaw);
	}

}