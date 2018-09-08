package ParseTree;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TreeBank {

    protected List<ParseTree> parseTrees;

    public TreeBank(){

    }

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

    public TreeBank(String fileName){
        String line, treeLine;
        int parenthesisCount = 0;
        parseTrees = new ArrayList<ParseTree>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stripPunctuation(){
        for (ParseTree tree:parseTrees){
            tree.stripPunctuation();
        }
    }

    public int size(){
        return parseTrees.size();
    }

    public int wordCount(boolean excludeStopWords){
        int count = 0;
        for (ParseTree tree:parseTrees){
            count += tree.wordCount(excludeStopWords);
        }
        return count;
    }

    public void save(String fileName){
        BufferedWriter fw;
        try {
            fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"));
            for (ParseTree tree:parseTrees){
                fw.write("( " + tree.toString() + " )\n");
            }
            fw.close();
        } catch (IOException e) {
        }
    }

    public ParseTree get(int index){
        return parseTrees.get(index);
    }

    public int countWords(boolean excludeStopWords){
        int count = 0;
        for (ParseTree tree:parseTrees){
            count += tree.wordCount(excludeStopWords);
        }
        return count;
    }

}
