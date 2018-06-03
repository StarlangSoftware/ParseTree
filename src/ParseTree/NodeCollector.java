package ParseTree;

import ParseTree.NodeCondition.NodeCondition;

import java.util.ArrayList;

public class NodeCollector {
    private NodeCondition condition;
    private ParseNode rootNode;

    public NodeCollector(ParseNode rootNode, NodeCondition condition){
        this.rootNode = rootNode;
        this.condition = condition;
    }

    private void collectNodes(ParseNode parseNode, ArrayList<ParseNode> collected){
        if (condition.satisfies(parseNode)){
            collected.add(parseNode);
        } else {
            for (int i = 0; i < parseNode.numberOfChildren(); i++){
                collectNodes((ParseNode)parseNode.getChild(i), collected);
            }
        }
    }

    public ArrayList<ParseNode> collect(){
        ArrayList<ParseNode> result = new ArrayList<ParseNode>();
        collectNodes(rootNode, result);
        return result;
    }

}
