package ParseTree.EvaluationMetrics;

import ParseTree.*;
import ParseTree.NodeCondition.IsLeaf;

import java.util.*;
import java.util.AbstractMap.*;

public class LeafAncestorMetric extends LAMetric {

    @Override
    protected double calculateCell(double[][] dp, int i, int j, ArrayList<ParseNode> string1, ArrayList<ParseNode> string2) {
        if (string1.get(i - 1).getData().getName().equals(string2.get(j - 1).getData().getName())) {
            return Math.min(1 + Math.min(dp[i][j - 1], dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1]));
        } else {
            return Math.min(1 + Math.min(dp[i][j - 1], dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1] + 1));
        }
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

    protected HashMap<Integer, ArrayList<ParseNode>> calculateStrings(ParseNode node) {
        HashMap<Integer, ArrayList<ParseNode>> map = new HashMap<>();
        NodeCollector collector = new NodeCollector(node, new IsLeaf());
        ArrayList<ParseNode> nodes = collector.collect();
        for (int i = 0; i < nodes.size(); i++) {
            map.put(i, createList(nodes.get(i)));
        }
        return map;
    }
}
