package re;

import javax.management.RuntimeErrorException;

public class RE implements REInterface{
    private String input;
    private int count;

    public RE(String input) {
        this.input = input;
        this.count = 0;
    }

    @Override
    public NFA getNFA() {
        return null;
    }

    private NFA regex() {
        NFA term = term();

        if (more() && peek() == '|') {
            eat('|');
            NFA regex = regex();

            NFA combined = new NFA();
            String name = "q" + count++;
            combined.addStartState(name);

            combined.addNFAState(regex.getStates());
            combined.addNFAState(term.getStates());

            combined.addAbc(regex.getABC());
            combined.addAbc(term.getABC());

            combined.addTransition(name, 'e', regex.getStartState().getName());
            combined.addTransition(name, 'e', term.getStartState().getName());

            return combined;
        }
        else {
            return term;
        }
    }

    private NFA term() {
        NFA factor = new NFA();

        while (more() && peek() != ')' && peek() != '|') {
            NFA nextFactor = factor();
            if (nextFactor.getStates().isEmpty()) {
                factor = nextFactor;
            }
            else {
                factor = concat(factor, nextFactor);
            }
        }
        return factor;
    }

    private NFA concat(NFA n1, NFA n2) {
        String n2Start = n2.getStartState().getName();
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
    private NFA root() {
    
        if (peek() == '(') {
        eat('(');
        NFA reg1 = regEx();
        eat(')');
        return reg1;
        }
        
        return newNFA(next());
         
     }
    private boolean more() {
        return input.length() > 0;
    }
    
}