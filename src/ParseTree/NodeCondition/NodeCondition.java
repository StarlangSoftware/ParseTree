package ParseTree.NodeCondition;

import ParseTree.ParseNode;

public interface NodeCondition {
    boolean satisfies(ParseNode parseNode);
}
