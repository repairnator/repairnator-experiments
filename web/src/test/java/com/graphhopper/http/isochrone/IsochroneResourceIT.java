package com.graphhopper.http.isochrone;

import com.graphhopper.directions.api.client.ApiClient;
import com.graphhopper.directions.api.client.api.IsochroneApi;
import com.graphhopper.directions.api.client.model.IsochroneResponse;
import com.graphhopper.http.GraphHopperApplication;
import com.graphhopper.http.GraphHopperServerConfiguration;
import com.graphhopper.util.CmdArgs;
import com.graphhopper.util.Helper;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.AfterClass;
import org.junit.ClassRule;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class IsochroneResourceIT {
    private static final String DIR = "./target/andorra-gh/";

    private static final GraphHopperServerConfiguration config = new GraphHopperServerConfiguration();

    private static IsochroneApi client;

    static {
        config.getGraphHopperConfiguration().merge(new CmdArgs().
                put("prepare.ch.weightings", "no").
                put("graph.flag_encoders", "car").
                put("datareader.file", "../core/files/andorra.osm.pbf").
                put("graph.location", DIR));
        client = new IsochroneApi();
        client.setApiClient(new ApiClient().setBasePath("http://localhost:8080"));
    }

    @ClassRule
    public static final DropwizardAppRule<GraphHopperServerConfiguration> app = new DropwizardAppRule(
            GraphHopperApplication.class, config);

    @AfterClass
    public static void cleanUp() {
        Helper.removeDir(new File(DIR));
    }

    @Test
    public void requestByTimeLimit() throws Exception {
        IsochroneResponse rsp = client.isochroneGet("42.531073,1.573792", "no_key_necessary",
                5 * 60, -1, "car", 2, false);
        assertEquals(2, rsp.getPolygons().size());
        List polygon0 = rsp.getPolygons().get(0).getGeometry().getCoordinates().get(0);
        List polygon1 = rsp.getPolygons().get(1).getGeometry().getCoordinates().get(0);

        assertTrue(contains(polygon0, 42.5386, 1.587224));
        assertFalse(contains(polygon0, 42.558012, 1.589756));

        assertTrue(contains(polygon1, 42.558012, 1.589756));
        assertFalse(contains(polygon1, 42.53841, 1.635246));
    }

    @Test
    public void requestByDistanceLimit() throws Exception {
        IsochroneResponse rsp = client.isochroneGet("42.531073,1.573792", "no_key_necessary", -1,
                3_000, "car", 2, false);
        assertEquals(2, rsp.getPolygons().size());
        List polygon0 = rsp.getPolygons().get(0).getGeometry().getCoordinates().get(0);
        List polygon1 = rsp.getPolygons().get(1).getGeometry().getCoordinates().get(0);

        assertTrue(contains(polygon0, 42.531706, 1.57937));
        assertFalse(contains(polygon0, 42.5386, 1.587224));

        assertTrue(contains(polygon1, 42.543216, 1.591644));
        assertFalse(contains(polygon1, 42.558012, 1.589756));
    }

    @Test
    public void requestReverseFlow() throws Exception {
        IsochroneResponse rsp = client.isochroneGet("42.531073,1.573792", "no_key_necessary",
                5 * 60, -1, "car", 2, true);
        assertEquals(2, rsp.getPolygons().size());
        List polygon0 = rsp.getPolygons().get(0).getGeometry().getCoordinates().get(0);
        List polygon1 = rsp.getPolygons().get(1).getGeometry().getCoordinates().get(0);

        assertTrue(contains(polygon0, 42.5386, 1.587224));
        assertFalse(contains(polygon0, 42.558012, 1.589756));

        assertTrue(contains(polygon1, 42.558012, 1.589756));
        assertFalse(contains(polygon1, 42.53841, 1.635246));
    }

    private boolean contains(List polygon, double lat, double lon) {
        int index = 0;
        double lats[] = new double[polygon.size()];
        double lons[] = new double[polygon.size()];

        for (Object o : polygon) {
            List latlon = (List) o;
            lons[index] = ((Number) latlon.get(0)).doubleValue();
            lats[index] = ((Number) latlon.get(1)).doubleValue();
            index++;
        }
        return new Polygon(lats, lons).contains(lat, lon);
    }

}