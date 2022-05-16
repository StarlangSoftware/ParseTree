package ParseTree.EvaluationMetrics;

import ParseTree.*;
import ParseTree.NodeCondition.IsLeaf;

import java.util.*;
import java.util.AbstractMap.*;

public class LeafAncestorMetric extends Metric {

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

    protected HashMap<Integer, ArrayList<ParseNode>> calculateStrings(ParseNode node) {
        HashMap<Integer, ArrayList<ParseNode>> map = new HashMap<>();
        NodeCollector collector = new NodeCollector(node, new IsLeaf());
        ArrayList<ParseNode> nodes = collector.collect();
        for (int i = 0; i < nodes.size(); i++) {
            map.put(i, createList(nodes.get(i)));
        }
        return map;
    }

    @Override
    protected double[] add(ParseTree goldTree, ParseTree computedTree) {
        double[] accuracy = new double[2];
        HashMap<Integer, ArrayList<ParseNode>> map1 = calculateStrings(goldTree.getRoot());
        HashMap<Integer, ArrayList<ParseNode>> map2 = calculateStrings(computedTree.getRoot());
        for (Integer key : map1.keySet()) {
            double[][] dp = new double[map1.get(key).size() + 1][map2.get(key).size() + 1];
            for (int i = 0; i < map1.get(key).size() + 1; i++) {
                dp[i][0] = i;
            }
            for (int i = 0; i < map2.get(key).size() + 1; i++) {
                dp[0][i] = i;
            }
            for (int i = 1; i < dp.length; i++) {
                for (int j = 1; j < dp[i].length; j++) {
                    dp[i][j] = Double.MAX_VALUE;
                    String string1 = map1.get(key).get(i - 1).getData().getName(), string2 = map2.get(key).get(j - 1).getData().getName();
                    if (string1.contains("-PRI")) {
                        string1 = string1.substring(0, string1.length() - 4);
                    }
                    if (string1.equals(string2)) {
                        dp[i][j] = Math.min(1 + Math.min(dp[i][j - 1], dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1]));
                    } else {
                        dp[i][j] = Math.min(1 + Math.min(dp[i][j - 1], dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1] + 1));
                    }
                }
            }
            accuracy[0] += ((double) (map1.get(key).size() + map2.get(key).size()) - dp[map1.get(key).size() - 1][map2.get(key).size() - 1]) / (map1.get(key).size() + map2.get(key).size());
        }
        accuracy[1] += map1.size();
        return accuracy;
    }

    @Override
    public double[][] calculate(ParallelTreeBank treeBanks) {
        double[][] matrix = new double[treeBanks.size()][2];
        for (int i = 0; i < treeBanks.size(); i++) {
            matrix[i] = add(treeBanks.fromTreeBank().get(i), treeBanks.toTreeBank().get(i));
        }
        return matrix;
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
