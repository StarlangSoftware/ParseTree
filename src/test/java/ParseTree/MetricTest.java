package ParseTree;

import ParseTree.EvaluationMetrics.LeafAncestorMetric;
import ParseTree.EvaluationMetrics.ParsevalMetric;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class MetricTest {

    @Test
    public void testParseval() {
        ParsevalMetric metric = new ParsevalMetric(new File("Turkish1"), new File("Turkish2"));
        assertEquals(0.8365384615384616, metric.getPrecision(), 0.01);
        assertEquals(0.8877551020408163, metric.getRecall(), 0.01);
        assertEquals(0.8613861386138615, metric.getFScore(), 0.01);
    }

    @Test
    public void testLeafAncestor() {
        LeafAncestorMetric metric = new LeafAncestorMetric(new File("Turkish1"), new File("Turkish2"));
        assertEquals(0.9032375913328291, metric.getAccuracy(), 0.01);
    }
}
