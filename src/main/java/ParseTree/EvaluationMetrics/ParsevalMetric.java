package ParseTree.EvaluationMetrics;

import ParseTree.*;

import java.io.File;
import java.util.AbstractMap.*;
import java.util.HashSet;

public class ParsevalMetric extends Metric {

    private int[] precision;
    private int[] recall;

    public ParsevalMetric(File folder1, File folder2) {
        super(folder1, folder2);
    }

    private SimpleEntry<String, Integer> traverseTree(HashSet<String> set, int count, ParseNode node) {
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
    protected void calculate() {
        this.precision = new int[2];
        this.recall = new int[2];
        for (int i = 0; i < treeBank1.size(); i++) {
            ParseTree tree1 = treeBank1.get(i);
            ParseTree tree2 = treeBank2.get(i);
            add(tree1, tree2);
        }
    }

    @Override
    public void add(ParseTree tree1, ParseTree tree2) {
        HashSet<String> set1 = new HashSet<>();
        traverseTree(set1, 0, tree1.getRoot());
        HashSet<String> set2 = new HashSet<>();
        traverseTree(set2, 0, tree2.getRoot());
        precision[1] += set2.size();
        recall[1] += set1.size();
        for (String key : set1) {
            if (set2.contains(key)) {
                precision[0]++;
                recall[0]++;
            }
        }
    }

    public double getPrecision() {
        return ((double) precision[0]) / precision[1];
    }

    public double getRecall() {
        return ((double) recall[0]) / recall[1];
    }

    public double getFScore() {
        double precision = getPrecision();
        double recall = getRecall();
        return (2 * precision * recall) / (precision + recall);
    }
}
