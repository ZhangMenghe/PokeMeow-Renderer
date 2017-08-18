package org.cytoscape.pokemeow.internal.viewport;

import org.cytoscape.pokemeow.internal.algebra.Vector2;

/**
 * Event raised upon resizing a viewport.
 */
public class ViewportResizedEvent 
{
	public final Vector2 newRawSize;	// New size in device pixels
	public final Vector2 newScaledSize;	// New size in scaled pixels, i. e. considering the system's DPI settings
	

	public ViewportResizedEvent (Vector2 newRawSize, Vector2 newScaledSize) 
	{
		this.newRawSize = newRawSize;
		this.newScaledSize = newScaledSize;
	}
}