import java.util.ArrayList;
import java.util.List;

//Built upon a basic lexical analyzer

public class WordSplitter {

    private boolean isSpecialCharacter(char c) {
        return !Character.isLetterOrDigit(c);
    }

    private final String input;
    private int pos = 0;

    public WordSplitter(String input) {
        this.input = input;
    }

    public List<String> split() {
        List<String> words = new ArrayList<>();
        while (pos < input.length()) {
            char current = peek();
            if (Character.isWhitespace(current)) {
                advance();
                continue; // skip whitespace
            }
            if (Character.isLetter(current)) {
                words.add(readIdentifier());
            } else if (Character.isDigit(current)) {
                advance();
            } else if (isSpecialCharacter(current)) {
                advance();
            } else {
                throw new RuntimeException("Unexpected character: " + current);
            }
        }
        return words;
    }
    private void readAfterApostrophes(StringBuilder sb) { //doing this recursively to handle cases like "O’Reilly’s"
        while (pos < input.length() && (Character.isLetterOrDigit(peek()) )) {
            sb.append(advance());
        }
        try{
            char current = peek();
            if (current == '\'' || current=='’') {//get the stuff after the apostrophe
                advance();
                if (Character.isLetterOrDigit(peek())) {
                    sb.append(current);
                    readAfterApostrophes(sb);
                }

            }
        }
        catch(StringIndexOutOfBoundsException ignored){
        }
    }
    private void readAfterOther(StringBuilder sb, char originalChar) {
        while (pos < input.length() && (Character.isLetterOrDigit(peek()))) {
            sb.append(advance());
        }
        try{
            char current = peek();
            if (current == originalChar) {//get the stuff after the char
                advance();
                if (Character.isLetterOrDigit(peek())) {
                    sb.append(current);
                    readAfterOther(sb,originalChar);
                }
                else if (current == '.' ){ //end of like, abbreviations
                    sb.append(current);
                }
            }
            else if (current == '\'' || current=='’') {//get the stuff after the  just in case its like mother-in-law's
                advance();
                if (Character.isLetterOrDigit(peek())) {
                    sb.append(current);
                    readAfterApostrophes(sb);
                }

            }
        }
        catch(StringIndexOutOfBoundsException ignored){
        }
    }

    private String readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (Character.isLetterOrDigit(peek()))) {
            sb.append(advance());
        }
        try{
            char current = peek();
            if (current == '\'' || current=='’') {//get the stuff after the apostrophe
                advance();
                if (Character.isLetterOrDigit(peek())) {
                    sb.append(current);
                    readAfterApostrophes(sb);
                }
            }
            else if (current == '.' || current == '-' || current == '_' || current == '–') {//is this a hyphenation/apostrophe/underscoring?
                char original = advance();
                if (Character.isLetterOrDigit(peek())) {
                    sb.append(original);
                    readAfterOther(sb, original);
                }

            }
        }
        catch(StringIndexOutOfBoundsException ignored){
        }
        return sb.toString();
    }


    private char peek() {
        return input.charAt(pos);
    }

    private char advance() {
        return input.charAt(pos++);
    }

    public static void main(String[] args) {
        WordSplitter splitter = new WordSplitter("The quick brown fox jumps over the lazy dog, but mother-in-law's recipe—passed down from her great-grandfather's 1920s' cookbook—is state-of-the-art! Dr. O'Neil's study on pre-COVID-19 data (published in J. Med. Res., pp. 10–12) shows that AT&T's 5G's impact isn't just hype; it's re-shaping tech. 'Rock 'n' roll's golden era,' she mused, 'wasn't all peace-and-love—think counter-culture's don'ts versus do's.' A passer-by's hat (a $1,000.00 designer piece!) flew off during NYC's windstorm, while editors-in-chief's opinions varied wildly: 'It's 90% accurate,' vs. 'Re-examine the data!' McDonald's's sales rose post-2020, but ex-CEO's '10am meetings' policy caused chaos. Can your splitter handle co-owner's ambiguous contract clauses, pre-1990's tax laws, or phrases like 'it’s' vs. 'its'? Punctuation!!!—like this?!—or ellipses... test limits. Extra spaces, tabs, and weird\\u00A0gaps shouldn’t break it.");
        List<String> words = splitter.split();
        for (String word : words) {
            System.out.println(word);
        }
    }
}
