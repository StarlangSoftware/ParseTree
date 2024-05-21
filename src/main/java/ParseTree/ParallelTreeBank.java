package ParseTree;

import java.io.File;

public class ParallelTreeBank {

    protected TreeBank fromTreeBank, toTreeBank;

    /**
     * Given two treebanks read, the method removes the trees which do not exist in one of the treebanks. At the end,
     * we will only have the tree files that exist in both treebanks.
     */
    protected void removeDifferentTrees(){
        int i, j;
        i = 0;
        j = 0;
        while (i < fromTreeBank.size() && j < toTreeBank.size()){
            if (fromTreeBank.get(i).getName().compareTo(toTreeBank.get(j).getName()) < 0){
                fromTreeBank.removeTree(i);
            } else {
                if (toTreeBank.get(j).getName().compareTo(fromTreeBank.get(i).getName()) < 0){
                    toTreeBank.removeTree(j);
                } else {
                    i++;
                    j++;
                }
            }
        }
        while (i < fromTreeBank.size()){
            fromTreeBank.removeTree(i);
        }
        while (j < toTreeBank.size()){
            toTreeBank.removeTree(j);
        }
    }

    /**
     * Empty constructor for the parallel treebank.
     */
    public ParallelTreeBank(){
    }

    /**
     * Constructor for the ParallelTreeBank class. A ParallelTreeBank consists of two treebanks, where each sentence
     * appears in both treebanks with possibly different tree structures. Each treebank is stored in a separate folder.
     * Both treebanks are read and distinct sentences are removed from the treebanks.
     * @param folder1 Folder containing the files for trees in the first treebank.
     * @param folder2 Folder containing the files for trees in the second treebank.
     */
    public ParallelTreeBank(File folder1, File folder2){
        fromTreeBank = new TreeBank(folder1);
        toTreeBank = new TreeBank(folder2);
        removeDifferentTrees();
    }

    /**
     * Another constructor for the ParallelTreeBank class. A ParallelTreeBank consists of two treebanks, where each
     * sentence appears in both treebanks with possibly different tree structures. Each treebank is stored in a separate
     * folder. Both treebanks are read and distinct sentences are removed from the treebanks. In thid constructor, only
     * files matching the pattern are read. Pattern is used for matching the extensions such as .train, .test, .dev.
     * @param folder1 Folder containing the files for trees in the first treebank.
     * @param folder2 Folder containing the files for trees in the second treebank.
     * @param pattern File pattern used for matching. Patterns are usually used for setting the extensions such as
     *                .train, .test, .dev.
     */
    public ParallelTreeBank(File folder1, File folder2, String pattern){
        fromTreeBank = new TreeBank(folder1, pattern);
        toTreeBank = new TreeBank(folder2, pattern);
        removeDifferentTrees();
    }

    /**
     * Returns number of sentences in ParallelTreeBank.
     * @return Number of sentences.
     */
    public int size(){
        return fromTreeBank.size();
    }

    /**
     * Returns the tree at position index in the first treebank.
     * @param index Position of the tree in the first treebank.
     * @return The tree at position index in the first treebank.
     */
    public ParseTree fromTree(int index){
        return fromTreeBank.get(index);
    }

    /**
     * Returns the tree at position index in the second treebank.
     * @param index Position of the tree in the second treebank.
     * @return The tree at position index in the second treebank.
     */
    public ParseTree toTree(int index){
        return toTreeBank.get(index);
    }

    /**
     * Returns the first treebank.
     * @return First treebank.
     */
    public TreeBank fromTreeBank(){
        return fromTreeBank;
    }

    /**
     * Returns the second treebank.
     * @return Second treebank.
     */
    public TreeBank toTreeBank(){
        return toTreeBank;
    }

}
