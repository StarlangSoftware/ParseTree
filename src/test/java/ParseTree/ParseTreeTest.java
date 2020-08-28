package ParseTree;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.Assert.*;

public class ParseTreeTest {
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
    public void testNodeCount(){
        assertEquals(34, parseTree1.nodeCount());
        assertEquals(39, parseTree2.nodeCount());
        assertEquals(32, parseTree3.nodeCount());
        assertEquals(28, parseTree4.nodeCount());
        assertEquals(9, parseTree5.nodeCount());
    }

    @Test
    public void testIsFullSentence(){
        assertTrue(parseTree1.isFullSentence());
        assertTrue(parseTree2.isFullSentence());
        assertTrue(parseTree3.isFullSentence());
        assertTrue(parseTree4.isFullSentence());
        assertFalse(parseTree5.isFullSentence());
    }

    @Test
    public void testLeafCount(){
        assertEquals(13, parseTree1.leafCount());
        assertEquals(15, parseTree2.leafCount());
        assertEquals(10, parseTree3.leafCount());
        assertEquals(10, parseTree4.leafCount());
        assertEquals(4, parseTree5.leafCount());
    }

    @Test
    public void testNodeCountWithMultipleChildren(){
        assertEquals(8, parseTree1.nodeCountWithMultipleChildren());
        assertEquals(9, parseTree2.nodeCountWithMultipleChildren());
        assertEquals(8, parseTree3.nodeCountWithMultipleChildren());
        assertEquals(6, parseTree4.nodeCountWithMultipleChildren());
        assertEquals(1, parseTree5.nodeCountWithMultipleChildren());
    }

    @Test
    public void testWordCount(){
        assertEquals(7, parseTree1.wordCount(true));
        assertEquals(8, parseTree2.wordCount(true));
        assertEquals(6, parseTree3.wordCount(true));
        assertEquals(7, parseTree4.wordCount(true));
        assertEquals(2, parseTree5.wordCount(true));
    }

    @Test
    public void testToSentence(){
        assertEquals("The complicated language in the huge new law has muddied the fight .", parseTree1.toSentence());
        assertEquals("The Ways and Means Committee will hold a hearing on the bill next Tuesday .", parseTree2.toSentence());
        assertEquals("We 're about to see if advertising works .", parseTree3.toSentence());
        assertEquals("This time around , they 're moving even faster .", parseTree4.toSentence());
        assertEquals("Ad Notes ... .", parseTree5.toSentence());
    }

    @Test
    public void testConstituentSpan(){
        ConstituentSpan span = parseTree1.constituentSpanList().get(6);
        assertEquals(new Symbol("PP-LOC"), span.getConstituent());
        assertEquals(4, span.getStart());
        assertEquals(9, span.getEnd());
        span = parseTree2.constituentSpanList().get(10);
        assertEquals(new Symbol("VB"), span.getConstituent());
        assertEquals(7, span.getStart());
        assertEquals(8, span.getEnd());
        span = parseTree3.constituentSpanList().get(0);
        assertEquals(new Symbol("S"), span.getConstituent());
        assertEquals(1, span.getStart());
        assertEquals(11, span.getEnd());
        span = parseTree4.constituentSpanList().get(5);
        assertEquals(new Symbol("ADVP"), span.getConstituent());
        assertEquals(3, span.getStart());
        assertEquals(4, span.getEnd());
        span = parseTree5.constituentSpanList().get(4);
        assertEquals(new Symbol("."), span.getConstituent());
        assertEquals(4, span.getStart());
        assertEquals(5, span.getEnd());
    }

}