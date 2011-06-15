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
package es.axios.udig.ui.spatialoperations.tasks;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.measure.quantity.Quantity;
import javax.measure.unit.BaseUnit;

import org.geotools.data.FeatureStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.SchemaException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.MultiPolygon;

import es.axios.udig.spatialoperations.tasks.IBufferTask;
import es.axios.udig.spatialoperations.tasks.IBufferTask.CapStyle;
import es.axios.udig.spatialoperations.tasks.SpatialOperationFactory;
import es.axios.udig.ui.spatialoperations.ShapefileUtil.ShapeReader;

/**
 * <p>
 * Test class for BufferTask operation.
 * 
 * <pre>
 * 
 * Common schema of any test:
 * 
 * public void Test (){
 * 
 * 		-initialize the parameters
 * 
 * 		 initTaskParameters();
 * 
 * 		-execute
 * 
 * 		 runTask();
 * 
 * 		-obtain the result and check the data.
 * }
 * 
 * </pre>
 * 
 * </p>
 * 
 * @author Aritz Davila (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @author Alain Jimeno (www.axios.es)
 */
public class BufferTaskTest extends AbstractTaskTest {

	private String				firstShp			= "";
	private String				targetShp			= "";
	private String				newShp				= "";

	private Boolean				isCreatingNewLayer	= null;

	private IBufferTask			task				= null;
	private SimpleFeatureStore		targetStore			= null;

	/* Buffer parameters. */
	private javax.measure.unit.Unit<Quantity>		unitOfMeasure		= null;
	private Boolean				mergeGeometries		= null;
	private Integer				quadrantSegments	= null;
	private Double				width				= null;
	private CapStyle			capStyle			= null;

	private static final String	TASK_PATH			= "BufferTask/";

	@Override
	protected Callable<FeatureStore<SimpleFeatureType,SimpleFeature>> getCurrentTask() {

		return task;
	}

	@Override
	protected void initTaskParameters() throws IOException, SchemaException {

		reader = new ShapeReader();

		SimpleFeatureCollection selectedFeatures = reader.getFeatures(PATH + firstShp);
		// Don't have Map so, use the crs of the source shapefile.
		final CoordinateReferenceSystem sourceCRS = reader.getCRS(PATH + firstShp);
		final CoordinateReferenceSystem targetCrs = reader.getCRS(PATH + targetShp);
		final CoordinateReferenceSystem mapCrs = sourceCRS;
		
		if (isCreatingNewLayer) {
			SimpleFeatureType type = (SimpleFeatureType) reader.getFeatureType(PATH + firstShp);
			targetStore = createNewTargetStore(PATH + newShp, type);
		} else {
			targetStore = (SimpleFeatureStore) createTargetStore(PATH + targetShp);
		}
		IBufferTask task = SpatialOperationFactory.createBuffer(selectedFeatures, targetStore, mapCrs, width,
					unitOfMeasure, mergeGeometries, quadrantSegments, capStyle, sourceCRS, targetCrs);

		this.task = task;
	}

