package ParseTree;

public class ConstituentSpan {
    private Symbol constituent;
    private int start;
    private int end;

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
