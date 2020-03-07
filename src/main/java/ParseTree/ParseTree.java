package ParseTree;

import DependencyParser.UniversalDependencyRelation;
import ParseTree.NodeCondition.IsEnglishLeaf;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ParseTree{
    private static ArrayList<String> sentenceLabels = new ArrayList<String>(
            Arrays.asList("SINV", "SBARQ", "SBAR", "SQ", "S"));

    protected ParseNode root;

    /**
     * Empty constructor for ParseTree. Initializes the root node to null.
     */
    public ParseTree(){
        root = null;
    }

    /**
     * Basic constructor for a ParseTree. Initializes the root node with the input.
     * @param root Root node of the tree
     */
    public ParseTree(ParseNode root){
        this.root = root;
    }

    /**
     * Another constructor of the ParseTree. The method takes the file containing a single line as input and constructs
     * the whole tree by calling the ParseNode constructor recursively.
     * @param file File containing a single line for a ParseTree
     */
    public ParseTree(FileInputStream file){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8));
            String line = br.readLine();
            if (line.contains("(") && line.contains(")")) {
                line = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
                root = new ParseNode(null, line, false);
                br.close();
            } else {
                root = null;
            }
        } catch (IOException e) {
            root = null;
        }
    }

    /**
     * Gets the next leaf node after the given leaf node in the ParseTree.
     * @param parseNode ParseNode for which next node is calculated.
     * @return Next leaf node after the given leaf node.
     */
    public ParseNode nextLeafNode(ParseNode parseNode){
        NodeCollector nodeCollector = new NodeCollector(root, new IsEnglishLeaf());
        ArrayList<ParseNode> leafList = nodeCollector.collect();
        for (int i = 0; i < leafList.size() - 1; i++){
            if (leafList.get(i).equals(parseNode)){
                return leafList.get(i + 1);
            }
        }
        return null;
    }

    /**
     * Gets the previous leaf node before the given leaf node in the ParseTree.
     * @param parseNode ParseNode for which previous node is calculated.
     * @return Previous leaf node before the given leaf node.
     */
    public ParseNode previousLeafNode(ParseNode parseNode){
        NodeCollector nodeCollector = new NodeCollector(root, new IsEnglishLeaf());
        ArrayList<ParseNode> leafList = nodeCollector.collect();
        for (int i = 1; i < leafList.size(); i++){
            if (leafList.get(i).equals(parseNode)){
                return leafList.get(i - 1);
            }
        }
        return null;
    }

    public HashMap<ParseNode, UniversalDependencyRelation> constructUniversalDependencies(){
        HashMap<ParseNode, UniversalDependencyRelation> universalDependencies = new HashMap<>();
        root.constructUniversalDependencies(universalDependencies);
        return universalDependencies;
    }

    /**
     * Calls recursive method to calculate the number of all nodes, which have more than one children.
     * @return Number of all nodes, which have more than one children.
     */
    public int nodeCountWithMultipleChildren(){
        return root.nodeCountWithMultipleChildren();
    }

    /**
     * Calls recursive method to calculate the number of all nodes tree.
     * @return Number of all nodes in the tree.
     */
    public int nodeCount(){
        return root.nodeCount();
    }

    /**
     * Calls recursive method to calculate the number of all leaf nodes in the tree.
     * @return Number of all leaf nodes in the tree.
     */
    public int leafCount(){
        return root.leafCount();
    }

    public boolean isFullSentence(){
        if (root != null && sentenceLabels.contains(root.data.getName())){
            return true;
        }
        return false;
    }

    /**
     * Saves the tree into the file with the given file name. The output file only contains one line representing tree.
     * @param fileName Output file name
     */
    public void save(String fileName){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            fw.write("( " + this.toString() + " )\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    /**
     * Calls recursive method to restore the parents of all nodes in the tree.
     */
    public void correctParents(){
        root.correctParents();
    }

    /**
     * Calls recursive method to remove all nodes starting with the symbol X. If the node is removed, its children are
     * connected to the next sibling of the deleted node.
     */
    public void removeXNodes(){
        root.removeXNodes();
    }

    /**
     * Calls recursive method to remove all punctuation nodes from the tree.
     */
    public void stripPunctuation(){
        root.stripPunctuation();
    }

    /**
     * Accessor method for the root node.
     * @return Root node
     */
    public ParseNode getRoot(){
        return root;
    }


    /**
     * Calls recursive function to convert the tree to a string.
     * @return A string which contains all words in the tree.
     */
    public String toString(){
        return root.toString();
    }

    /**
     * Calls recursive function to convert the tree to a sentence.
     * @return A sentence which contains all words in the tree.
     */
    public String toSentence(){
        return root.toSentence();
    }

    /**
     * Calls recursive function to count the number of words in the tree.
     * @param excludeStopWords If true, stop words are not counted.
     * @return Number of words in the tree.
     */
    public int wordCount(boolean excludeStopWords){
        return root.wordCount(excludeStopWords);
    }

    public int compareTo(Object o) {
        return 0;
    }
}
