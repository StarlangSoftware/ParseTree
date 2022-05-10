package ParseTree.EvaluationMetrics;

import ParseTree.ParseTree;
import ParseTree.TreeBank;

import java.io.File;

public abstract class Metric {

    public abstract double[][] calculate(File goldTrees, File computedTrees);

    public abstract double[][] calculate(TreeBank goldTrees, TreeBank computedTrees);

    protected abstract double[] add(ParseTree goldTree, ParseTree computedTree);

    public abstract double[] average(double[][] matrix);
}
