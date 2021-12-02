package re;

import javax.management.RuntimeErrorException;

public class RE implements REInterface{
    private String input;

    public RE(String input) {
        this.input = input;
    }

    @Override
    public NFA getNFA() {
        return null;
    }

    private char peek() {
        return input.charAt(0);
    }

    private void eat(char c) {
        if (peek() == c) {
            input = input.substring(1);
        }
        else {
            throw new RuntimeException("Expected: " + c + "; got: " + peek());
        }
    }

    private char next() {
        char c = peek();
        eat(c);
        return c;
    }

    private boolean more() {
        return input.length() > 0;
    }
    
}
