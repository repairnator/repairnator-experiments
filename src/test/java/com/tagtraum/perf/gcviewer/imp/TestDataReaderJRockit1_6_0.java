package com.tagtraum.perf.gcviewer.imp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.tagtraum.perf.gcviewer.UnittestHelper;
import com.tagtraum.perf.gcviewer.UnittestHelper.FOLDER;
import com.tagtraum.perf.gcviewer.model.AbstractGCEvent.Type;
import com.tagtraum.perf.gcviewer.model.GCEvent;
import com.tagtraum.perf.gcviewer.model.GCModel;
import com.tagtraum.perf.gcviewer.model.GCResource;
import com.tagtraum.perf.gcviewer.model.GcResourceFile;
import org.junit.Test;

/**
 * Tests for logs generated by JRockit 1.6 vm.
 * 
 * @author <a href="mailto:gcviewer@gmx.ch">Joerg Wuethrich</a>
 * <p>created on: 06.02.2013</p>
 */
public class TestDataReaderJRockit1_6_0 {

    private InputStream getInputStream(String fileName) throws IOException {
        return UnittestHelper.getResourceAsStream(FOLDER.JROCKIT, fileName);
    }
    
    private DataReader getDataReader(GCResource gcResource) throws UnsupportedEncodingException, IOException {
        return new DataReaderJRockit1_6_0(gcResource, getInputStream(gcResource.getResourceName()));
    }
    
    @Test
    public void testGcPrioPauseSingleParCon() throws Exception {
        TestLogHandler handler = new TestLogHandler();
        handler.setLevel(Level.WARNING);
        GCResource gcResource = new GcResourceFile("SampleJRockit1_6_gc_mode_singleparcon.txt");
        gcResource.getLogger().addHandler(handler);

        DataReader reader = getDataReader(gcResource);
        GCModel model = reader.read();
        
        assertEquals("count", 42, model.size());
        
        GCEvent event = (GCEvent) model.get(0);
        assertEquals("timestamp", 24.930, event.getTimestamp(), 0.000001);
        assertEquals("name", Type.JROCKIT_16_OLD_GC.getName(), event.getExtendedType().getName());
        assertEquals("before", 464309, event.getPreUsed());
        assertEquals("after", 282831, event.getPostUsed());
        assertEquals("total", 524288, event.getTotal());
        assertEquals("pause", 0.020957, event.getPause(), 0.0000001);

        assertEquals("number of warnings", 5, handler.getCount());
    }
    
    @Test
    public void testGcPrioPauseSingleParConVerbose() throws Exception {
        TestLogHandler handler = new TestLogHandler();
        handler.setLevel(Level.WARNING);
        GCResource gcResource = new GcResourceFile("SampleJRockit1_6_verbose_gc_mode_singleparcon.txt");
        gcResource.getLogger().addHandler(handler);

        DataReader reader = getDataReader(gcResource);
        
        assertTrue("should be DataReaderJRockit1_6_0 (but was " + reader.toString() + ")", reader instanceof DataReaderJRockit1_6_0);

        GCModel model = reader.read();
        
        assertEquals("count", 52, model.size());
        
        GCEvent event = (GCEvent) model.get(0);
        assertEquals("timestamp", 26.242, event.getTimestamp(), 0.000001);
        assertEquals("name", Type.JROCKIT_16_OLD_GC.getName(), event.getExtendedType().getName());
        assertEquals("before", 270909, event.getPreUsed());
        assertEquals("after", 210916, event.getPostUsed());
        assertEquals("total", 524288, event.getTotal());
        assertEquals("pause", 0.032087, event.getPause(), 0.0000001);

        assertEquals("number of warnings", 5, handler.getCount());
    }
    
    /**
     * This log file sample contains much more information about concurrent events
     * than is currently parsed. Still the parser must be able to extract the information
     * it can parse.
     */
    @Test
    public void testGenConVerbose() throws Exception {
        DataReader reader = getDataReader(new GcResourceFile("SampleJRockit1_6_verbose_gc_mode_gencon.txt"));
        GCModel model = reader.read();
        
        assertEquals("count", 15, model.size());
    }
    
