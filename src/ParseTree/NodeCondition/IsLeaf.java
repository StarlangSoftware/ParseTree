package ParseTree.NodeCondition;

import ParseTree.NodeCondition.NodeCondition;
import ParseTree.ParseNode;

public class IsLeaf implements NodeCondition {

    public boolean satisfies(ParseNode parseNode) {
        return parseNode.numberOfChildren() == 0;
    }
}
