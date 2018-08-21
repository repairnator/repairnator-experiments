/**
 * This file is part of General Entity Annotator Benchmark.
 *
 * General Entity Annotator Benchmark is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * General Entity Annotator Benchmark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with General Entity Annotator Benchmark.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.gerbil.execute;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.aksw.gerbil.annotator.TestAnnotatorConfiguration;
import org.aksw.gerbil.annotator.decorator.ErrorCountingAnnotatorDecorator;
import org.aksw.gerbil.database.SimpleLoggingResultStoringDAO4Debugging;
import org.aksw.gerbil.dataset.DatasetConfiguration;
import org.aksw.gerbil.dataset.impl.nif.NIFFileDatasetConfig;
import org.aksw.gerbil.datatypes.ExperimentTaskConfiguration;
import org.aksw.gerbil.datatypes.ExperimentType;
import org.aksw.gerbil.evaluate.EvaluatorFactory;
import org.aksw.gerbil.evaluate.impl.ConfidenceBasedFMeasureCalculator;
import org.aksw.gerbil.matching.Matching;
import org.aksw.gerbil.matching.impl.MatchingsCounterImpl;
import org.aksw.gerbil.semantic.kb.SimpleWhiteListBasedUriKBClassifier;
import org.aksw.gerbil.semantic.kb.UriKBClassifier;
import org.aksw.gerbil.test.SameAsRetrieverSingleton4Tests;
import org.aksw.gerbil.transfer.nif.Document;
import org.aksw.gerbil.transfer.nif.Marking;
import org.aksw.gerbil.transfer.nif.data.Annotation;
import org.aksw.gerbil.transfer.nif.data.DocumentImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * This class tests the entity linking evaluation.
 * 
 * @author Michael R&ouml;der <roeder@informatik.uni-leipzig.de>
 * 
 */
@RunWith(Parameterized.class)
public class C2KBTest extends AbstractExperimentTaskTest {

    @BeforeClass
    public static void setMatchingsCounterDebugFlag() {
        MatchingsCounterImpl.setPrintDebugMsg(true);
        ConfidenceBasedFMeasureCalculator.setPrintDebugMsg(true);
        ErrorCountingAnnotatorDecorator.setPrintDebugMsg(true);

    }

    private static final String TEXTS[] = new String[] {
            "Florence May Harding studied at a school in Sydney, and with Douglas Robert Dundas , but in effect had no formal training in either botany or art.",
            "Such notables include James Carville, who was the senior political adviser to Bill Clinton, and Donna Brazile, the campaign manager of the 2000 presidential campaign of Vice-President Al Gore.",
            "The senator received a Bachelor of Laws from the Columbia University." };
    private static final DatasetConfiguration GOLD_STD = new NIFFileDatasetConfig("OKE_Task1",
            "src/test/resources/OKE_Challenge/example_data/task1.ttl", false, ExperimentType.C2KB, null, null);
    private static final UriKBClassifier URI_KB_CLASSIFIER = new SimpleWhiteListBasedUriKBClassifier(
            "http://dbpedia.org/resource/");

