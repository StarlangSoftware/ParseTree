package ParseTree;

import ParseTree.EvaluationMetrics.LeafAncestorMetric;
import ParseTree.EvaluationMetrics.ModifiedLeafAncestorMetric;
import ParseTree.EvaluationMetrics.ModifiedParsevalMetric;
import ParseTree.EvaluationMetrics.ParsevalMetric;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class MetricTest {

    @Test
    public void testParseval() {
        ParsevalMetric metric = new ParsevalMetric();
        double[][] matrix = metric.calculate(new ParallelTreeBank(new File("Turkish1"), new File("Turkish2")));
        double[] vector = metric.average(matrix);
        assertEquals(0.5060545935545936, vector[2], 0.01);
    }

    @Test
    public void testModifiedParseval() {
        ModifiedParsevalMetric metric = new ModifiedParsevalMetric();
        double[][] matrix = metric.calculate(new ParallelTreeBank(new File("Turkish1"), new File("Turkish2")));
        double[] vector = metric.average(matrix);
        assertEquals(0.484297091103383, vector[2], 0.01);
    }

    @Test
    public void testLeafAncestor() {
        LeafAncestorMetric metric = new LeafAncestorMetric();
        double[][] matrix = metric.calculate(new ParallelTreeBank(new File("Turkish1"), new File("Turkish2")));
        double[] vector = metric.average(matrix);
        assertEquals(0.8560941043083901, vector[0] / vector[1], 0.01);
    }

    @Test
    public void testModifiedLeafAncestor() {
        ModifiedLeafAncestorMetric metric = new ModifiedLeafAncestorMetric();
        double[][] matrix = metric.calculate(new ParallelTreeBank(new File("Turkish1"), new File("Turkish2")));
        double[] vector = metric.average(matrix);
        assertEquals(0.8741496598639455, vector[0] / vector[1], 0.01);
    }
}
