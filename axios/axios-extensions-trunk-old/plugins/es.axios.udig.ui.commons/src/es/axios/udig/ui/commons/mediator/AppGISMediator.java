/* Spatial Operations & Editing Tools for uDig
 * 
 * Axios Engineering under a funding contract with: 
 *      Diputación Foral de Gipuzkoa, Ordenación Territorial 
 *
 *      http://b5m.gipuzkoa.net
 *      http://www.axios.es 
 *
 * (C) 2006, Diputación Foral de Gipuzkoa, Ordenación Territorial (DFG-OT). 
 * DFG-OT agree to licence under Lesser General Public License (LGPL).
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
package es.axios.udig.ui.commons.mediator;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.refractions.udig.catalog.CatalogPlugin;
import net.refractions.udig.catalog.ICatalog;
import net.refractions.udig.catalog.IGeoResource;
import net.refractions.udig.project.IEditManager;
import net.refractions.udig.project.ILayer;
import net.refractions.udig.project.IMap;
import net.refractions.udig.project.command.factory.EditCommandFactory;
import net.refractions.udig.project.ui.ApplicationGIS;
import net.refractions.udig.project.ui.BusyIndicator;
import net.refractions.udig.project.ui.render.displayAdapter.ViewportPane;

import org.geotools.data.FeatureStore;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Mediator to udig
 * <p>
 * Implements util functions for interacting with the uDig Facades
 * </p>
 * TODO rename as Adapter
 * 
 * @author Mauricio Pazos (www.axios.es)
 * @author Gabriel Roldan (www.axios.es)
 * @since 1.1.0
 */
public final class AppGISMediator {

    private AppGISMediator() {
        // util class
    }


    /**
     * Delegate in ApplicationGIS.addLayersToMap
     * 
     * 
     *
     * @param map
     * @param resources
     * @param index
     * @return
     */
    public static List< ? extends ILayer> addLayersToMap( IMap map, List<IGeoResource> resources,
                                                          int index ) {
        return ApplicationGIS.addLayersToMap(map, resources, index);
    }

    /**
     * @return List of layers displayed on active map.If active map is null return
     *         <code>Collections.EMPTY_LIST<code>.
     */
    public static Set<ILayer> getActiveMapLayers() {

        IMap map = ApplicationGIS.getActiveMap();

        return getMapLayers(map);

    }

    /**
     * Layers sorted by name
     * 
     * FIXME The result is not ordered by Name (check the LayerImp.CompareTo)
     * 
     * @return a Sorted Set of layers displayed on active map, if map is null return
     *         <code>LayerUtil.EMPTY_LIST<code>.
     */
    public static Set<ILayer> getMapLayers( IMap map ) {

        if (map == null) {
            return Collections.emptySet();
        }

        List<ILayer> list = map.getMapLayers();

        // Does the filter of layers that can be resolved as feature store
        Set<ILayer> listLayerWithName = new TreeSet<ILayer>();
        for( ILayer layer : list ) {
            
            if ( canResolveFeautreStore (layer) && (layer.getName() != null) ){
                listLayerWithName.add(layer);
            }
            
            
        }

        return listLayerWithName;

    }
    /**
     *
     * @param layer
     * @return true if can resolv to FeatureStore
     */
    private static boolean canResolveFeautreStore( final ILayer layer ) {


        IGeoResource resource = layer.getGeoResource();

        boolean retValue = resource.canResolve(FeatureStore.class);
        
        return retValue;
    }

    /**
     * @return the active map;
     */
    public static IMap getActiveMap() {

        IMap map = ApplicationGIS.getActiveMap();

        return map;
    }

    public static ICatalog getCatalog() {

        return CatalogPlugin.getDefault().getLocalCatalog();
    }

    /**
     * Creates a georesource for the indeed feature type
     * 
     * @param featureType
     * @return a new geo resource
     */
    public static IGeoResource createTempGeoResource( final SimpleFeatureType featureType ) {

        assert featureType != null;

        // new resourece is required because new layer was selected
        final ICatalog catalog = AppGISMediator.getCatalog();
        assert catalog != null;
        
        IGeoResource resource = catalog.createTemporaryResource(featureType);

        return resource;
    }

    /**
     * @return the selected layer
     */
    public static ILayer getSelectedLayer() {

        IMap map = AppGISMediator.getActiveMap();
        if (map == null)
            return null;

        IEditManager edit = map.getEditManager();
        if (edit == null)
            return null;

        ILayer layer = edit.getSelectedLayer();
        if (layer == null)
            return null;
        if (layer.getName() == null)
            return null;

        return layer;
    }

    /**
     * Returns an instance (singleton object) of EditCommandFactory
     * 
     * @return EditCommandFactory
     */
    public static EditCommandFactory getEditCommandFactory() {
        return EditCommandFactory.getInstance();
    }

    /**
     * Runs the given <code>runnable</code> *synchronously* in the display thread while showing a
     * busy indicator (wait cursor) on the {@link ViewportPane pane}
     * 
     * @param pane
     * @param runnable
     */
    public static void showWhile( ViewportPane pane, Runnable runnable ) {
        BusyIndicator.showWhile(pane, runnable);
    }
    

}
