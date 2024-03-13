package ParseTree;

import Dictionary.*;

import java.util.*;

public class ParseNode {

    protected ArrayList<ParseNode> children = new ArrayList<>();
    protected ParseNode parent = null;
    protected Symbol data = null;
    private static final String[] ADJP = new String[]{"NNS", "QP", "NN", "$", "ADVP", "JJ", "VBN", "VBG", "ADJP", "JJR", "NP", "JJS", "DT", "FW", "RBR", "RBS", "SBAR", "RB"};
    private static final String[] ADVP = new String[]{"RB", "RBR", "RBS", "FW", "ADVP", "TO", "CD", "JJR", "JJ", "IN", "NP", "JJS", "NN"};
    private static final String[] CONJP = new String[]{"CC", "RB", "IN"};
    private static final String[] FRAG = new String[]{};
    private static final String[] INTJ = new String[]{};
    private static final String[] LST = new String[]{"LS", ":"};
    private static final String[] NAC = new String[]{"NN", "NNS", "NNP", "NNPS", "NP", "NAC", "EX", "$", "CD", "QP", "PRP", "VBG", "JJ", "JJS", "JJR", "ADJP", "FW"};
    private static final String[] PP = new String[]{"IN", "TO", "VBG", "VBN", "RP", "FW"};
    private static final String[] PRN = new String[]{};
    private static final String[] PRT = new String[]{"RP"};
    private static final String[] QP = new String[]{"$", "IN", "NNS", "NN", "JJ", "RB", "DT", "CD", "NCD", "QP", "JJR", "JJS"};
    private static final String[] RRC = new String[]{"VP", "NP", "ADVP", "ADJP", "PP"};
    private static final String[] S = new String[]{"TO", "IN", "VP", "S", "SBAR", "ADJP", "UCP", "NP"};
    private static final String[] SBAR = new String[]{"WHNP", "WHPP", "WHADVP", "WHADJP", "IN", "DT", "S", "SQ", "SINV", "SBAR", "FRAG"};
    private static final String[] SBARQ = new String[]{"SQ", "S", "SINV", "SBARQ", "FRAG"};
    private static final String[] SINV = new String[]{"VBZ", "VBD", "VBP", "VB", "MD", "VP", "S", "SINV", "ADJP", "NP"};
    private static final String[] SQ = new String[]{"VBZ", "VBD", "VBP", "VB", "MD", "VP", "SQ"};
    private static final String[] UCP = new String[]{};
    private static final String[] VP = new String[]{"TO", "VBD", "VBN", "MD", "VBZ", "VB", "VBG", "VBP", "VP", "ADJP", "NN", "NNS", "NP"};
    private static final String[] WHADJP = new String[]{"CC", "WRB", "JJ", "ADJP"};
    private static final String[] WHADVP = new String[]{"CC", "WRB"};
    private static final String[] WHNP = new String[]{"WDT", "WP", "WP$", "WHADJP", "WHPP", "WHNP"};
    private static final String[] WHPP = new String[]{"IN", "TO", "FW"};
    private static final String[] NP1 = new String[]{"NN", "NNP", "NNPS", "NNS", "NX", "POS", "JJR"};
    private static final String[] NP2 = new String[]{"NP"};
    private static final String[] NP3 = new String[]{"$", "ADJP", "PRN"};
    private static final String[] NP4 = new String[]{"CD"};
    private static final String[] NP5 = new String[]{"JJ" , "JJS", "RB", "QP"};

    /**
     * Empty constructor for ParseNode class.
     */
    public ParseNode() {
    }

