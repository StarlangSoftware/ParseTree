package ParseTree;

import java.io.*;
import java.nio.charset.StandardCharsets;
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

    /**
     * A constructor of {@link TreeBank} class which reads all {@link ParseTree} files inside the given folder. For each
     * file inside that folder, the constructor creates a ParseTree and puts in inside the list parseTrees.
     */
    public TreeBank(File folder){
        parseTrees = new ArrayList<ParseTree>();
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);
        for (File file:listOfFiles){
            try {
                ParseTree parseTree = new ParseTree(new FileInputStream(folder.getAbsolutePath() + "/" + file.getName()));
                if (parseTree.getRoot() != null){
                    parseTrees.add(parseTree);
                } else {
                    System.out.println("Parse Tree " + file.getName() + " can not be read");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
            if (!fileName.contains(pattern))
                continue;
            try {
                ParseTree parseTree = new ParseTree(new FileInputStream(folder.getAbsolutePath() + "/" + fileName));
                if (parseTree.getRoot() != null) {
                    parseTrees.add(parseTree);
                } else {
                    System.out.println("Parse Tree " + folder.getAbsolutePath() + "/" + fileName + " can not be read");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
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
            try {
                parseTrees.add(new ParseTree(new FileInputStream(folder.getAbsolutePath() + "/" + String.format("%04d", i) + pattern)));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * An obsolete constructor of the {@link TreeBank} class. If the contents of all the parseTree's are inside a single
     * file, this constructor can be called. Each line in this file corresponds to a single ParseTree.
     * @param fileName File containing the parse trees.
     */
    public TreeBank(String fileName){
        String line, treeLine;
        int parenthesisCount = 0;
        parseTrees = new ArrayList<ParseTree>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
            line = br.readLine();
            treeLine = "";
            while (line != null){
                if (!line.isEmpty()){
                    for (int i = 0; i < line.length(); i++){
                        if (line.charAt(i) == '('){
                            parenthesisCount++;
                        } else {
                            if (line.charAt(i) == ')'){
                                parenthesisCount--;
                            }
                        }
                    }
                    treeLine = treeLine + line;
                    if (parenthesisCount == 0){
                        ParseTree tree = new ParseTree(treeLine);
                        if (tree.getRoot() != null){
                            parseTrees.add(tree);
                        }
                        treeLine = "";
                    }
                }
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
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
     * Another obsolete function of the {@link TreeBank} class. If the contents of all the parseTree's must be inside a
     * single file, this function saves all the ParseTree's in a single file. Each line in this file corresponds to a
     * single ParseTree.
     * @param fileName Output file that will contain the parse trees.
     */
    public void save(String fileName){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8));
            for (ParseTree tree:parseTrees){
                fw.write("( " + tree.toString() + " )\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accessor for a single ParseTree.
     * @param index Index of the parseTree.
     * @return The ParseTree at the given index.
     */
    public ParseTree get(int index){
        return parseTrees.get(index);
    }

}
