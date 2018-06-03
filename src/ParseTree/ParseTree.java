package ParseTree;

import DependencyParser.UniversalDependencyRelation;
import ParseTree.NodeCondition.IsEnglishLeaf;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ParseTree{
    private static ArrayList<String> sentenceLabels = new ArrayList<String>(
            Arrays.asList("SINV", "SBARQ", "SBAR", "SQ", "S"));

    protected ParseNode root;

    public ParseTree(){
        root = null;
    }

    public ParseTree(ParseNode root){
        this.root = root;
    }

    public ParseTree(String line){
        line = line.replaceAll("\\n", "");
        line = line.replaceAll("\\t", "");
        if (line.contains("(") && line.lastIndexOf(")") != -1){
            line = line.substring(line.indexOf("(") + 1, line.lastIndexOf(")")).trim();
            root = new ParseNode(null, line, false);
        } else {
            root = null;
        }
    }

    public ParseTree(FileInputStream file){
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(file, "UTF8"));
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

    public int nodeCountWithMultipleChildren(){
        return root.nodeCountWithMultipleChildren();
    }

    public int nodeCount(){
        return root.nodeCount();
    }

    public int leafCount(){
        return root.leafCount();
    }

    public boolean isFullSentence(){
        if (root != null && sentenceLabels.contains(root.data.getName())){
            return true;
        }
        return false;
    }

    public void save(String fileName){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            fw.write("( " + this.toString() + " )\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    public void correctParents(){
        root.correctParents();
    }

    public void removeXNodes(){
        root.removeXNodes();
    }

    public void stripPunctuation(){
        root.stripPunctuation();
    }

    public ParseNode getRoot(){
        return root;
    }

    public String toString(){
        return root.toString();
    }

    public String toSentence(){
        return root.toSentence();
    }

    public int wordCount(boolean excludeStopWords){
        return root.wordCount(excludeStopWords);
    }

    public int compareTo(Object o) {
        return 0;
    }
}
