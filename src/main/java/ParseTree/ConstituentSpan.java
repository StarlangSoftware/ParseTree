package ParseTree;

public class ConstituentSpan {
    private final Symbol constituent;
    private final int start;
    private final int end;

    public Symbol getConstituent() {
        return constituent;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public ConstituentSpan(Symbol constituent, int start, int end) {
        this.constituent = constituent;
        this.start = start;
        this.end = end;
    }
}
