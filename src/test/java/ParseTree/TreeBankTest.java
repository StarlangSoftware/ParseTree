package ParseTree;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class TreeBankTest {

    @Test
    public void testTreeBank(){
        TreeBank treeBank1 = new TreeBank(new File("trees"));
        assertEquals(5, treeBank1.size());
        assertEquals(30, treeBank1.wordCount(true));
        TreeBank treeBank2 = new TreeBank(new File("trees"), ".dev");
        assertEquals(5, treeBank2.size());
        assertEquals(30, treeBank2.wordCount(true));
        TreeBank treeBank3 = new TreeBank(new File("trees"), ".dev", 0, 3);
        assertEquals(4, treeBank3.size());
        assertEquals(28, treeBank3.wordCount(true));
    }

}