    @Test
    public void testGenParVerboseNursery() throws Exception {
        TestLogHandler handler = new TestLogHandler();
        handler.setLevel(Level.WARNING);
        GCResource gcResource = new GcResourceFile("SampleJRockit1_6_33_gc_mode_genpar_verbosenursery.txt");
        gcResource.getLogger().addHandler(handler);

        DataReader reader = getDataReader(gcResource);
        GCModel model = reader.read();
        
        assertEquals("count", 3, model.size());
        
        GCEvent event = (GCEvent) model.get(0);
        assertEquals("timestamp", 124.644, event.getTimestamp(), 0.000001);
        assertEquals("name", Type.JROCKIT_16_YOUNG_GC.getName(), event.getExtendedType().getName());
        assertEquals("before", 453996, event.getPreUsed());
        assertEquals("after", 188846, event.getPostUsed());
        assertEquals("total", 786432, event.getTotal());
        assertEquals("pause", 0.055369, event.getPause(), 0.0000001);
        
        // generational algorithms have information about the generation sizing in the introduction of the gc log
        // -> check that it is present in the event
        event = event.getYoung();
        assertEquals("total", 393216, event.getTotal());

        event = (GCEvent) model.get(2);
        assertEquals("timestamp", 148.254, event.getTimestamp(), 0.000001);
        assertEquals("name", Type.JROCKIT_16_OLD_GC.getName(), event.getExtendedType().getName());
        assertEquals("before", 691255, event.getPreUsed());
        assertEquals("after", 279876, event.getPostUsed());
        assertEquals("total", 786432, event.getTotal());
        assertEquals("pause", 0.086713, event.getPause(), 0.0000001);

        // generational algorithms have information about the generation sizing in the introduction of the gc log
        // -> check that it is present in the event
        GCEvent eventTenured = event.getTenured();
        assertEquals("total", 786432 - 393216, eventTenured.getTotal());

        GCEvent eventYoung = event.getYoung();
        assertEquals("total", 393216, eventYoung.getTotal());

        assertEquals("number of warnings", 0, handler.getCount());
    }
    
    @Test
    public void testMalformedType() throws Exception {
        TestLogHandler handler = new TestLogHandler();
        handler.setLevel(Level.INFO);
        GCResource gcResource = new GcResourceFile("byteArray");
        gcResource.getLogger().addHandler(handler);

        ByteArrayInputStream in = new ByteArrayInputStream(
                ("[INFO ][memory ] [YC#1] 9.743-9.793: YC-malformed 294974KB->122557KB (524288KB), 0.050 s, sum of pauses 49.692 ms, longest pause 49.692 ms.")
                       .getBytes());
        
        DataReader reader = new DataReaderJRockit1_6_0(gcResource, in);
        reader.read();
        
        // 3 INFO events:
        // Reading JRockit ... format
        // Failed to determine type ...
        // Reading done.
        assertEquals("number of infos", 3, handler.getCount());
        
        List<LogRecord> logRecords = handler.getLogRecords();
        assertEquals("should start with 'Failed to determine type'", 0, logRecords.get(1).getMessage().indexOf("Failed to determine type"));
    }
    
    @Test
    public void testStandardLine() throws Exception {
        final ByteArrayInputStream in = new ByteArrayInputStream(
                ("[INFO ][memory ] [YC#1] 9.743-9.793: YC 294974KB->122557KB (524288KB), 0.050 s, sum of pauses 49.692 ms, longest pause 49.692 ms.")
                        .getBytes());
        
        DataReader reader = new DataReaderJRockit1_6_0(new GcResourceFile("byteArray"), in);
        GCModel model = reader.read();
        
        assertEquals("count", 1, model.size());
        
        GCEvent event = (GCEvent) model.get(0);
        assertEquals("timestamp", 9.743, event.getTimestamp(), 0.000001);
        assertEquals("name", Type.JROCKIT_16_YOUNG_GC.getName(), event.getExtendedType().getName());
        assertEquals("before", 294974, event.getPreUsed());
        assertEquals("after", 122557, event.getPostUsed());
        assertEquals("total", 524288, event.getTotal());
        assertEquals("pause", 0.049692, event.getPause(), 0.0000001);
    }
    
}
