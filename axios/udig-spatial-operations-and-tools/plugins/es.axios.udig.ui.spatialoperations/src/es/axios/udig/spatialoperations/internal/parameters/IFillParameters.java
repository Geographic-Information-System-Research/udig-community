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
package es.axios.udig.spatialoperations.internal.parameters;

import net.refractions.udig.project.ILayer;

import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Parameters for Fill Operation.
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @author Alain Jimeno (www.axios.es)
 * @since 1.2.0
 */
public interface IFillParameters {

	/**
	 * @return Returns the firstLayer.
	 */
	public ILayer getFirstLayer();

	/**
	 * @return Returns the first layer CRS.
	 */
	public CoordinateReferenceSystem getFirstLayerCRS();

	/**
	 * @return Returns the secondLayer.
	 */
	public ILayer getSecondLayer();

	/**
	 * @return Returns the second layer CRS.
	 */
	public CoordinateReferenceSystem getSecondLayerCRS();

	/**
	 * @return Returns the FilterInFirstLayer.
	 */
	public Filter getFilterInFirstLayer();

	/**
	 * @return Returns the FilterInSecondLayer.
	 */
	public Filter getFilterInSecondLayer();
	
	/**
	 * @return true if the process requires copy the source features in the target layer
	 */
	public boolean isCopySourceFeatures();
	

}
