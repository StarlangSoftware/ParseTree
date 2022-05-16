package ParseTree.EvaluationMetrics;

import ParseTree.ParseNode;
import ParseTree.ParseTree;

import java.util.AbstractMap.*;
import java.util.HashSet;

public class ModifiedParsevalMetric extends ParsevalMetric {

    private SimpleEntry<Integer, Integer> traverseTree(ParseNode node) {
        SimpleEntry<Integer, Integer> current = new SimpleEntry<>(0, 0);
        for (int i = 0; i < node.numberOfChildren(); i++) {
            if (!node.getChild(i).isLeaf()) {
                SimpleEntry<Integer, Integer> child = traverseTree(node.getChild(i));
                current = new SimpleEntry<>(current.getKey() + child.getKey(), current.getValue() + child.getValue());
            }
        }
        if (node.getData().getName().contains("-PRI")) {
            return new SimpleEntry<>(current.getKey() + 1, current.getValue());
        } else {
            return new SimpleEntry<>(current.getKey(), current.getValue() + 1);
        }
    }

    @Override
    public double[] add(ParseTree goldTree, ParseTree computedTree) {
        double[] scores = new double[3];
        SimpleEntry<Integer, Integer> entry = traverseTree(goldTree.getRoot());
        double m = entry.getKey(), n = entry.getValue();
        HashSet<String> set1 = new HashSet<>();
        traverseTree(set1, 0, goldTree.getRoot());
        HashSet<String> set2 = new HashSet<>();
        traverseTree(set2, 0, computedTree.getRoot());
        double score = 0;
        double t = ((m + n) / (m * (1 + (m / (m + n))) + n * (1 - (m / (m + n)))));
        for (String key : set1) {
            if (key.contains("-PRI")) {
                if (set2.contains(key.substring(0, key.length() - 4))) {
                    score += (1 + (m / (m + n))) * t;
                }
            } else {
                if (set2.contains(key)) {
                    score += (1 - (m / (m + n))) * t;
                }
            }
        }
        scores[0] = (score + 0.00) / set2.size();
        scores[1] = (score + 0.00) / set1.size();
        scores[2] = (2 * scores[0] * scores[1]) / (scores[0] + scores[1]);
        return scores;
    }
}