	/**
	 * Creates a buffer from the source shapefile MultiPolygon2 and put the
	 * result on the existent shapefile TargetMultiPolygonForBuffer.
	 * 
	 * Check that the featureStore isn't empty and the geometry of the resultant
	 * features is MultiPolygon.
	 * 
	 * @throws IOException
	 * @throws SchemaException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testBufferTask() throws IOException, SchemaException, InterruptedException, ExecutionException {

		// initialize the parameters

		firstShp = "MultiPolygon2.shp";
		targetShp = TASK_PATH + "TargetMultiPolygonForBuffer.shp";

		isCreatingNewLayer = false;

		width = 10.0;

		unitOfMeasure = valueOfUnit("m");
		
		mergeGeometries = false;
		quadrantSegments = 8;
		capStyle = CapStyle.capRound;

		initTaskParameters();

		assertNotNull(task);
		assertNotNull(targetStore);

		// execute

		runTask();

		// obtain the result and check the data.

		SimpleFeatureStore resultStore = (SimpleFeatureStore) future.get();

		assertNotNull(resultStore);

		SimpleFeatureCollection fc = null;
		SimpleFeatureIterator it = null;

		try {
			fc = resultStore.getFeatures();

			assertFalse(fc.isEmpty());
			assertTrue(fc.size() > 0);

			it = fc.features();
			while (it.hasNext()) {

				SimpleFeature f = it.next();
				assertTrue(f.getDefaultGeometry().getClass() == MultiPolygon.class);
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (it != null) {
				it.close();
			}
		}

	}

	@BeforeClass
	public  void beforeInitResultShapeFile(){
	
		//TODO initialize the result shapefile 
		
	}
	
	/**
	 * Creates a buffer from the source shapefile MultiPolygon2, merge the
	 * resultant features and put them on the existent shapefile
	 * TargetMultiPolygonForBufferMerge.
	 * 
	 * Check that the featureStore isn't empty, the result only have 1 feature
	 * and the geometry of the resultant features is MultiPolygon.
	 * 
	 * @throws IOException
	 * @throws SchemaException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testBufferTaskMerge() throws IOException, SchemaException, InterruptedException, ExecutionException {

		// initialize the parameters

		firstShp = "MultiPolygon2.shp";
		targetShp = TASK_PATH + "TargetMultiPolygonForBufferMerge.shp";

		isCreatingNewLayer = false;

		width = 10.0;
		unitOfMeasure = valueOfUnit("m");
		mergeGeometries = true;
		quadrantSegments = 8;
		capStyle = CapStyle.capFlat;

		initTaskParameters();

		assertNotNull(task);
		assertNotNull(targetStore);

		// execute

		runTask();

		// obtain the result and check the data.

		SimpleFeatureStore resultStore = (SimpleFeatureStore) future.get();

		assertNotNull(resultStore);

		SimpleFeatureCollection fc = null;
		SimpleFeatureIterator it = null;

		try {
			fc = resultStore.getFeatures();

			assertFalse(fc.isEmpty());
			assertTrue(fc.size() == 1);

			it = fc.features();
			while (it.hasNext()) {

				SimpleFeature f = it.next();
				assertTrue(f.getDefaultGeometry().getClass() == MultiPolygon.class);
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (it != null) {
				it.close();
			}
		}

	}

	private javax.measure.unit.Unit<Quantity> valueOfUnit(String value) {
		javax.measure.unit.Unit<? extends Quantity> units = BaseUnit.valueOf(value); 
		return (javax.measure.unit.Unit<Quantity>) units;
	}

	/**
	 * Creates a buffer from the source shapefile MultiPolygon2 and put the
	 * result on the new shapefile newTargetBuffer.
	 * 
	 * Check that the featureStore isn't empty and the geometry of the resultant
	 * features is MultiPolygon.
	 * 
	 * @throws IOException
	 * @throws SchemaException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testBufferTaskInNewLayer()
		throws IOException, SchemaException, InterruptedException, ExecutionException {

		// initialize the parameters

		firstShp = "MultiPolygon2.shp";
		newShp = TASK_PATH + "newTargetBuffer";

		isCreatingNewLayer = true;

		width = 10.0;
		unitOfMeasure = valueOfUnit("m");
		mergeGeometries = false;
		quadrantSegments = 8;
		capStyle = CapStyle.capFlat;

		initTaskParameters();

		assertNotNull(task);
		assertNotNull(targetStore);

		// execute

		runTask();

		// obtain the result and check the data.

		SimpleFeatureStore resultStore = (SimpleFeatureStore) future.get();

		assertNotNull(resultStore);

		SimpleFeatureCollection fc = null;
		SimpleFeatureIterator it = null;

		try {
			fc = resultStore.getFeatures();

			assertFalse(fc.isEmpty());
			assertTrue(fc.size() > 0);

			it = fc.features();
			while (it.hasNext()) {

				SimpleFeature f = it.next();
				assertTrue(f.getDefaultGeometry().getClass() == MultiPolygon.class);
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (it != null) {
				it.close();
			}
		}

	}

	/**
	 * Creates a buffer from the source shapefile MultiPolygon2, merge the
	 * resultant features and put them on the new shapefile
	 * newTargetBufferMerge.
	 * 
	 * Check that the featureStore isn't empty, the result only have 1 feature
	 * and the geometry of the resultant features is MultiPolygon.
	 * 
	 * @throws IOException
	 * @throws SchemaException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@Test
	public void testBufferTaskInNewLayerMerge()
		throws IOException, SchemaException, InterruptedException, ExecutionException {

		// initialize the parameters

		firstShp = "MultiPolygon2.shp";
		newShp = TASK_PATH + "newTargetBufferMerge";

		isCreatingNewLayer = true;

		width = 10.0;
		unitOfMeasure = valueOfUnit("m");
		mergeGeometries = true;
		quadrantSegments = 8;
		capStyle = CapStyle.capSquare;

		initTaskParameters();

		assertNotNull(task);
		assertNotNull(targetStore);

		// execute

		runTask();

		// obtain the result and check the data.

		SimpleFeatureStore resultStore = (SimpleFeatureStore) future.get();

		assertNotNull(resultStore);

		SimpleFeatureCollection fc = null;
		SimpleFeatureIterator it = null;

		try {
			fc = resultStore.getFeatures();

			assertFalse(fc.isEmpty());
			assertTrue(fc.size() == 1);

			it = fc.features();
			while (it.hasNext()) {

				SimpleFeature f = it.next();
				assertTrue(f.getDefaultGeometry().getClass() == MultiPolygon.class);
			}
		}
		catch (IOException e) {
			throw e;
		}
		finally {
			if (it != null) {
				it.close();
			}
		}

	}

}
