package ParseTree.EvaluationMetrics;

import ParseTree.NodeCollector;
import ParseTree.NodeCondition.IsLeaf;
import ParseTree.*;

import java.util.*;

public class ModifiedLeafAncestorMetric extends LeafAncestorMetric {

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
}
