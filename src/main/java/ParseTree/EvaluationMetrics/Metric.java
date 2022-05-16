package ParseTree.EvaluationMetrics;

import ParseTree.*;

public abstract class Metric {
    public abstract double[][] calculate(ParallelTreeBank treeBank);

    protected abstract double[] add(ParseTree goldTree, ParseTree computedTree);

    public abstract double[] average(double[][] matrix);
}
