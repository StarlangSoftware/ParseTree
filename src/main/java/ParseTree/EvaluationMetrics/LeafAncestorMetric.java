package ParseTree.EvaluationMetrics;

import ParseTree.*;
import ParseTree.NodeCondition.IsLeaf;

import java.io.File;
import java.util.*;
import java.util.AbstractMap.*;

public class LeafAncestorMetric extends Metric {

    private double[] accuracy;

    public LeafAncestorMetric(File folder1, File folder2) {
        super(folder1, folder2);
    }

    private double editDistance(ArrayList<ParseNode> string1, ArrayList<ParseNode> string2) {
        int[][] dp = new int[string1.size() + 1][string2.size() + 1];
        for (int i = 0; i < string1.size() + 1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < string2.size() + 1; i++) {
            dp[0][i] = i;
        }
        for (int i = 1; i < dp.length; i++) {
            for (int j = 1; j < dp[i].length; j++) {
                dp[i][j] = 1 + Math.min(dp[i][j - 1], dp[i - 1][j]);
                if (string1.get(i - 1).getData().getName().equals(string2.get(j - 1).getData().getName())) {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1]);
                } else {
                    dp[i][j] = Math.min(dp[i][j], dp[i - 1][j - 1] + 1);
                }
            }
        }
        return ((double) (string1.size() + string2.size()) - dp[string1.size() - 1][string2.size() - 1]) / (string1.size() + string2.size());
    }

    private ArrayList<ParseNode> createList(ParseNode node) {
        ArrayList<ParseNode> list = new ArrayList<>();
        ParseNode current = node.getParent().getParent();
        SimpleEntry<Integer, String> entry = new SimpleEntry<>(-1, "");
        if (current.getChild(0).equals(node.getParent())) {
            entry = new SimpleEntry<>(1, "[");
        } else if (current.getChild(current.numberOfChildren() - 1).equals(node.getParent())) {
            entry = new SimpleEntry<>(1, "]");
        }
        while (current != null) {
            list.add(current);
            current = current.getParent();
            if (current != null) {
                if (entry.getValue().equals("[")) {
                    if (list.get(list.size() - 1).equals(current.getChild(0))) {
                        entry = new SimpleEntry<>(list.size() + 1, "[");
                    } else {
                        list.add(new ParseNode(new Symbol("[")));
                        entry = new SimpleEntry<>(-1, "");
                    }
                } else if (entry.getValue().equals("]")) {
                    if (list.get(list.size() - 1).equals(current.getChild(current.numberOfChildren() - 1))) {
                        entry = new SimpleEntry<>(list.size() + 1, "]");
                    } else {
                        list.add(new ParseNode(new Symbol("]")));
                        entry = new SimpleEntry<>(-1, "");
                    }
                }
            }
        }
        if (entry.getKey() > -1) {
            list.add(entry.getKey(), new ParseNode(new Symbol(entry.getValue())));
        }
        return list;
    }

    private HashMap<Integer, ArrayList<ParseNode>> calculateStrings(ParseNode node) {
        HashMap<Integer, ArrayList<ParseNode>> map = new HashMap<>();
        NodeCollector collector = new NodeCollector(node, new IsLeaf());
        ArrayList<ParseNode> nodes = collector.collect();
        for (int i = 0; i < nodes.size(); i++) {
            map.put(i, createList(nodes.get(i)));
        }
        return map;
    }

    @Override
    protected void calculate() {
        accuracy = new double[2];
        for (int i = 0; i < treeBank1.size(); i++) {
            ParseTree tree1 = treeBank1.get(i);
            ParseTree tree2 = treeBank2.get(i);
            add(tree1, tree2);
        }
    }

    @Override
    public void add(ParseTree tree1, ParseTree tree2) {
        HashMap<Integer, ArrayList<ParseNode>> map1 = calculateStrings(tree1.getRoot());
        HashMap<Integer, ArrayList<ParseNode>> map2 = calculateStrings(tree2.getRoot());
        for (Integer key : map1.keySet()) {
            accuracy[0] += editDistance(map1.get(key), map2.get(key));
        }
        accuracy[1] += map1.size();
    }

    public double getAccuracy() {
        return accuracy[0] / accuracy[1];
    }
}
