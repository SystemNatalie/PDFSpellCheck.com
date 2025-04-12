import java.util.ArrayList;
import java.util.List;


enum TokenType {
    WORD, GIBBERISH, SEP
}

class Token {
    TokenType type;
    String value;

    Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }

    public String toString() {
        return type + ": " + value;
    }
}


public class WordSplitterType2{

    private String[] startsWithAposWords = {"twas","tis","neath","gainst"};
    private final String input;
    private int pos = 0;

    public WordSplitterType2(String input) {
        this.input = input;
    }
    private char peek() {
        return input.charAt(pos);
    }
    private char peek(int posOffset) {
        return input.charAt(pos+posOffset);
    }

    private char advance() {
        return input.charAt(pos++);
    }

    private Token readIdentifier() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (!Character.isWhitespace(peek()) && Character.isLetter(peek()))) {
            sb.append(advance());
        }
        //if (!Character.isLetterOrDigit(sb.charAt(sb.length()-1))) { //TODO/Discuss - I want to remove the "!" or "." or "," from the end of each word. My logic is the only time they matter are for abbreviations, and we can
        //    while (!Character.isLetterOrDigit(sb.charAt(sb.length()-1))){
        //        sb.deleteCharAt(sb.length()-1);
        //    }
        //}
        if (sb.toString().startsWith("Sarah")) {
            System.out.println("y");
        }
        return new Token(TokenType.WORD,sb.toString());
    }

    private Token readGibberish() {
        StringBuilder sb = new StringBuilder();
        while (pos < input.length() && (!Character.isWhitespace(peek())&&!Character.isLetter(peek()))) {
            sb.append(advance());
        }
        return new Token(TokenType.GIBBERISH,sb.toString());
    }

    public List<Token> split() {
        List<Token> words = new ArrayList<>();
        while (pos < input.length()) {
            char current = peek();

            if (Character.isWhitespace(current)) {//Skip whitespace after token.
                words.add(new Token(TokenType.SEP,String.valueOf(advance())));
            }
            else if (Character.isLetter(current)) { //We only care about words, so this grabs the token if it starts with a letter.
                words.add(readIdentifier());
            }
            else if (current == '\''|| current == '’') { //Some words happen to start with ', such as "'twas". We want to account for this so.
                if (Character.isLetter(peek(1))){
                    words.add(new Token(TokenType.GIBBERISH,String.valueOf(advance())));
                    words.add(readIdentifier());
                }
                else{
                    words.add(readGibberish());
                }
            }
            else{ //Other non valid starting input, such as digits.
                words.add(readGibberish());
            }


        }
        return words;
    }



    public static void main(String[] args) {
        WordSplitterType2 splitter = new WordSplitterType2("The quick brown fox jumps over the lazy dog. \"Can't believe it!\" she shouted—her voice echoing across the park.  \n" +
                "Here's a test: hyphenated-words, commas, periods, and ellipses... Wait—what about parentheses (like this)?  \n" +
                "\n" +
                "Numbers: 1, 2, 3.14, -40, $19.99, 10pm, 5am.  \n" +
                "Contractions: I'm, you're, he's, won't, shouldn't, o'clock, ma'am, y'all.  \n" +
                "Possessives: Sarah's, dogs', James's.  \n" +
                "\n" +
                "Abbreviations: Dr., Mr., Ms., U.S.A., etc., e.g., i.e., Ph.D., vs., Ave., St.  \n" +
                "Symbols: & (and), @ (at), # (hashtag), % (percent).  \n" +
                "\n" +
                "Hyphenated Edge Cases:  \n" +
                "- state-of-the-art  \n" +
                "- mother-in-law  \n" +
                "- pre-approved  \n" +
                "- up-to-date  \n" +
                "- twenty-three  \n" +
                "\n" +
                "Punctuation Stress Test:  \n" +
                "\"Wow!\" said the man. \"Really?! This costs $1,000.00—but why?\" She paused... then added: \"Never mind.\"  \n" +
                "\n" +
                "Weird Cases:  \n" +
                "- A single 'quote' inside words.  \n" +
                "- Double \"quotes\" around phrases.  \n" +
                "- Periods mid.sentence (e.g., abbreviations).  \n" +
                "- Commas, like this, disrupt flow.  \n" +
                "\n" +
                "Whitespace Tests:  \n" +
                "Extra    spaces between   words.  \n" +
                "Tabs→\tbetween→\twords.  \n" +
                "Newlines\\nalso\\nbreak\\nthings.  \n" +
                "\n" +
                "Final Challenge:  \n" +
                "Can your splitter handle this?: \"I'd say it's 90% accurate—maybe 95%, e.g., in Dr. Smith's study (see pp. 10-12).\"  ");
        List<Token> words = splitter.split();
        for (Token word : words) {
            System.out.println(word.toString());
        }
    }
}
