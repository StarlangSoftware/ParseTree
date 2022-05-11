package ParseTree;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ParallelTreeBankTest {

    @Test
    public void testTreeBank(){
        ParallelTreeBank parallelTreeBank = new ParallelTreeBank(new File("trees"), new File("trees2"));
        assertEquals(3, parallelTreeBank.size());
    }

}
