package ParseTree.EvaluationMetrics;

import ParseTree.ParseTree;
import ParseTree.TreeBank;

import java.io.File;

public abstract class Metric {

    protected TreeBank treeBank1, treeBank2;

    public Metric(File folder1, File folder2) {
        treeBank1 = new TreeBank(folder1);
        treeBank2 = new TreeBank(folder2);
        calculate();
    }

    protected abstract void calculate();

    public abstract void add(ParseTree tree1, ParseTree tree2);
}
