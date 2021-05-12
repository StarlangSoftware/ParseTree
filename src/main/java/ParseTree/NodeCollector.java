package ParseTree;

import ParseTree.NodeCondition.NodeCondition;

import java.util.ArrayList;

public class NodeCollector {
    private NodeCondition condition;
    private ParseNode rootNode;

    /**
     * Constructor for the NodeCollector class. NodeCollector's main aim is to collect a set of ParseNode's from a
     * subtree rooted at rootNode, where the ParseNode's satisfy a given NodeCondition, which is implemented by other
     * interface class.
     * @param rootNode Root node of the subtree
     * @param condition The condition interface for which all nodes in the subtree rooted at rootNode will be checked
     */
    public NodeCollector(ParseNode rootNode, NodeCondition condition){
        this.rootNode = rootNode;
        this.condition = condition;
    }

    /**
     * Private recursive method to check all descendants of the parseNode, if they ever satisfy the given node condition
     * @param parseNode Root node of the subtree
     * @param collected The {@link ArrayList} where the collected ParseNode's will be stored.
     */
    private void collectNodes(ParseNode parseNode, ArrayList<ParseNode> collected){
        if (condition == null || condition.satisfies(parseNode)){
            collected.add(parseNode);
        }
        for (int i = 0; i < parseNode.numberOfChildren(); i++){
            collectNodes(parseNode.getChild(i), collected);
        }
    }

    /**
     * Collects and returns all ParseNode's satisfying the node condition.
     * @return All ParseNode's satisfying the node condition.
     */
    public ArrayList<ParseNode> collect(){
        ArrayList<ParseNode> result = new ArrayList<ParseNode>();
        collectNodes(rootNode, result);
        return result;
    }

}
