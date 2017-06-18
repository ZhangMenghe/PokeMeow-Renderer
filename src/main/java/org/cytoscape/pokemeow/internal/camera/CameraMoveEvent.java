package main.java.org.cytoscape.pokemeow.internal.camera;

import main.java.org.cytoscape.pokemeow.internal.algebra.Matrix4;

/**
 * Event raised upon a change in camera parameters (most likely movement).
 */
public class CameraMoveEvent 
{
	public final CameraConfiguration oldConfiguration;	// Old camera settings
	public final CameraConfiguration newConfiguration;	// New camera settings
	public final Matrix4 matrixViewProj;				// New (view * projection) matrix
	public final boolean isWithinPlane;					// Only pan movement
	

	public CameraMoveEvent (CameraConfiguration oldConfiguration, CameraConfiguration newConfiguration, Matrix4 matrixViewProj, boolean isWithinPlane) 
	{
		this.oldConfiguration = oldConfiguration;
		this.newConfiguration = newConfiguration;
		this.matrixViewProj = matrixViewProj;
		this.isWithinPlane = isWithinPlane;
	}
}