    /**
     * Constructs a ParseNode from a single line. If the node is a leaf node, it only sets the data. Otherwise, splits
     * the line w.r.t. spaces and paranthesis and calls itself resursively to generate its child parseNodes.
     * @param parent The parent node of this node.
     * @param line The input line to create this parseNode.
     * @param isLeaf True, if this node is a leaf node; false otherwise.
     */
    public ParseNode(ParseNode parent, String line, boolean isLeaf){
        int parenthesisCount = 0;
        StringBuilder childLine = new StringBuilder();
        this.parent = parent;
        if (isLeaf){
            data = new Symbol(line);
        } else {
            data = new Symbol(line.substring(1, line.indexOf(" ")));
            if (line.indexOf(")") == line.lastIndexOf(")")){
                children.add(new ParseNode(this, line.substring(line.indexOf(" ") + 1, line.indexOf(")")), true));
            } else {
                for (int i = line.indexOf(" ") + 1; i < line.length(); i++){
                    if (line.charAt(i) != ' ' || parenthesisCount > 0){
                        childLine.append(line.charAt(i));
                    }
                    if (line.charAt(i) == '('){
                        parenthesisCount++;
                    } else {
                        if (line.charAt(i) == ')'){
                            parenthesisCount--;
                        }
                    }
                    if (parenthesisCount == 0 && (childLine.length() > 0)){
                        children.add(new ParseNode(this, childLine.toString().trim(), false));
                        childLine = new StringBuilder();
                    }
                }
            }
        }
    }

    /**
     * Another simple constructor for ParseNode. It takes inputs left and right children of this node, and the data.
     * Sets the corresponding attributes with these inputs.
     * @param left Left child of this node.
     * @param right Right child of this node.
     * @param data Data for this node.
     */
    public ParseNode(ParseNode left, ParseNode right, Symbol data){
        children.add(left);
        left.parent = this;
        children.add(right);
        right.parent = this;
        this.data = data;
    }

    /**
     * Another simple constructor for ParseNode. It takes inputs left child of this node and the data.
     * Sets the corresponding attributes with these inputs.
     * @param left Left child of this node.
     * @param data Data for this node.
     */
    public ParseNode(ParseNode left, Symbol data){
        children.add(left);
        left.parent = this;
        this.data = data;
    }

    /**
     * Another simple constructor for ParseNode. It only can take input the data, and sets it.
     * @param data Data for this node.
     */
    public ParseNode(Symbol data){
        this.data = data;
    }

    /**
     * Extracts the head of the children of this current node.
     * @param priorityList Depending on the pos of current node, the priorities among the children are given with this parameter
     * @param direction Depending on the pos of the current node, search direction is either from left to right, or from
     *                  right to left.
     * @param defaultCase If true, and no child appears in the priority list, returns first child on the left, or first
     *                    child on the right depending on the search direction.
     * @return Head node of the children of the current node
     */
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

