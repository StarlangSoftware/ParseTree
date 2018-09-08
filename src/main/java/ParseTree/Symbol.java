package ParseTree;

import Dictionary.Word;

import java.util.ArrayList;
import java.util.Arrays;

public class Symbol extends Word {
    ArrayList<String> nonTerminalList = new ArrayList<String>(Arrays.asList("ADJP", "ADVP", "CC", "CD", "CONJP", "DT", "EX", "FRAG", "FW", "IN", "INTJ", "JJ", "JJR", "JJS", "LS",
            "LST", "MD", "NAC", "NN", "NNP", "NNPS", "NNS", "NP", "NX", "PDT", "POS", "PP", "PRN", "PRP", "PRP$", "PRT",
            "PRT|ADVP", "QP", "RB", "RBR", "RP", "RRC", "S", "SBAR", "SBARQ", "SINV", "SQ", "SYM", "TO", "UCP", "UH", "VB", "VBD", "VBG", "VBN",
            "VBP", "VBZ", "VP", "WDT", "WHADJP", "WHADVP", "WHNP", "WP", "WP$", "WRB", "X", "-NONE-"));
    ArrayList<String> phraseLabels = new ArrayList<String>(
            Arrays.asList("NP", "PP", "ADVP", "ADJP", "CC", "VG"));
    ArrayList<String> sentenceLabels = new ArrayList<String>(
            Arrays.asList("SINV","SBARQ","SBAR","SQ","S"));
    ArrayList<String> verbLabels = new ArrayList<String>(
            Arrays.asList("VB", "VBD", "VBG", "VBN","VBP", "VBZ", "VERB"));
    String VPLabel = "VP";

    public Symbol(String name){
        super(name);
    }

    public boolean isVerb(){
        return verbLabels.contains(name);
    }

    public boolean isVP(){
        if (name.equals(VPLabel)){
            return true;
        }
        return false;
    }

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

    public boolean isChunkLabel(){
        if (Word.isPunctuation(name) || sentenceLabels.contains(name.replaceAll("-.*","")) || phraseLabels.contains(name.replaceAll("-.*", "")))
            return true;
        return false;
    }

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
