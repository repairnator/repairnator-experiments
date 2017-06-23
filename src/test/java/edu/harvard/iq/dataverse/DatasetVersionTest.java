/*
 *  (C) Michael Bar-Sinai
 */
package edu.harvard.iq.dataverse;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class DatasetVersionTest {

    public DatasetVersionTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testComparator() {
        DatasetVersion ds1_0 = new DatasetVersion();
        ds1_0.setId(0l);
        ds1_0.setVersionNumber( 1l );
        ds1_0.setMinorVersionNumber( 0l );
        ds1_0.setVersionState(DatasetVersion.VersionState.RELEASED);
        
        DatasetVersion ds1_1 = new DatasetVersion();
        ds1_1.setId(1l);
        ds1_1.setVersionNumber( 1l );
        ds1_1.setMinorVersionNumber( 1l );
        ds1_1.setVersionState(DatasetVersion.VersionState.RELEASED);
        
        DatasetVersion ds2_0 = new DatasetVersion();
        ds2_0.setId(2l);
        ds2_0.setVersionNumber( 2l );
        ds2_0.setMinorVersionNumber( 0l );
        ds2_0.setVersionState(DatasetVersion.VersionState.RELEASED);
        
        DatasetVersion ds_draft = new DatasetVersion();
        ds_draft.setId(3l);
        ds_draft.setVersionState(DatasetVersion.VersionState.DRAFT);
        
        List<DatasetVersion> expected = Arrays.asList( ds1_0, ds1_1, ds2_0, ds_draft );
        List<DatasetVersion> actual = Arrays.asList( ds2_0, ds1_0, ds_draft, ds1_1 );
        Collections.sort(actual, DatasetVersion.compareByVersion);
        assertEquals( expected, actual );
    }

    @Test
    public void testIsInReview() {
        DatasetVersion draft = new DatasetVersion();
        draft.setVersionState(DatasetVersion.VersionState.DRAFT);
        draft.setInReview(true);
        assertEquals(true, draft.isInReview());

        DatasetVersion nonDraft = new DatasetVersion();
        nonDraft.setVersionState(DatasetVersion.VersionState.RELEASED);
        nonDraft.setInReview(true);
        assertEquals(false, nonDraft.isInReview());
    }
    
}
