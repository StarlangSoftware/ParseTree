package ParseTree;

import org.junit.Test;
import sun.jvm.hotspot.debugger.cdbg.Sym;

import static org.junit.Assert.*;

public class SymbolTest {

    @Test
    public void trimSymbol() {
        assertEquals("NP", new Symbol("NP-SBJ").trimSymbol().getName());
        assertEquals("VP", new Symbol("VP-SBJ-2").trimSymbol().getName());
        assertEquals("NNP", new Symbol("NNP-SBJ-OBJ-TN").trimSymbol().getName());
        assertEquals("S", new Symbol("S-SBJ=OBJ").trimSymbol().getName());
    }
}