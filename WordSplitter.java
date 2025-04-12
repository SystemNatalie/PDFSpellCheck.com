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
        WordSplitter splitter = new WordSplitter("Word-splitter, test! Can you handle:  \n" +
                "1. \"Complex 'nested' quotes,\" she asked?  \n" +
                "2. Emails (e.g., user_name+filter@sub.domain.co.uk)  \n" +
                "3. Currency: $1,000.00 / €500,50 / ¥100万  \n" +
                "4. Strange-hyphenated–words—like this…  \n" +
                "5. Math: 2.3e-5 ≈ 3.14 × 10² (even ±5%)  \n" +
                "6. Unicode: 日本語, русский, ελληνικά, 汉字  \n" +
                "7. Contractions: can’t, shouldn’ve, o’clock  \n" +
                "8. Abbrvs.: Ph.D., Mr., U.S.A., 10am, 5pm  \n" +
                "9. Code/tech: #hashtag, @mention, <html>, JSON{}  \n" +
                "10. Emoji/symbols: \uD83D\uDC4D \uD83D\uDD25 ⚡ © ® ™  \n" +
                "11. Whitespace:\\t tabs,\\r\\n newlines,\\v weird\\u00A0spaces  \n" +
                "12. Edge!!! cases??? (e.g., ..., ---, [[[ ]]])  ");
        List<String> words = splitter.split();
        for (String word : words) {
            System.out.println(word);
        }
    }
}
