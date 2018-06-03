package ParseTree.NodeCondition;

import ParseTree.ParseNode;

public class IsEnglishLeaf extends IsLeaf{

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
