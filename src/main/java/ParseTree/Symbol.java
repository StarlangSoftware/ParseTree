package ParseTree;

import Dictionary.Word;

import java.util.ArrayList;
import java.util.Arrays;

public class Symbol extends Word {
    ArrayList<String> nonTerminalList = new ArrayList<>(Arrays.asList("ADJP", "ADVP", "CC", "CD", "CONJP", "DT", "EX", "FRAG", "FW", "IN", "INTJ", "JJ", "JJR", "JJS", "LS",
            "LST", "MD", "NAC", "NN", "NNP", "NNPS", "NNS", "NP", "NX", "PDT", "POS", "PP", "PRN", "PRP", "PRP$", "PRT",
            "PRT|ADVP", "QP", "RB", "RBR", "RP", "RRC", "S", "SBAR", "SBARQ", "SINV", "SQ", "SYM", "TO", "UCP", "UH", "VB", "VBD", "VBG", "VBN",
            "VBP", "VBZ", "VP", "WDT", "WHADJP", "WHADVP", "WHNP", "WP", "WP$", "WRB", "X", "-NONE-"));
    ArrayList<String> phraseLabels = new ArrayList<>(
            Arrays.asList("NP", "PP", "ADVP", "ADJP", "CC", "VG"));
    ArrayList<String> sentenceLabels = new ArrayList<>(
            Arrays.asList("SINV","SBARQ","SBAR","SQ","S"));
    ArrayList<String> verbLabels = new ArrayList<>(
            Arrays.asList("VB", "VBD", "VBG", "VBN","VBP", "VBZ", "VERB"));
    String VPLabel = "VP";

    /**
     * Constructor for Symbol class. Sets the name attribute.
     * @param name Name attribute
     */
    public Symbol(String name){
        super(name);
    }

    /**
     * Checks if this symbol is a verb type.
     * @return True if the symbol is a verb, false otherwise.
     */
    public boolean isVerb(){
        return verbLabels.contains(name);
    }

    /**
     * Checks if the symbol is VP or not.
     * @return True if the symbol is VB, false otherwise.
     */
    public boolean isVP(){
        return name.equals(VPLabel);
    }

    /**
     * Checks if this symbol is a terminal symbol or not. A symbol is terminal if it is a punctuation symbol, or
     * if it starts with a lowercase symbol.
     * @return True if this symbol is a terminal symbol, false otherwise.
     */
    public boolean isTerminal(){
        int i;
        if (name.equals(",") || name.equals(".") || name.equals("!") || name.equals("?") || name.equals(":")
                || name.equals(";") || name.equals("\"") || name.equals("''") || name.equals("'") || name.equals("`")
                || name.equals("``") || name.equals("...") || name.equals("-") || name.equals("--"))
            return true;
        if (nonTerminalList.contains(name))
            return false;
        if (name.equals("I") || name.equals("A"))
            return true;
        for (i = 0; i < name.length(); i++){
            if (name.charAt(i) >= 'a' && name.charAt(i) <= 'z'){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if this symbol can be a chunk label or not.
     * @return True if this symbol can be a chunk label, false otherwise.
     */
    public boolean isChunkLabel(){
        return Word.isPunctuation(name) || sentenceLabels.contains(name.replaceAll("-.*", "")) || phraseLabels.contains(name.replaceAll("-.*", ""));
    }

    /**
     * If the symbol's data contains '-' or '=', this method trims all characters after those characters and returns
     * the resulting string.
     * @return Trimmed symbol.
     */
    public Symbol trimSymbol(){
        int minusIndex, equalIndex;
        if (name.startsWith("-") || (!name.contains("-") && !name.contains("="))){
            return this;
        }
        minusIndex = name.indexOf('-');
        equalIndex = name.indexOf('=');
        if (minusIndex != -1 || equalIndex != -1){
            if (minusIndex != -1 && equalIndex != -1){
                if (minusIndex < equalIndex){
                    return new Symbol(name.substring(0, minusIndex));
                } else {
                    return new Symbol(name.substring(0, equalIndex));
                }
            } else {
                if (minusIndex != -1){
                    return new Symbol(name.substring(0, minusIndex));
                } else {
                    return new Symbol(name.substring(0, equalIndex));
                }
            }
        } else {
            return this;
        }
    }

    @Override public boolean equals(Object aThat) {
        if (this == aThat)
            return true;
        if (!(aThat instanceof Symbol))
            return false;
        Symbol that = (Symbol)aThat;
        return that.name.equals(name);
    }

}
