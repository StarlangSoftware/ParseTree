package ParseTree;

import java.io.File;

public class ParallelTreeBank {

    protected TreeBank fromTreeBank, toTreeBank;

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

    public ParallelTreeBank(){
    }

    public ParallelTreeBank(File folder1, File folder2){
        fromTreeBank = new TreeBank(folder1);
        toTreeBank = new TreeBank(folder2);
        removeDifferentTrees();
    }

    public ParallelTreeBank(File folder1, File folder2, String pattern){
        fromTreeBank = new TreeBank(folder1, pattern);
        toTreeBank = new TreeBank(folder2, pattern);
        removeDifferentTrees();
    }

    public int size(){
        return fromTreeBank.size();
    }

    public ParseTree fromTree(int index){
        return fromTreeBank.get(index);
    }

    public ParseTree toTree(int index){
        return toTreeBank.get(index);
    }

    public TreeBank fromTreeBank(){
        return fromTreeBank;
    }

    public TreeBank toTreeBank(){
        return toTreeBank;
    }

}
