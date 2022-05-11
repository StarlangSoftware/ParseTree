package ParseTree.EvaluationMetrics;

import ParseTree.NodeCollector;
import ParseTree.NodeCondition.IsLeaf;
import ParseTree.*;

import java.util.*;
import java.util.AbstractMap.*;

public class ModifiedLeafAncestorMetric extends LAMetric {

    private ArrayList<ParseNode> createList(ParseNode node, HashSet<ParseNode> visited) {
        ArrayList<ParseNode> list = new ArrayList<>();
        ParseNode current = node.getParent();
        while (current != null) {
            if (!visited.contains(current)) {
                visited.add(current);
                list.add(current);
            } else {
                list.add(new ParseNode(new Symbol(".")));
            }
            current = current.getParent();
        }
        return list;
    }

    protected HashMap<Integer, ArrayList<ParseNode>> calculateStrings(ParseNode node) {
        HashSet<ParseNode> visited = new HashSet<>();
        HashMap<Integer, ArrayList<ParseNode>> map = new HashMap<>();
        NodeCollector collector = new NodeCollector(node, new IsLeaf());
        ArrayList<ParseNode> nodes = collector.collect();
        for (int i = 0; i < nodes.size(); i++) {
            map.put(i, createList(nodes.get(i), visited));
        }
        return map;
    }

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
        SimpleEntry<Integer, Integer> entry = traverseTree(goldTree.getRoot());
        int m = entry.getKey(), n = entry.getValue();
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
                    int index = map1.get(key).get(i - 1).getData().getName().indexOf("-PRI");
                    String first = map1.get(key).get(i - 1).getData().getName();
                    double plus = 1 + (m / (m + n));
                    double minus = 1 - (m / (m + n));
                    if (index > -1) {
                        first = first.substring(0, index);
                        if (first.equals(map2.get(key).get(j - 1).getData().getName())) {
                            dp[i][j] = Math.min(Math.min((plus * ((m + n) / (m * plus + (n * (1 - (m / (m + n))))))) + dp[i][j - 1], 1 + dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1]));
                        } else {
                            dp[i][j] = Math.min((plus * ((m + n) / (m * plus + (n * (1 - (m / (m + n))))))) + Math.min(dp[i][j - 1], 1 + dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1] + (plus * ((m + n) / (m * plus + (n * (1 - (m / (m + n)))))))));
                        }
                    } else {
                        if (first.equals(map2.get(key).get(j - 1).getData().getName())) {
                            dp[i][j] = Math.min(Math.min((minus * ((m + n) / (m * plus + (n * (1 - (m / (m + n))))))) + dp[i][j - 1], 1 + dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1]));
                        } else {
                            dp[i][j] = Math.min(Math.min((minus * ((m + n) / (m * plus + (n * (1 - (m / (m + n))))))) + dp[i][j - 1], 1 + dp[i - 1][j]), Math.min(dp[i][j], dp[i - 1][j - 1] + (minus * ((m + n) / (m * plus + (n * (1 - (m / (m + n)))))))));
                        }
                    }
                }
            }
            accuracy[0] += ((double) (map1.get(key).size() + map2.get(key).size()) - dp[map1.get(key).size() - 1][map2.get(key).size() - 1]) / (map1.get(key).size() + map2.get(key).size());
        }
        accuracy[1] += map1.size();
        return accuracy;
    }
}
