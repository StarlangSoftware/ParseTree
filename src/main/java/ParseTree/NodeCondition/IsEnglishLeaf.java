package ParseTree.NodeCondition;

import ParseTree.ParseNode;

public class IsEnglishLeaf extends IsLeaf{

    /**
     * Implemented node condition for English leaf node.
     * @param parseNode Checked node.
     * @return If the node is a leaf node and is not a dummy node, returns true; false otherwise.
     */
    public boolean satisfies(ParseNode parseNode) {
        if (super.satisfies(parseNode)) {
            String data = parseNode.getData().getName();
            String parentData = parseNode.getParent().getData().getName();
            if (data.contains("*") || (data.equals("0") && parentData.equals("-NONE-"))){
                return false;
            }
            return true;
        }
        return false;
    }

}