    /**
     * If current node is not a leaf, it has one or more children, this method determines recursively the head of
     * that (those) child(ren). Otherwise, it returns itself. In this way, this method returns the head of all leaf
     * successors.
     * @return Head node of the descendant leaves of this current node.
     */
    public ParseNode headLeaf(){
        if (!children.isEmpty()){
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

    /**
     * Calls searchHeadChild to determine the head node of all children of this current node. The search direction and
     * the search priority list is determined according to the symbol in this current parent node.
     * @return Head node among its children of this current node.
     */
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

    /**
     * Returns an iterator for the child nodes of this {@link ParseNode}.
     * @return Iterator for the children of this very node.
     */
    public Iterator<ParseNode> getChildIterator(){
         return children.iterator();
    }

    /**
     * Adds a child node at the end of the children node list.
     * @param child Child node to be added.
     */
    public void addChild(ParseNode child){
        children.add(child);
        child.parent = this;
    }

    /**
     * Recursive method to restore the parents of all nodes below this node in the hierarchy.
     */
    public void correctParents(){
        for (ParseNode child : children){
            child.parent = this;
            child.correctParents();
        }
    }

    /**
     * Recursive method to remove all nodes starting with the symbol X. If the node is removed, its children are
     * connected to the next sibling of the deleted node.
     */
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

    /**
     * Adds a child node at the given specific index in the children node list.
     * @param index Index where the new child node will be added.
     * @param child Child node to be added.
     */
    public void addChild(int index, ParseNode child){
        children.add(index, child);
        child.parent = this;
    }

    /**
     * Replaces a child node at the given specific with a new child node.
     * @param index Index where the new child node replaces the old one.
     * @param child Child node to be replaced.
     */
    public void setChild(int index, ParseNode child){
        children.set(index, child);
    }

    /**
     * Removes a given child from children node list.
     * @param child Child node to be deleted.
     */
    public void removeChild(ParseNode child){
        for (int i = 0; i < children.size(); i++){
            if (children.get(i) == child){
                children.remove(i);
                break;
            }
        }
    }

    /**
     * Recursive method to calculate the number of all leaf nodes in the subtree rooted with this current node.
     * @return Number of all leaf nodes in the current subtree.
     */
    public int leafCount(){
        if (children.isEmpty()){
            return 1;
        } else {
            int sum = 0;
            for (ParseNode child : children) {
                sum += child.leafCount();
            }
            return sum;
        }
    }

    /**
     * Recursive method to calculate the number of all nodes in the subtree rooted with this current node.
     * @return Number of all nodes in the current subtree.
     */
    public int nodeCount(){
        if (!children.isEmpty()){
            int sum = 1;
            for (ParseNode child: children){
                sum += child.nodeCount();
            }
            return sum;
        } else {
            return 1;
        }
    }

    /**
     * Recursive method to calculate the number of all nodes, which have more than one child, in the subtree rooted
     * with this current node.
     * @return Number of all nodes, which have more than one child, in the current subtree.
     */
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

    /**
     * Recursive method to remove all punctuation nodes from the current subtree.
     */
    public void stripPunctuation(){
        children.removeIf(parseNode -> Word.isPunctuation(parseNode.getData().getName()));
        for (ParseNode node: children)
            node.stripPunctuation();
    }

    /**
     * Returns number of children of this node.
     * @return Number of children of this node.
     */
    public int numberOfChildren(){
        return children.size();
    }

    /**
     * Returns the child i of this node.
     * @param i Index of the retrieved node.
     * @return i'th child of this node.
     */
    public ParseNode getChild(int i){
        return children.get(i);
    }

    /**
     * Returns the first child of this node.
     * @return First child of this node.
     */
    public ParseNode firstChild(){
        return children.get(0);
    }

    /**
     * Returns the last child of this node.
     * @return Last child of this node.
     */
    public ParseNode lastChild(){
        return children.get(children.size() - 1);
    }

    /**
     * Checks if the given node is the last child of this node.
     * @param child To be checked node.
     * @return True, if child is the last child of this node, false otherwise.
     */
    public boolean isLastChild(ParseNode child){
        return children.get(children.size() - 1) == child;
    }

    /**
     * Returns the index of the given child of this node.
     * @param child Child whose index shoud be returned.
     * @return Index of the child of this node.
     */
    public int getChildIndex(ParseNode child){
        return children.indexOf(child);
    }

    /**
     * Returns true if the given node is a descendant of this node.
     * @param node Node to check if it is descendant of this node.
     * @return True if the given node is descendant of this node.
     */
    public boolean isDescendant(ParseNode node){
        for (ParseNode aChild : children){
            if (aChild.equals(node)){
                return true;
            } else {
                if (aChild.isDescendant(node)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the previous sibling (sister) of this node.
     * @return If this is the first child of its parent, returns null. Otherwise, returns the previous sibling of this
     * node.
     */
    public ParseNode previousSibling(){
        for (int i = 1; i < parent.children.size(); i++){
            if (parent.children.get(i) == this){
                return parent.children.get(i - 1);
            }
        }
        return null;
    }

    /**
     * Returns the next sibling (sister) of this node.
     * @return If this is the last child of its parent, returns null. Otherwise, returns the next sibling of this
     * node.
     */
    public ParseNode nextSibling(){
        for (int i = 0; i < parent.children.size() - 1; i++){
            if (parent.children.get(i) == this){
                return parent.children.get(i + 1);
            }
        }
        return null;
    }

    /**
     * Accessor for the parent attribute.
     * @return Parent of this node.
     */
    public ParseNode getParent(){
        return parent;
    }

    /**
     * Accessor for the data attribute.
     * @return Data of this node.
     */
    public Symbol getData(){
        return data;
    }

    /**
     * Mutator of the data attribute.
     * @param data Data to be set.
     */
    public void setData(Symbol data){
        this.data = data;
    }

    /**
     * Recursive function to count the number of words in the subtree rooted at this node.
     * @param excludeStopWords If true, stop words are not counted.
     * @return Number of words in the subtree rooted at this node.
     */
    public int wordCount(boolean excludeStopWords){
        int sum;
        if (children.isEmpty()){
            if (!excludeStopWords){
                sum = 1;
            } else {
                if (Word.isPunctuation(data.getName()) || data.getName().contains("*") || Word.isEnglishStopWord(data.getName())) {
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

    /**
     * Construct recursively the constituent span list of a subtree rooted at this node.
     * @param startIndex Start index of the leftmost leaf node of this subtree.
     * @param list Returned span list.
     */
    void constituentSpanList(int startIndex, ArrayList<ConstituentSpan> list){
        if (!children.isEmpty()){
            list.add(new ConstituentSpan(data, startIndex, startIndex + leafCount()));
        }
        int total = 0;
        for (ParseNode parseNode : children){
            parseNode.constituentSpanList(startIndex + total, list);
            total += parseNode.leafCount();
        }
    }

    /**
     * Returns true if this node is leaf, false otherwise.
     * @return true if this node is leaf, false otherwise.
     */
    public boolean isLeaf(){
        return children.isEmpty();
    }

    /**
     * Returns true if this node does not contain a meaningful data, false otherwise.
     * @return true if this node does not contain a meaningful data, false otherwise.
     */
    public boolean isDummyNode(){
        return getData().getName().contains("*") || (getData().getName().equals("0") && parent.getData().getName().equals("-NONE-"));
    }

    /**
     * Recursive function to convert the subtree rooted at this node to a sentence.
     * @return A sentence which contains all words in the subtree rooted at this node.
     */
    public String toSentence(){
        if (children.isEmpty()){
            if (getData() != null && !isDummyNode()){
                return " " + getData().getName().replaceAll("-LRB-", "(").replaceAll("-RRB-", ")").replaceAll("-LSB-", "[").replaceAll("-RSB-", "]").replaceAll("-LCB-", "{").replaceAll("-RCB-", "}").replaceAll("-lrb-", "(").replaceAll("-rrb-", ")").replaceAll("-lsb-", "[").replaceAll("-rsb-", "]").replaceAll("-lcb-", "{").replaceAll("-rcb-", "}");
            } else {
                if (isDummyNode()){
                    return "";
                } else {
                    return " ";
                }
            }
        } else {
            StringBuilder st = new StringBuilder();
            for (ParseNode aChild : children) {
                st.append(aChild.toSentence());
            }
            return st.toString();
        }
    }

    /**
     * Recursive function to convert the subtree rooted at this node to a string.
     * @return A string which contains all words in the subtree rooted at this node.
     */
    public String toString(){
        if (children.size() < 2){
            if (children.isEmpty()){
                return getData().getName();
            } else {
                return "(" + data.getName() + " " + firstChild().toString() + ")";
            }
        } else {
            StringBuilder st = new StringBuilder("(" + data.getName());
            for (ParseNode aChild : children) {
                st.append(" ").append(aChild.toString());
            }
            return st + ") ";
        }
    }

    /**
     * Swaps the given child node of this node with the previous sibling of that given node. If the given node is the
     * leftmost child, it swaps with the last node.
     * @param node Node to be swapped.
     */
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

    /**
     * Recursive function to concatenate the data of the all ascendant nodes of this node to a string.
     * @return A string which contains all data of all the ascendant nodes of this node.
     */
    public String ancestorString(){
        if (parent == null){
            return data.getName();
        } else {
            return parent.ancestorString() + data.getName();
        }
    }

    /**
     * Swaps the given child node of this node with the next sibling of that given node. If the given node is the
     * rightmost child, it swaps with the first node.
     * @param node Node to be swapped.
     */
    public void moveRight(ParseNode node){
        int i;
        for (i = 0; i < children.size(); i++) {
            if (children.get(i) == node){
                if (i == children.size() - 1){
                    Collections.swap(children, 0, children.size() - 1);
                } else {
                    Collections.swap(children, i, (i + 1) % children.size());
                }
                return;
            }
        }
        for (ParseNode aChild: children){
            aChild.moveRight(node);
        }
    }

}
