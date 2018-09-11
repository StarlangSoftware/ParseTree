package ParseTree;

import DependencyParser.UniversalDependencyRelation;
import Dictionary.*;

import java.util.*;

public class ParseNode {

    protected ArrayList<ParseNode> children;
    protected ParseNode parent = null;
    protected int childIndex = -1;
    protected Symbol data = null;
    private static String[] ADJP = new String[]{"NNS", "QP", "NN", "$", "ADVP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB"};
    private static String[] ADVP = new String[]{"RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN"};
    private static String[] CONJP = new String[]{"CC", "RB", "IN"};
    private static String[] FRAG = new String[]{};
    private static String[] INTJ = new String[]{};
    private static String[] LST = new String[]{"LS", ":"};
    private static String[] NAC = new String[]{"NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW"};
    private static String[] PP = new String[]{"IN", "TO", "VBG", "VBN", "RP", "FW"};
    private static String[] PRN = new String[]{};
    private static String[] PRT = new String[]{"RP"};
    private static String[] QP = new String[]{"$", "IN", "NNS", "NN", "JJ", "RB", "DT", "CD", "NCD", "QP", "JJR", "JJS"};
    private static String[] RRC = new String[]{"VP", "NP", "ADVP", "ADJP", "PP"};
    private static String[] S = new String[]{"TO", "IN", "VP", "S", "SBAR", "ADJP", "UCP", "NP"};
    private static String[] SBAR = new String[]{"WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG"};
    private static String[] SBARQ = new String[]{"SQ", "S", "SINV", "SBARQ", "FRAG"};
    private static String[] SINV = new String[]{"VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP"};
    private static String[] SQ = new String[]{"VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ"};
    private static String[] UCP = new String[]{};
    private static String[] VP = new String[]{"TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "VP", "ADJP", "NN", "NNS", "NP"};
    private static String[] WHADJP = new String[]{"CC", "WRB", "JJ", "ADJP"};
    private static String[] WHADVP = new String[]{"CC", "WRB"};
    private static String[] WHNP = new String[]{"WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP"};
    private static String[] WHPP = new String[]{"IN", "TO", "FW"};
    private static String[] NP1 = new String[]{"NN", "NNP", "NNPS", "NNS", "NX", "POS", "JJR"};
    private static String[] NP2 = new String[]{"NP"};
    private static String[] NP3 = new String[]{"$", "ADJP", "PRN"};
    private static String[] NP4 = new String[]{"CD"};
    private static String[] NP5 = new String[]{"JJ" , "JJS", "RB", "QP"};

    public ParseNode() {
    }

    public ParseNode(ParseNode parent, String line, boolean isLeaf){
        int parenthesisCount = 0;
        String childLine = "";
        this.parent = parent;
        if (parent != null){
            this.childIndex = parent.children.size() - 1;
        }
        children = new ArrayList<ParseNode>();
        if (isLeaf){
            data = new Symbol(line);
        } else {
            data = new Symbol(line.substring(1, line.indexOf(" ")));
            if (line.indexOf(")") == line.lastIndexOf(")")){
                children.add(new ParseNode(this, line.substring(line.indexOf(" ") + 1, line.indexOf(")")), true));
            } else {
                for (int i = line.indexOf(" ") + 1; i < line.length(); i++){
                    if (line.charAt(i) != ' ' || parenthesisCount > 0){
                        childLine = childLine + line.charAt(i);
                    }
                    if (line.charAt(i) == '('){
                        parenthesisCount++;
                    } else {
                        if (line.charAt(i) == ')'){
                            parenthesisCount--;
                        }
                    }
                    if (parenthesisCount == 0 && !childLine.isEmpty()){
                        children.add(new ParseNode(this, childLine.trim(), false));
                        childLine = "";
                    }
                }
            }
        }
    }

    public ParseNode(ParseNode left, ParseNode right, Symbol data){
        children = new ArrayList<ParseNode>();
        children.add(left);
        left.parent = this;
        children.add(right);
        right.parent = this;
        this.data = data;
    }

    public ParseNode(ParseNode left, Symbol data){
        children = new ArrayList<ParseNode>();
        children.add(left);
        left.parent = this;
        this.data = data;
    }

    public ParseNode(Symbol data){
        children = new ArrayList<ParseNode>();
        this.data = data;
    }

    private ParseNode searchHeadChild(String[] priorityList, SearchDirectionType direction, boolean defaultCase){
        switch (direction){
            case LEFT:
                for (String item : priorityList) {
                    for (ParseNode child : children) {
                        if (child.getData().trimSymbol().getName().equals(item)) {
                            return child;
                        }
                    }
                }
                if (defaultCase){
                    return firstChild();
                }
                break;
            case RIGHT:
                for (String item : priorityList) {
                    for (int j = children.size() - 1; j >= 0; j--) {
                        ParseNode child = children.get(j);
                        if (child.getData().trimSymbol().getName().equals(item)) {
                            return child;
                        }
                    }
                }
                if (defaultCase){
                    return lastChild();
                }
        }
        return null;
    }

    public ParseNode headLeaf(){
        if (children.size() > 0){
            ParseNode head = headChild();
            if (head != null){
                return head.headLeaf();
            } else {
                return null;
            }
        } else {
            return this;
        }
    }

    public ParseNode headChild(){
        ParseNode result;
        switch (data.trimSymbol().toString()){
            case "ADJP":
                return searchHeadChild(ADJP, SearchDirectionType.LEFT, true);
            case "ADVP":
                return searchHeadChild(ADVP, SearchDirectionType.RIGHT, true);
            case "CONJP":
                return searchHeadChild(CONJP, SearchDirectionType.RIGHT, true);
            case "FRAG":
                return searchHeadChild(FRAG, SearchDirectionType.RIGHT, true);
            case "INTJ":
                return searchHeadChild(INTJ, SearchDirectionType.LEFT, true);
            case "LST":
                return searchHeadChild(LST, SearchDirectionType.RIGHT, true);
            case "NAC":
                return searchHeadChild(NAC, SearchDirectionType.LEFT, true);
            case "PP":
                return searchHeadChild(PP, SearchDirectionType.RIGHT, true);
            case "PRN":
                return searchHeadChild(PRN, SearchDirectionType.LEFT, true);
            case "PRT":
                return searchHeadChild(PRT, SearchDirectionType.RIGHT, true);
            case "QP":
                return searchHeadChild(QP, SearchDirectionType.LEFT, true);
            case "RRC":
                return searchHeadChild(RRC, SearchDirectionType.RIGHT, true);
            case "S":
                return searchHeadChild(S, SearchDirectionType.LEFT, true);
            case "SBAR":
                return searchHeadChild(SBAR, SearchDirectionType.LEFT, true);
            case "SBARQ":
                return searchHeadChild(SBARQ, SearchDirectionType.LEFT, true);
            case "SINV":
                return searchHeadChild(SINV, SearchDirectionType.LEFT, true);
            case "SQ":
                return searchHeadChild(SQ, SearchDirectionType.LEFT, true);
            case "UCP":
                return searchHeadChild(UCP, SearchDirectionType.RIGHT, true);
            case "VP":
                return searchHeadChild(VP, SearchDirectionType.LEFT, true);
            case "WHADJP":
                return searchHeadChild(WHADJP, SearchDirectionType.LEFT, true);
            case "WHADVP":
                return searchHeadChild(WHADVP, SearchDirectionType.RIGHT, true);
            case "WHNP":
                return searchHeadChild(WHNP, SearchDirectionType.LEFT, true);
            case "WHPP":
                return searchHeadChild(WHPP, SearchDirectionType.RIGHT, true);
            case "NP":
                if (lastChild().getData().getName().equals("POS")){
                    return lastChild();
                } else {
                    result = searchHeadChild(NP1, SearchDirectionType.RIGHT, false);
                    if (result != null){
                        return result;
                    } else {
                        result = searchHeadChild(NP2, SearchDirectionType.LEFT, false);
                        if (result != null){
                            return result;
                        } else {
                            result = searchHeadChild(NP3, SearchDirectionType.RIGHT, false);
                            if (result != null){
                                return result;
                            } else {
                                result = searchHeadChild(NP4, SearchDirectionType.RIGHT, false);
                                if (result != null){
                                    return result;
                                } else {
                                    result = searchHeadChild(NP5, SearchDirectionType.RIGHT, false);
                                    if (result != null){
                                        return  result;
                                    } else {
                                        return lastChild();
                                    }
                                }
                            }
                        }
                    }
                }
        }
        return null;
    }

    public void constructUniversalDependencies(HashMap<ParseNode, UniversalDependencyRelation> dependencies){
    }

    public Iterator<ParseNode> getChildIterator(){
         return children.iterator();
    }

    public void addChild(ParseNode child){
        children.add(child);
        child.parent = this;
    }

    public void correctParents(){
        for (ParseNode child : children){
            child.parent = this;
            child.correctParents();
        }
    }

    public void removeXNodes(){
        int i = 0;
        while (i < children.size()){
            if (children.get(i).getData().getName().startsWith("X")){
                children.addAll(i + 1, children.get(i).children);
                children.remove(i);
            } else {
                i++;
            }
        }
        for (ParseNode child:children){
            child.removeXNodes();
        }
    }

    public void addChild(int index, ParseNode child){
        children.add(index, child);
        child.parent = this;
    }

    public void setChild(int index, ParseNode child){
        children.set(index, child);
    }

    public void removeChild(ParseNode child){
        for (int i = 0; i < children.size(); i++){
            if (children.get(i) == child){
                children.remove(i);
                break;
            }
        }
    }

    public int leafCount(){
        if (children.size() == 0){
            return 1;
        } else {
            int sum = 0;
            for (ParseNode child : children) {
                sum += child.leafCount();
            }
            return sum;
        }
    }

    public int nodeCount(){
        if (children.size() > 0){
            int sum = 1;
            for (ParseNode child: children){
                sum += child.nodeCount();
            }
            return sum;
        } else {
            return 1;
        }
    }

    public int nodeCountWithMultipleChildren(){
        if (children.size() > 1){
            int sum = 1;
            for (ParseNode child: children){
                sum += child.nodeCountWithMultipleChildren();
            }
            return sum;
        } else {
            return 0;
        }
    }

    public void stripPunctuation(){
        Iterator<ParseNode> iterator = children.iterator();
        while (iterator.hasNext()){
            if (Word.isPunctuation(iterator.next().getData().getName()))
                iterator.remove();
        }
        for (ParseNode node: children)
            node.stripPunctuation();
    }

    public int numberOfChildren(){
        return children.size();
    }

    public ParseNode getChild(int i){
        return children.get(i);
    }

    public ParseNode firstChild(){
        return children.get(0);
    }

    public ParseNode lastChild(){
        return children.get(children.size() - 1);
    }

    public boolean isLastChild(ParseNode child){
        return children.get(children.size() - 1) == child;
    }

    public ParseNode previousSibling(){
        for (int i = 1; i < parent.children.size(); i++){
            if (parent.children.get(i) == this){
                return parent.children.get(i - 1);
            }
        }
        return null;
    }

    public ParseNode nextSibling(){
        for (int i = 0; i < parent.children.size() - 1; i++){
            if (parent.children.get(i) == this){
                return parent.children.get(i + 1);
            }
        }
        return null;
    }

    public ParseNode getParent(){
        return parent;
    }

    public Symbol getData(){
        return data;
    }

    public void setData(Symbol data){
        this.data = data;
    }

    public int wordCount(boolean excludeStopWords){
        int sum;
        if (children.size() == 0){
            if (!excludeStopWords){
                sum = 1;
            } else {
                if (data.getName().equalsIgnoreCase(",") || data.getName().equalsIgnoreCase(".") || data.getName().equalsIgnoreCase(";")
                        || data.getName().contains("*") || data.getName().equalsIgnoreCase("at") || data.getName().equalsIgnoreCase("the")
                        || data.getName().equalsIgnoreCase("to") || data.getName().equalsIgnoreCase("a") || data.getName().equalsIgnoreCase("an")
                        || data.getName().equalsIgnoreCase("not") || data.getName().equalsIgnoreCase("is") || data.getName().equalsIgnoreCase("was")
                        || data.getName().equalsIgnoreCase("were") || data.getName().equalsIgnoreCase("have") || data.getName().equalsIgnoreCase("had")
                        || data.getName().equalsIgnoreCase("has") || data.getName().equalsIgnoreCase("!") || data.getName().equalsIgnoreCase("?")
                        || data.getName().equalsIgnoreCase("by") || data.getName().equalsIgnoreCase("at") || data.getName().equalsIgnoreCase("on")
                        || data.getName().equalsIgnoreCase("off") || data.getName().equalsIgnoreCase("'s") || data.getName().equalsIgnoreCase("n't")
                        || data.getName().equalsIgnoreCase("can") || data.getName().equalsIgnoreCase("could") || data.getName().equalsIgnoreCase("may")
                        || data.getName().equalsIgnoreCase("might") || data.getName().equalsIgnoreCase("will") || data.getName().equalsIgnoreCase("would")
                        || data.getName().equalsIgnoreCase("''") || data.getName().equalsIgnoreCase("'") || data.getName().equalsIgnoreCase("\"")
                        || data.getName().equalsIgnoreCase("\"\"") || data.getName().equalsIgnoreCase("as") || data.getName().equalsIgnoreCase("with")
                        || data.getName().equalsIgnoreCase("for") || data.getName().equalsIgnoreCase("will") || data.getName().equalsIgnoreCase("would")
                        || data.getName().equalsIgnoreCase("than") || data.getName().equalsIgnoreCase("``") || data.getName().equalsIgnoreCase("$")
                        || data.getName().equalsIgnoreCase("and") || data.getName().equalsIgnoreCase("or") || data.getName().equalsIgnoreCase("of")
                        || data.getName().equalsIgnoreCase("are") || data.getName().equalsIgnoreCase("be") || data.getName().equalsIgnoreCase("been")
                        || data.getName().equalsIgnoreCase("do") || data.getName().equalsIgnoreCase("few") || data.getName().equalsIgnoreCase("there")
                        || data.getName().equalsIgnoreCase("up") || data.getName().equalsIgnoreCase("down")) {
                    sum = 0;
                } else {
                    sum = 1;
                }
            }
        }
        else{
            sum = 0;
        }
        for (ParseNode aChild : children) {
            sum += aChild.wordCount(excludeStopWords);
        }
        return sum;
    }

    public boolean isLeaf(){
        return children.size() == 0;
    }

    public boolean isDummyNode(){
        return getData().getName().contains("*") || (getData().getName().equals("0") && parent.getData().getName().equals("-NONE-"));
    }

    public String toSentence(){
        if (children.size() == 0){
            if (getData() != null && !isDummyNode()){
                return " " + getData().getName().replaceAll("-LRB-", "(").replaceAll("-RRB-", ")").replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replaceAll("-LCB-", "{").replaceAll("-RCB-", "}").replaceAll("-lrb-", "(").replaceAll("-rrb-", ")").replaceAll("-lsb-", "[").replaceAll("-rsb-", "]").replaceAll("-lcb-", "{").replaceAll("-rcb-", "}");
            } else {
                return " ";
            }
        } else {
            String st = "";
            for (ParseNode aChild : children) {
                st = st + aChild.toSentence();
            }
            return st;
        }
    }

    public String toString(){
        if (children.size() < 2){
            if (children.size() < 1){
                return getData().getName();
            } else {
                return "(" + data.getName() + " " + firstChild().toString() + ")";
            }
        } else {
            String st = "(" + data.getName();
            for (ParseNode aChild : children) {
                st = st + " " + aChild.toString();
            }
            return st + ") ";
        }
    }

    public void moveLeft(ParseNode node){
        int i;
        for (i = 0; i < children.size(); i++) {
            if (children.get(i) == node){
                if (i == 0){
                    Collections.swap(children, 0, children.size() - 1);
                } else {
                    Collections.swap(children, i, (i - 1) % children.size());
                }
                return;
            }
        }
        for (ParseNode aChild: children){
            aChild.moveLeft(node);
        }
    }

    public String ancestorString(){
        if (parent == null){
            return data.getName();
        } else {
            return parent.ancestorString() + data.getName();
        }
    }

    public void moveRight(ParseNode node){
        int i;
        for (i = 0; i < children.size(); i++) {
            if (children.get(i) == node){
                Collections.swap(children, i, (i + 1) % children.size());
                return;
            }
        }
        for (ParseNode aChild: children){
            aChild.moveRight(node);
        }
    }

}