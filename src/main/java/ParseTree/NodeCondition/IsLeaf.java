package ParseTree.NodeCondition;

import ParseTree.ParseNode;

public class IsLeaf implements NodeCondition {

    /**
     * Implemented node condition for the leaf node. If a node has no children it is a leaf node.
     * @param parseNode Checked node.
     * @return True if the input node is a leaf node, false otherwise.
     */
    public boolean satisfies(ParseNode parseNode) {
        return parseNode.numberOfChildren() == 0;
    }
}
