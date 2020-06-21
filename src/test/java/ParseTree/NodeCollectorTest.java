package ParseTree;

import ParseTree.NodeCondition.IsEnglishLeaf;
import ParseTree.NodeCondition.IsLeaf;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class NodeCollectorTest {
    ParseTree parseTree1;
    ParseTree parseTree2;
    ParseTree parseTree3;
    ParseTree parseTree4;
    ParseTree parseTree5;

    @org.junit.Before
    public void setUp() throws Exception {
        parseTree1 = new ParseTree(new FileInputStream(new File("trees/0000.dev")));
        parseTree2 = new ParseTree(new FileInputStream(new File("trees/0001.dev")));
        parseTree3 = new ParseTree(new FileInputStream(new File("trees/0002.dev")));
        parseTree4 = new ParseTree(new FileInputStream(new File("trees/0003.dev")));
        parseTree5 = new ParseTree(new FileInputStream(new File("trees/0014.dev")));
    }

    @Test
    public void testCollectLeaf() {
        NodeCollector nodeCollector1 = new NodeCollector(parseTree1.getRoot(), new IsLeaf());
        assertEquals(13, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree2.getRoot(), new IsLeaf());
        assertEquals(15, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree3.getRoot(), new IsLeaf());
        assertEquals(10, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree4.getRoot(), new IsLeaf());
        assertEquals(10, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree5.getRoot(), new IsLeaf());
        assertEquals(4, nodeCollector1.collect().size());
    }

    @Test
    public void testCollectEnglish() {
        NodeCollector nodeCollector1 = new NodeCollector(parseTree1.getRoot(), new IsEnglishLeaf());
        assertEquals(13, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree2.getRoot(), new IsEnglishLeaf());
        assertEquals(15, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree3.getRoot(), new IsEnglishLeaf());
        assertEquals(9, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree4.getRoot(), new IsEnglishLeaf());
        assertEquals(10, nodeCollector1.collect().size());
        nodeCollector1 = new NodeCollector(parseTree5.getRoot(), new IsEnglishLeaf());
        assertEquals(4, nodeCollector1.collect().size());
    }

}