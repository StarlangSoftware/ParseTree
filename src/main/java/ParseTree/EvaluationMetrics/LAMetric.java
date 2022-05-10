package ParseTree.EvaluationMetrics;

import ParseTree.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class LAMetric extends Metric {

    @Override
    public double[][] calculate(File goldTrees, File computedTrees) {
        TreeBank goldTreeBank = new TreeBank(goldTrees);
        TreeBank computedTreeBank = new TreeBank(computedTrees);
        return calculate(goldTreeBank, computedTreeBank);
    }

    @Override
    public double[][] calculate(TreeBank goldTrees, TreeBank computedTrees) {
        double[][] matrix = new double[goldTrees.size()][2];
        for (int i = 0; i < goldTrees.size(); i++) {
            matrix[i] = add(goldTrees.get(i), computedTrees.get(i));
        }
        return matrix;
    }

    protected abstract double calculateCell(double[][] dp, int i, int j, ArrayList<ParseNode> string1, ArrayList<ParseNode> string2);

    private double editDistance(ArrayList<ParseNode> string1, ArrayList<ParseNode> string2) {
        double[][] dp = new double[string1.size() + 1][string2.size() + 1];
        for (int i = 0; i < string1.size() + 1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < string2.size() + 1; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                dp[i][j] = Double.MAX_VALUE;
                dp[i][j] = calculateCell(dp, i, j, string1, string2);
            }
        }
        return ((double) (string1.size() + string2.size()) - dp[string1.size() - 1][string2.size() - 1]) / (string1.size() + string2.size());
    }

    protected abstract HashMap<Integer, ArrayList<ParseNode>> calculateStrings(ParseNode node);

    @Override
    protected double[] add(ParseTree goldTree, ParseTree computedTree) {
        double[] accuracy = new double[2];
        HashMap<Integer, ArrayList<ParseNode>> map1 = calculateStrings(goldTree.getRoot());
        HashMap<Integer, ArrayList<ParseNode>> map2 = calculateStrings(computedTree.getRoot());
        for (Integer key : map1.keySet()) {
            accuracy[0] += editDistance(map1.get(key), map2.get(key));
        }
        accuracy[1] += map1.size();
        return accuracy;
    }

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