    @Parameters
    public static Collection<Object[]> data() {
        List<Object[]> testConfigs = new ArrayList<Object[]>();
        // The extractor returns nothing
        testConfigs.add(new Object[] { new Document[] {}, GOLD_STD, Matching.WEAK_ANNOTATION_MATCH,
                new double[] { 0, 0, 0, 0, 0, 0, 0 } });
        // The extractor found everything and marked all entities using the OKE
        // URI --> some of them should be wrong, because they are not linked to
        // the DBpedia
        testConfigs.add(new Object[] { new Document[] { new DocumentImpl(TEXTS[0],
                "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-1",
                Arrays.asList(
                        (Marking) new Annotation(
                                "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Florence_May_Harding"),
                        (Marking) new Annotation(
                                "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/National_Art_School"),
                        (Marking) new Annotation(
                                "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Sydney"),
                        (Marking) new Annotation(
                                "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Douglas_Robert_Dundas"))),
                // found 2xnull but missed 2xDBpedia
                // (TP=2,FP=2,FN=2,P=0.5,R=0.5,F1=0.5)
                new DocumentImpl(TEXTS[1], "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-2",
                        Arrays.asList(
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/James_Carville"),
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Political_adviser"),
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Bill_Clinton"),
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Donna_Brazile"),
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Campaign_manager"),
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Al_Gore"))),
                // missed 6xDBpedia
                // (TP=0,FP=6,FN=6,P=0,R=0,F1=0)
                new DocumentImpl(TEXTS[2], "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-3",
                        Arrays.asList(
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Senator_1"),
                                (Marking) new Annotation(
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/Columbia_University"))) },
                // found 1xnull but missed 1xDBpedia
                // (TP=1,FP=1,FN=1,P=0.5,R=0.5,F1=0.5)
                GOLD_STD, Matching.WEAK_ANNOTATION_MATCH,
                new double[] { 1.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0.25, 0.25, 0.25, 0 } });
        // The linker linked all entities using dbpedia URIs if they were
        // available. Otherwise, it didn't mark them.
        testConfigs
                .add(new Object[] {
                        new Document[] {
                                new DocumentImpl(TEXTS[0],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-1",
                                        Arrays.asList(
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Florence_May_Harding"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Sydney"))),
                                // found 2xDBpedia but missed 2xnull
                                // (TP=2,FP=0,FN=2,P=1,R=0.5,F1=2/3)
                                new DocumentImpl(TEXTS[1],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-2",
                                        Arrays.asList(
                                                (Marking) new Annotation("http://dbpedia.org/resource/James_Carville"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Political_consulting"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Bill_Clinton"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Donna_Brazile"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Campaign_manager"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Al_Gore"))),
                                // found 6xDBpedia
                                // (TP=6,FP=0,FN=0,P=1,R=1,F1=1)
                                new DocumentImpl(TEXTS[2],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-3",
                                        Arrays.asList((Marking) new Annotation(
                                                "http://dbpedia.org/resource/Columbia_University"))) },
                        // found 1xDBpedia but missed 1xnull
                        // (TP=1,FP=0,FN=1,P=1,R=0.5,F1=2/3)
                        GOLD_STD, Matching.WEAK_ANNOTATION_MATCH,
                        new double[] { 1.0, 2.0 / 3.0, 7.0 / 9.0, 1.0, 0.75, 1.5 / 1.75, 0 } });
        // The linker linked all entities using dbpedia URIs if they were
        // available or an emtpy URI set.
        testConfigs
                .add(new Object[] {
                        new Document[] {
                                new DocumentImpl(TEXTS[0],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-1",
                                        Arrays.asList(
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Florence_May_Harding"),
                                                (Marking) new Annotation(new HashSet<String>()),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Sydney"),
                                                (Marking) new Annotation(new HashSet<String>()))),
                                new DocumentImpl(TEXTS[1],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-2",
                                        Arrays.asList(
                                                (Marking) new Annotation("http://dbpedia.org/resource/James_Carville"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Political_consulting"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Bill_Clinton"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Donna_Brazile"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Campaign_manager"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Al_Gore"))),
                                new DocumentImpl(TEXTS[2],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-3",
                                        Arrays.asList((Marking) new Annotation(new HashSet<String>()),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Columbia_University"))) },
                        GOLD_STD, Matching.WEAK_ANNOTATION_MATCH, new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0 } });
        // The extractor found everything and marked all entities using dbpedia
        // URIs (if they were available) or own URIs
        testConfigs
                .add(new Object[] {
                        new Document[] {
                                new DocumentImpl(TEXTS[0],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-1",
                                        Arrays.asList(
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Florence_May_Harding"),
                                                (Marking) new Annotation(
                                                        "http://aksws.org/notInWiki/National_Art_School"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Sydney"),
                                                (Marking) new Annotation(
                                                        "http://aksws.org/notInWiki/Douglas_Robert_Dundas"))),
                                new DocumentImpl(TEXTS[1],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-2",
                                        Arrays.asList(
                                                (Marking) new Annotation("http://dbpedia.org/resource/James_Carville"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Political_consulting"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Bill_Clinton"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Donna_Brazile"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Campaign_manager"),
                                                (Marking) new Annotation("http://dbpedia.org/resource/Al_Gore"))),
                                new DocumentImpl(TEXTS[2],
                                        "http://www.ontologydesignpatterns.org/data/oke-challenge/task-1/sentence-3",
                                        Arrays.asList((Marking) new Annotation("http://aksws.org/notInWiki/Senator_1"),
                                                (Marking) new Annotation(
                                                        "http://dbpedia.org/resource/Columbia_University"))) },
                        GOLD_STD, Matching.WEAK_ANNOTATION_MATCH, new double[] { 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0 } });
        return testConfigs;
    }

    private Document annotatorResults[];
    private DatasetConfiguration dataset;
    private double expectedResults[];
    private Matching matching;

    public C2KBTest(Document[] annotatorResults, DatasetConfiguration dataset, Matching matching,
            double[] expectedResults) {
        this.annotatorResults = annotatorResults;
        this.dataset = dataset;
        this.expectedResults = expectedResults;
        this.matching = matching;
    }

    @Test
    public void test() {
        int experimentTaskId = 1;
        SimpleLoggingResultStoringDAO4Debugging experimentDAO = new SimpleLoggingResultStoringDAO4Debugging();
        ExperimentTaskConfiguration configuration = new ExperimentTaskConfiguration(
                new TestAnnotatorConfiguration(Arrays.asList(annotatorResults), ExperimentType.C2KB), dataset,
                ExperimentType.C2KB, matching);
        runTest(experimentTaskId, experimentDAO, SameAsRetrieverSingleton4Tests.getInstance(),
                new EvaluatorFactory(URI_KB_CLASSIFIER), configuration,
                new F1MeasureTestingObserver(this, experimentTaskId, experimentDAO, expectedResults));
    }
}
