/* (c) 2014-2015 Open Source Geospatial Foundation - all rights reserved
 * (c) 2001 - 2013 OpenPlans
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.catalog;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;

import javax.media.jai.PlanarImage;
import javax.xml.namespace.QName;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.io.FileUtils;
import org.geoserver.catalog.util.ReaderUtils;
import org.geoserver.config.GeoServer;
import org.geoserver.config.GeoServerDataDirectory;
import org.geoserver.config.GeoServerInfo;
import org.geoserver.data.test.MockData;
import org.geoserver.data.test.SystemTestData;
import org.geoserver.data.test.TestData;
import org.geoserver.platform.GeoServerEnvironment;
import org.geoserver.platform.GeoServerExtensions;
import org.geoserver.test.GeoServerSystemTestSupport;
import org.geoserver.test.RunTestSetup;
import org.geoserver.test.SystemTest;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.StructuredGridCoverage2DReader;
import org.geotools.data.DataAccess;
import org.geotools.data.DataUtilities;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.GeoTools;
import org.geotools.feature.NameImpl;
import org.geotools.ows.ServiceException;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.styling.AbstractStyleVisitor;
import org.geotools.styling.Mark;
import org.geotools.styling.PolygonSymbolizer;
import org.geotools.styling.Style;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.Version;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.style.ExternalGraphic;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Tests for {@link ResourcePool}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
@Category(SystemTest.class)
public class ResourcePoolTest extends GeoServerSystemTestSupport {
    
    private static final String HUMANS = "humans";

    static {
        System.setProperty("ALLOW_ENV_PARAMETRIZATION", "true");
    }
    
    private static File rockFillSymbolFile;

    protected static QName TIMERANGES = new QName(MockData.SF_URI, "timeranges", MockData.SF_PREFIX);
    
    private static final String EXTERNAL_ENTITIES = "externalEntities";
    
    @Override
    protected void onSetUp(SystemTestData testData) throws Exception {
        super.onSetUp(testData);
        
        testData.addStyle("relative", "se_relativepath.sld", ResourcePoolTest.class, getCatalog());
        testData.addStyle("relative_protocol", "se_relativepath_protocol.sld",
                ResourcePoolTest.class, getCatalog());
        testData.addStyle(HUMANS, "humans.sld", ResourcePoolTest.class, getCatalog());
        testData.addStyle(EXTERNAL_ENTITIES, "externalEntities.sld", TestData.class, getCatalog());
        StyleInfo style = getCatalog().getStyleByName("relative");
        style.setFormatVersion(new Version("1.1.0"));
        getCatalog().save(style);
        style = getCatalog().getStyleByName(HUMANS);
        style.setFormatVersion(new Version("1.1.0"));
        getCatalog().save(style);
        File images = new File(testData.getDataDirectoryRoot(), "styles/images");
        assertTrue(images.mkdir());
        File image = new File("./src/test/resources/org/geoserver/catalog/rockFillSymbol.png");
        assertTrue(image.exists());
        FileUtils.copyFileToDirectory(image, images);
        rockFillSymbolFile = new File(images, image.getName()).getCanonicalFile();
        
        testData.addRasterLayer(TIMERANGES, "timeranges.zip", null, null, SystemTestData.class, getCatalog());
        
        FileUtils.copyFileToDirectory(new File("./src/test/resources/geoserver-environment.properties"), 
                testData.getDataDirectoryRoot());
    }

    @Override
    protected void setUpTestData(SystemTestData testData) throws Exception {
        super.setUpTestData(testData);
        testData.setUpWcs11RasterLayers();
    }

    /**
     * Test that the {@link FeatureType} cache returns the same instance every time. This is assumed
     * by some nasty code in other places that tampers with the CRS. If a new {@link FeatureType} is
     * constructed for the same {@link FeatureTypeInfo}, Bad Things Happen (TM).
     */
    @Test public void testFeatureTypeCacheInstance() throws Exception {
        ResourcePool pool = ResourcePool.create(getCatalog());
        FeatureTypeInfo info = getCatalog().getFeatureTypeByName(
                MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        FeatureType ft1 = pool.getFeatureType(info);
        FeatureType ft2 = pool.getFeatureType(info);
        FeatureType ft3 = pool.getFeatureType(info);
        assertSame(ft1, ft2);
        assertSame(ft1, ft3);
    }
    
    @Test public void testAttributeCache() throws Exception {
        final Catalog catalog = getCatalog();
        ResourcePool pool = ResourcePool.create(catalog);
        
        // clean up the lakes type
        FeatureTypeInfo oldInfo = catalog.getFeatureTypeByName(
                MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        List<LayerInfo> layers = catalog.getLayers(oldInfo);
        for (LayerInfo layerInfo : layers) {
            catalog.remove(layerInfo);
        }
        catalog.remove(oldInfo);
        
        // rebuild as new
        CatalogBuilder builder = new CatalogBuilder(catalog);
        builder.setStore(catalog.getStoreByName(MockData.CITE_PREFIX, MockData.CITE_PREFIX, DataStoreInfo.class));
        FeatureTypeInfo info = builder.buildFeatureType(new NameImpl(MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart()));
        
        // non persisted state, caching should not occurr
        List<AttributeTypeInfo> att1 = pool.getAttributes(info);
        List<AttributeTypeInfo> att2 = pool.getAttributes(info);
        assertNotSame(att1, att2);
        assertEquals(att1, att2);
        
        // save it, making it persistent
        catalog.add(info);
        
        // first check caching actually works against persisted type infos
        List<AttributeTypeInfo> att3 = pool.getAttributes(info);
        List<AttributeTypeInfo> att4 = pool.getAttributes(info);
        assertSame(att3, att4);
        assertNotSame(att1, att3);
        assertEquals(att1, att3);
    }
    
    boolean cleared = false;
    @Test public void testCacheClearing() throws IOException {
        cleared = false;
        ResourcePool pool = new ResourcePool(getCatalog()) {
            @Override
            public void clear(FeatureTypeInfo info) {
                cleared = true;
                super.clear(info);
            }
        };
        FeatureTypeInfo info = getCatalog().getFeatureTypeByName(
                MockData.LAKES.getNamespaceURI(), MockData.LAKES.getLocalPart());
        
        assertNotNull( pool.getFeatureType( info ) );
        info.setTitle("changed");
        
        assertFalse( cleared );
        getCatalog().save( info );
        assertTrue( cleared );
        
        cleared = false;
        assertNotNull( pool.getFeatureType( info ) );
        
        for ( LayerInfo l : getCatalog().getLayers( info ) ) {
            getCatalog().remove( l );
        }
        getCatalog().remove( info );
        assertTrue( cleared );
    }

    boolean disposeCalled;

    /**
     * Make sure {@link ResourcePool#clear(DataStoreInfo)} and {@link ResourcePool#dispose()} call
     * {@link DataAccess#dispose()}
     */
    @Test public void testDispose() throws IOException {
        disposeCalled = false;
        class ResourcePool2 extends ResourcePool {
            @SuppressWarnings("serial")
            public ResourcePool2(Catalog catalog) {
                super(catalog);
                dataStoreCache = new DataStoreCache() {
                    @SuppressWarnings("unchecked")
                    @Override
                    protected void dispose(String name, DataAccess dataStore) {
                        disposeCalled = true;
                        super.dispose(name, dataStore);
                    }
                };
            }
        }

        Catalog catalog = getCatalog();
        ResourcePool pool = new ResourcePool2(catalog);
        catalog.setResourcePool(pool);

        DataStoreInfo info = catalog.getDataStores().get(0);
        // force the datastore to be created
        DataAccess<? extends FeatureType, ? extends Feature> dataStore = pool.getDataStore(info);
        assertNotNull(dataStore);
        assertFalse(disposeCalled);
        pool.clear(info);
        assertTrue(disposeCalled);

        // force the datastore to be created
        dataStore = pool.getDataStore(info);
        assertNotNull(dataStore);
        disposeCalled = false;
        pool.dispose();
        assertTrue(disposeCalled);
    }

    @Test public void testConfigureFeatureTypeCacheSize() {
        GeoServer gs = getGeoServer();
        GeoServerInfo global = gs.getGlobal();
        global.setFeatureTypeCacheSize(200);
        gs.save(global);

        Catalog catalog = getCatalog();
        // we actually keep two versions of the feature type in the cache, so we need it 
        // twice as big
        assertEquals(400, ((SoftValueHashMap)catalog.getResourcePool().getFeatureTypeCache()).getHardReferencesCount());
    }
    
    @Test public void testDropCoverageStore() throws Exception {
        // build the store
        Catalog cat = getCatalog();
        CatalogBuilder cb = new CatalogBuilder(cat);
        CoverageStoreInfo store = cb.buildCoverageStore("dem");
        store.setURL(MockData.class.getResource("tazdem.tiff").toExternalForm());
        store.setType("GeoTIFF");
        cat.add(store);
        
        // build the coverage
        cb.setStore(store);
        CoverageInfo ci = cb.buildCoverage();
        cat.add(ci);
        
        // build the layer
        LayerInfo layer = cb.buildLayer(ci);
        cat.add(layer);
        
        // grab a reader just to inizialize the code
        ci.getGridCoverage(null, null);
        ci.getGridCoverageReader(null, GeoTools.getDefaultHints());
        
        // now drop the store
        CascadeDeleteVisitor visitor = new CascadeDeleteVisitor(cat);
        visitor.visit(store);
        
        // and reload (GEOS-4782 -> BOOM!)
        getGeoServer().reload();
        
    }

    @RunTestSetup
    @Test public void testGeoServerReload() throws Exception {
        Catalog cat = getCatalog();
        FeatureTypeInfo lakes = cat.getFeatureTypeByName(MockData.LAKES.getNamespaceURI(),
                MockData.LAKES.getLocalPart());
        assertFalse("foo".equals(lakes.getTitle()));

        GeoServerDataDirectory dd = new GeoServerDataDirectory(getResourceLoader());
        File info = dd.findResourceFile(lakes);
        //File info = getResourceLoader().find("featureTypes", "cite_Lakes", "info.xml");
        
        FileReader in = new FileReader(info);
        Element dom = ReaderUtils.parse(in); 
        Element title = ReaderUtils.getChildElement(dom, "title");
        title.getFirstChild().setNodeValue("foo");
        
        OutputStream output = new FileOutputStream(info);
        try {
            TransformerFactory.newInstance().newTransformer()
                    .transform(new DOMSource(dom), new StreamResult(output));
        } finally {
            output.close();
        }
        
        getGeoServer().reload();
        lakes = cat.getFeatureTypeByName(MockData.LAKES.getNamespaceURI(),
                MockData.LAKES.getLocalPart());
        assertEquals("foo", lakes.getTitle());
    }
    
    @Test
    public void testSEStyleWithRelativePath() throws IOException {
        StyleInfo si = getCatalog().getStyleByName("relative");

        assertNotNull(si);
        Style style = si.getStyle();
        PolygonSymbolizer ps = (PolygonSymbolizer) style.featureTypeStyles().get(0).rules().get(0).symbolizers().get(0);
        ExternalGraphic eg = (ExternalGraphic) ps.getFill().getGraphicFill().graphicalSymbols().get(0);
        URI uri = eg.getOnlineResource().getLinkage();
        assertNotNull(uri);
        File actual = DataUtilities.urlToFile(uri.toURL()).getCanonicalFile();
        assertEquals(rockFillSymbolFile, actual);
    }
    
    @Test
    public void testSEStyleWithRelativePathProtocol() throws IOException {
        StyleInfo si = getCatalog().getStyleByName("relative_protocol");

        assertNotNull(si);
        Style style = si.getStyle();
        PolygonSymbolizer ps = (PolygonSymbolizer) style.featureTypeStyles().get(0).rules().get(0)
                .symbolizers().get(0);
        ExternalGraphic eg = (ExternalGraphic) ps.getFill().getGraphicFill().graphicalSymbols()
                .get(0);
        URI uri = eg.getOnlineResource().getLinkage();
        assertNotNull(uri);
        File actual = DataUtilities.urlToFile(uri.toURL()).getCanonicalFile();
        assertEquals(rockFillSymbolFile, actual);
    }

    @Test
    public void testPreserveStructuredReader() throws IOException {
        // we have to make sure time ranges native name is set to trigger the bug in question
        CoverageInfo ci = getCatalog().getCoverageByName(getLayerId(TIMERANGES));
        assertTrue(ci.getGridCoverageReader(null, null) instanceof StructuredGridCoverage2DReader);
        String name = ci.getGridCoverageReader(null, null).getGridCoverageNames()[0];
        ci.setNativeCoverageName(name);
        getCatalog().save(ci);
        
        ci = getCatalog().getCoverageByName(getLayerId(TIMERANGES));
        assertTrue(ci.getGridCoverageReader(null, null) instanceof StructuredGridCoverage2DReader);
    }

    @Test
    public void testMissingNullValuesInCoverageDimensions() throws IOException {
        CoverageInfo ci = getCatalog().getCoverageByName(getLayerId(MockData.TASMANIA_DEM));
        List<CoverageDimensionInfo> dimensions = ci.getDimensions();
        // legacy layers have no null value list
        dimensions.get(0).getNullValues().clear();
        getCatalog().save(ci);

        // and now go back and ask for the reader
        ci = getCatalog().getCoverageByName(getLayerId(MockData.TASMANIA_DEM));
        GridCoverageReader reader = ci.getGridCoverageReader(null, null);
        GridCoverage2D gc = null;
        try {
            // check that we maintain the native info if we don't have any
            gc = (GridCoverage2D) reader.read(null);
            assertEquals(-9999d, CoverageUtilities.getNoDataProperty(gc).getAsSingleValue(), 0d);
        } finally {
            if (gc != null) {
                RenderedImage ri = gc.getRenderedImage();
                if (gc instanceof GridCoverage2D) {
                    gc.dispose(true);
                }
                if (ri instanceof PlanarImage) {
                    ImageUtilities.disposePlanarImageChain((PlanarImage) ri);
                }
            }
        }
    }
    
    @RunTestSetup
    @Test public void testEnvParametrizationValues() throws Exception {
        
        final GeoServerEnvironment gsEnvironment = GeoServerExtensions.bean(GeoServerEnvironment.class);

        DataStoreInfo ds = getCatalog().getFactory().createDataStore();
        ds.getConnectionParameters().put("host", "${jdbc.host}");
        ds.getConnectionParameters().put("port", "${jdbc.port}");

        try {
            final String dsName = "GS-ENV-TEST-DS";
            ds.setName(dsName);

            getCatalog().save(ds);

            ds = getCatalog().getDataStoreByName(dsName);

            DataStoreInfo expandedDs = getCatalog().getResourcePool().clone(ds, true);

            assertTrue(ds.getConnectionParameters().get("host").equals("${jdbc.host}"));
            assertTrue(ds.getConnectionParameters().get("port").equals("${jdbc.port}"));
            
            if (GeoServerEnvironment.ALLOW_ENV_PARAMETRIZATION) {
                assertTrue(expandedDs.getConnectionParameters().get("host").equals(gsEnvironment.resolveValue("${jdbc.host}")));
                assertTrue(expandedDs.getConnectionParameters().get("port").equals(gsEnvironment.resolveValue("${jdbc.port}")));
            } else {
                assertTrue(expandedDs.getConnectionParameters().get("host").equals("${jdbc.host}"));
                assertTrue(expandedDs.getConnectionParameters().get("port").equals("${jdbc.port}"));                
            }
        } finally {
            getCatalog().remove(ds);
        }
    }
    
    @Test
    public void testCloneStoreInfo() throws Exception {
        Catalog catalog = getCatalog();
        
        DataStoreInfo source1 = catalog.getDataStores().get(0);
        DataStoreInfo clonedDs = catalog.getResourcePool().clone(source1, false);
        assertNotNull(source1);
        assertNotNull(clonedDs);
        
        assertEquals(source1, clonedDs);
        
        CoverageStoreInfo source2 = catalog.getCoverageStores().get(0);
        CoverageStoreInfo clonedCs = catalog.getResourcePool().clone(source2, false);
        assertNotNull(source2);
        assertNotNull(clonedCs);
        
        assertEquals(source2, clonedCs);
    }
    
    @Test
    public void testWmsCascadeEntityExpansion() throws Exception {
        //Other tests mess with or reset the resourcePool, so lets make it is initialized properly
        GeoServerExtensions.extensions(ResourcePoolInitializer.class).get(0).initialize(getGeoServer());
        
        ResourcePool rp = getCatalog().getResourcePool();
        
        WMSStoreInfo info = getCatalog().getFactory().createWebMapServer();
        URL url = getClass().getResource("1.3.0Capabilities-xxe.xml");
        info.setCapabilitiesURL(url.toExternalForm());
        info.setEnabled(true);
        // the connection pooling client does not support file references, disable it
        info.setUseConnectionPooling(false);
        try {
            rp.getWebMapServer(info);
            fail("WebMapServer instantiation should fail");
        } catch(IOException e) {
            assertThat(e.getCause(), instanceOf(ServiceException.class));
            ServiceException serviceException = (ServiceException) e.getCause();
            assertThat(serviceException.getMessage(), containsString("Error while parsing XML"));
            
            SAXException saxException = (SAXException) serviceException.getCause();
            Exception cause = saxException.getException();
            assertFalse("Expect external entity cause", cause != null && cause instanceof FileNotFoundException);
        }
        //make sure clearing the catalog does not clear the EntityResolver
        getGeoServer().reload();
        rp = getCatalog().getResourcePool();
        
        try {
            rp.getWebMapServer(info);
            fail("WebMapServer instantiation should fail");
        } catch(IOException e) {
            assertThat(e.getCause(), instanceOf(ServiceException.class));
            ServiceException serviceException = (ServiceException) e.getCause();
            assertThat(serviceException.getMessage(), containsString("Error while parsing XML"));
            
            SAXException saxException = (SAXException) serviceException.getCause();
            Exception cause = saxException.getException();
            assertFalse("Expect external entity cause", cause != null && cause instanceof FileNotFoundException);
        }
        
    }
    
    @Test
    public void testWfsCascadeEntityExpansion() throws Exception {
        CatalogBuilder cb = new CatalogBuilder(getCatalog());
        DataStoreInfo ds = cb.buildDataStore("wfs-xxe");
        URL url = getClass().getResource("wfs1.1.0Capabilities-xxe.xml");
        ds.getConnectionParameters().put(WFSDataStoreFactory.URL.key, url);
        // required or the store won't fetch caps from a file
        ds.getConnectionParameters().put("TESTING", Boolean.TRUE);
        final ResourcePool rp = getCatalog().getResourcePool();
        try {
            rp.getDataStore(ds);
            fail("Store creation should have failed to to XXE attack");
        } catch(Exception e) {
            String message = e.getMessage();
            assertThat(message, containsString("Entity resolution disallowed"));
            assertThat(message, containsString("file:///file/not/there"));
        }
    }
    
    @Test
    public void testStyleWithExternalEntities() throws Exception {
        StyleInfo si = getCatalog().getStyleByName(EXTERNAL_ENTITIES);
        try {
            si.getStyle();
            fail("Should have failed with a parse error");
        } catch(Exception e) {
            String message = e.getMessage();
            assertThat(message, containsString("Entity resolution disallowed"));
            assertThat(message, containsString("/this/file/does/not/exist"));
        }
    }
    
    @Test
    public void testParseExternalMark() throws Exception {
        StyleInfo si = getCatalog().getStyleByName(HUMANS);
        // used to blow here with an NPE
        Style s = si.getStyle();
        s.accept(new AbstractStyleVisitor() {
            @Override
            public void visit(Mark mark) {
                assertEquals("ttf://Webdings", mark.getExternalMark().getOnlineResource().getLinkage().toASCIIString());
            }
        });
    }
}
