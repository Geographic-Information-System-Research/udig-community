/* Spatial Operations & Editing Tools for uDig
 * 
 * Axios Engineering under a funding contract with: 
 *      Diputación Foral de Gipuzkoa, Ordenación Territorial 
 *
 *      http://b5m.gipuzkoa.net
 *      http://www.axios.es 
 *
 * (C) 2006, Diputación Foral de Gipuzkoa, Ordenación Territorial (DFG-OT). 
 * DFG-OT agrees to licence under Lesser General Public License (LGPL).
 * 
 * You can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software 
 * Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package es.axios.udig.spatialoperations.internal.ui.parameters.split;

import org.eclipse.swt.graphics.Image;

import es.axios.udig.spatialoperations.ui.parameters.AbstractImagesOperation;
import es.axios.udig.spatialoperations.ui.parameters.LayersCheckState;

/**
 * Stores the different split images and show the correspondent image.
 * 
 * TODO for each option add getters on the command and implement the logical
 * here
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 1.2.0
 */
final class SplitImages extends AbstractImagesOperation {

	private static final String	SPLIT_SOURCE	= "SplitSource";	//$NON-NLS-1$
	private static final String	SPLIT_RESULT	= "SplitResult";	//$NON-NLS-1$
	private static final String	SPLIT_FINAL		= "SplitFinal";	//$NON-NLS-1$


	private Image				currentImage	= null;

	private LayersCheckState	currentState	= null;

	public SplitImages() {

		super();
	}


	@Override
	public Image getDefaultImage() {

		this.currentImage = getImage(SPLIT_FINAL);
		return currentImage;
	}

	@Override
	public Image getImage(Boolean chkSource, Boolean chkResult) {

		this.currentState = (setCurrentLayerCheckState(chkSource, chkResult));

		if (this.currentState == null) {
			// no check selected = no image will display
			return null;
		}

		// TODO implement check for each option, and finally will return the
		// correspondent image.
		switch (this.currentState) {
		case Source:
			this.currentImage = getImage(SPLIT_SOURCE);
			break;
		case Result:
			this.currentImage = getImage(SPLIT_RESULT);
			break;
		case Final:
			this.currentImage = getImage(SPLIT_FINAL);
			break;
		default:
			assert false : "Error"; //$NON-NLS-1$
			break;
		}

		assert currentImage != null : "currentImage couldn't be null"; //$NON-NLS-1$

		return currentImage;
	}

	/**
	 * Creates the imageregistry where saves all the images.
	 * 
	 * @return
	 */
	@Override
	protected void createImageRegistry() {

		addImageToRegistry(SPLIT_SOURCE, SplitImages.class);
		addImageToRegistry(SPLIT_RESULT, SplitImages.class);
		addImageToRegistry(SPLIT_FINAL, SplitImages.class);
	}

}
