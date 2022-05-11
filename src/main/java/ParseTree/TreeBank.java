package ParseTree;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeBank {

    protected List<ParseTree> parseTrees;

    /**
     * Empty constructor for TreeBank.
     */
    public TreeBank(){

    }

    private void addParseTree(String path, String fileName){
        try {
            ParseTree parseTree = new ParseTree(new FileInputStream(path));
            parseTree.setName(fileName);
            if (parseTree.getRoot() != null){
                parseTrees.add(parseTree);
            } else {
                System.out.println("Parse Tree " + fileName + " can not be read");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * A constructor of {@link TreeBank} class which reads all {@link ParseTree} files inside the given folder. For each
     * file inside that folder, the constructor creates a ParseTree and puts in inside the list parseTrees.
     * @param folder Folder where all parseTrees reside.
     */
    public TreeBank(File folder){
        parseTrees = new ArrayList<ParseTree>();
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        for (File file:listOfFiles){
            addParseTree(folder.getAbsolutePath() + "/" + file.getName(), file.getName());
        }
    }

    /**
     * A constructor of {@link TreeBank} class which reads all {@link ParseTree} files with the file name satisfying the
     * given pattern inside the given folder. For each file inside that folder, the constructor creates a ParseTree
     * and puts in inside the list parseTrees.
     * @param folder Folder where all parseTrees reside.
     * @param pattern File pattern such as "." ".train" ".test".
     */
    public TreeBank(File folder, String pattern){
        parseTrees = new ArrayList<ParseTree>();
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        for (File file:listOfFiles){
            String fileName = file.getName();
            if (!fileName.contains(pattern)){
                continue;
            }
            addParseTree(folder.getAbsolutePath() + "/" + fileName, fileName);
        }
    }

    /**
     * A constructor of {@link TreeBank} class which reads the files numbered from from to to with the file name
     * satisfying the given pattern inside the given folder. For each file inside that folder, the constructor
     * creates a ParseTree and puts in inside the list parseTrees.
     * @param folder Folder where all parseTrees reside.
     * @param pattern File pattern such as "." ".train" ".test".
     * @param from Starting index for the ParseTrees read.
     * @param to Ending index for the ParseTrees read.
     */
    public TreeBank(File folder, String pattern, int from, int to){
        parseTrees = new ArrayList<ParseTree>();
        for (int i = from; i <= to; i++){
            addParseTree(folder.getAbsolutePath() + "/" + String.format("%04d", i) + pattern, String.format("%04d", i) + pattern);
        }
    }

    /**
     * Strips punctuation symbols from all parseTrees in this TreeBank.
     */
    public void stripPunctuation(){
        for (ParseTree tree:parseTrees){
            tree.stripPunctuation();
        }
    }

    /**
     * Returns number of trees in the TreeBank.
     * @return Number of trees in the TreeBank.
     */
    public int size(){
        return parseTrees.size();
    }

    /**
     * Returns number of words in the parseTrees in the TreeBank. If excludeStopWords is true, stop words are not
     * counted.
     * @param excludeStopWords If true, stop words are not included in the count process.
     * @return Number of all words in all parseTrees in the TreeBank.
     */
    public int wordCount(boolean excludeStopWords){
        int count = 0;
        for (ParseTree tree:parseTrees){
            count += tree.wordCount(excludeStopWords);
        }
        return count;
    }

    /**
     * Accessor for a single ParseTree.
     * @param index Index of the parseTree.
     * @return The ParseTree at the given index.
     */
    public ParseTree get(int index){
        return parseTrees.get(index);
    }

    public void removeTree(int index){
        parseTrees.remove(index);
    }

}
