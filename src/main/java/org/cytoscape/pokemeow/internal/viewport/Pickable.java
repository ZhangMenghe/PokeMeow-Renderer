package org.cytoscape.pokemeow.internal.viewport;

import org.cytoscape.pokemeow.internal.algebra.*;

/**
 * An object that can be picked with a mouse cursor by intersecting it with a ray in 3D space.
 * Intend to implement ray tracer
 */
public interface Pickable 
{
	/**
	 * Prompts the object to test for intersections with a ray.
	 * 
	 * @param ray Ray, typically going from the camera through the mouse pointer
	 * @param viewMatrix Current view matrix 
	 * @return PickingResult object if there is an intersection, null otherwise
	 */
	public PickingResult intersectsWith(Ray3 ray, Matrix4 viewMatrix);
}