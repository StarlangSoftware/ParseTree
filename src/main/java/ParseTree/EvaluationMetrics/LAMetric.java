package ParseTree.EvaluationMetrics;

import ParseTree.*;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class LAMetric extends Metric {

    @Override
    public double[][] calculate(ParallelTreeBank treeBanks) {
        double[][] matrix = new double[treeBanks.size()][2];
        for (int i = 0; i < treeBanks.size(); i++) {
            matrix[i] = add(treeBanks.fromTreeBank().get(i), treeBanks.toTreeBank().get(i));
        }
        return matrix;
    }

    protected abstract HashMap<Integer, ArrayList<ParseNode>> calculateStrings(ParseNode node);

    @Override
    public double[] average(double[][] matrix) {
        double[] average = new double[2];
        for (double[] doubles : matrix) {
            average[0] += doubles[0];
            average[1] += doubles[1];
        }
        return average;
    }
}
