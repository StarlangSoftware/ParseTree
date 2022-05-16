package ParseTree.EvaluationMetrics;

import ParseTree.*;

import java.util.AbstractMap.*;
import java.util.HashSet;

public class ParsevalMetric extends Metric {

    protected SimpleEntry<String, Integer> traverseTree(HashSet<String> set, int count, ParseNode node) {
        if (node.getChild(0).isLeaf()) {
            set.add(count + "|" + node.getData().getName());
            return new SimpleEntry<>(String.valueOf(count), count + 1);
        }
        StringBuilder current = new StringBuilder();
        int currentCount = count;
        for (int i = 0; i < node.numberOfChildren(); i++) {
            ParseNode child = node.getChild(i);
            SimpleEntry<String, Integer> entry = traverseTree(set, currentCount, child);
            current.append(entry.getKey());
            currentCount = entry.getValue();
            if (i + 1 != node.numberOfChildren()) {
                current.append("-");
            }
        }
        set.add(current + "|" + node.getData().getName());
        return new SimpleEntry<>(current.toString(), currentCount);
    }

    @Override
    public double[][] calculate(ParallelTreeBank treeBanks) {
        double[][] matrix = new double[treeBanks.size()][3];
        for (int i = 0; i < treeBanks.size(); i++) {
            matrix[i] = add(treeBanks.fromTreeBank().get(i), treeBanks.toTreeBank().get(i));
        }
        return matrix;
    }

    @Override
    public double[] add(ParseTree goldTree, ParseTree computedTree) {
        double[] scores = new double[3];
        HashSet<String> set1 = new HashSet<>();
        traverseTree(set1, 0, goldTree.getRoot());
        HashSet<String> set2 = new HashSet<>();
        traverseTree(set2, 0, computedTree.getRoot());
        int score = 0;
        for (String key : set1) {
            if (key.contains("-PRI")) {
                if (set2.contains(key.substring(0, key.length() - 4))) {
                    score++;
                }
            } else {
                if (set2.contains(key)) {
                    score++;
                }
            }
        }
        scores[0] = (score + 0.00) / set2.size();
        scores[1] = (score + 0.00) / set1.size();
        scores[2] = (2 * scores[0] * scores[1]) / (scores[0] + scores[1]);
        return scores;
    }

    @Override
    public double[] average(double[][] matrix) {
        double[] average = new double[3];
        double precision = 0, recall = 0, fScore = 0;
        for (double[] doubles : matrix) {
            precision += doubles[0];
            recall += doubles[1];
            fScore += doubles[2];
        }
        average[0] = (precision + 0.00) / matrix.length;
        average[1] = (recall + 0.00) / matrix.length;
        average[2] = (fScore + 0.00) / matrix.length;
        return average;
    }
}
