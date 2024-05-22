package ParseTree;

public class ConstituentSpan {
    private final Symbol constituent;
    private final int start;
    private final int end;

    /**
     * Constructor for the ConstituentSpan class. ConstituentSpan is a structure for storing constituents or phrases in
     * a sentence with a specific label. Sets the attributes.
     * @param constituent Label of the span.
     * @param start Start index of the span.
     * @param end End index of the span.
     */
    public ConstituentSpan(Symbol constituent, int start, int end) {
        this.constituent = constituent;
        this.start = start;
        this.end = end;
    }

    /**
     * Accessor for the constituent attribute
     * @return Current constituent
     */
    public Symbol getConstituent() {
        return constituent;
    }

    /**
     * Accessor for the start attribute
     * @return Current start
     */
    public int getStart() {
        return start;
    }

    /**
     * Accessor for the end attribute
     * @return Current end
     */
    public int getEnd() {
        return end;
    }
}
