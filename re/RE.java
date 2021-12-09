package re;

import java.util.LinkedHashSet;
import java.util.Set;

import fa.State;
import fa.nfa.NFA;
import fa.nfa.NFAState;

/**
 * December 8th, 2021
 * This Class parses a regular expression, 
 * and creates an NFA using the input.
 * @author Jason Kuphaldt, Connor Jackson
 *
 */

public class RE implements REInterface{
    private String input;
    private int count;

    /**
     * 
     * @param input string that 
     * represents the regular expression.
     */
        public RE(String input) {
        this.input = input;
        this.count = 0;
    }

    
    /** 
     * returns the nfa 
     * based on the getNFA method
     * @return NFA 
     */
    @Override
    public NFA getNFA() {
        return regex();
    }

    
    /** 
     * Creates nfa aafter parsing 
     * through the regular expression
     * @return NFA
     */
    private NFA regex() {
        NFA term = term();

        if (more() && peek() == '|') {
            eat('|');
            NFA regex = regex();

            NFA combined = new NFA();
            String name = Integer.toString(count++);
            combined.addStartState(name);

            combined.addNFAStates(term.getStates());
            combined.addNFAStates(regex.getStates());
            
            combined.addTransition(name, 'e', term.getStartState().getName());
            combined.addTransition(name, 'e', regex.getStartState().getName());
            
            combined.addAbc(term.getABC());
            combined.addAbc(regex.getABC());
            

            return combined;
        }
        else {
            return term;
        }
    }

    
    /** 
     * Used to build the regular expression
     * @return NFA
     */
    private NFA term() {
        NFA factor = new NFA();

        while (more() && peek() != ')' && peek() != '|') {
            NFA nextFactor = factor();
            if (factor.getStates().isEmpty()) {
                factor = nextFactor;
            }
            else {
                factor = concat(factor, nextFactor);
            }
        }
        return factor;
    }

    
    /** 
     * returns base
     * @return NFA
     */
    private NFA factor() {

        NFA base = base();
        while (more() && peek() == '*') {
            eat('*');
            base = repetition(base);
        }
        return base;
    }

    
    /** 
     * This is the method that
     * will determine the ablity
     * of the * operator.
     * @param base 
     * @return NFA
     */
    private NFA repetition(NFA base) {
        NFA nfa = new NFA();

        String start = Integer.toString(count++);
        nfa.addStartState(start);

        String fin = Integer.toString(count++);
        nfa.addFinalState(fin);

        nfa.addNFAStates(base.getStates());

        nfa.addTransition(start, 'e', fin);
        nfa.addTransition(fin, 'e', base.getStartState().getName());
        nfa.addTransition(start, 'e', base.getStartState().getName());

        nfa.addAbc(base.getABC());

        for (State state: base.getFinalStates()) {
            nfa.addTransition(state.getName(), 'e', fin);

            for (State state2: nfa.getFinalStates()) {
                if(state2.getName().equals(state.getName())) {
                    ((NFAState)state2).setNonFinal();
                }
            }
        }

        

        return nfa;

    }

    
    /** 
     * concats two nfas together
     * to form the final nfa.
     * @param n1 first nfa
     * @param n2 second nfa
     * @return NFA
     */
    private NFA concat(NFA n1, NFA n2) {
        Set<State> n1Finals = n1.getFinalStates();

        n1.addNFAStates(n2.getStates());
        

        for (State state: n1Finals) {
            ((NFAState)state).setNonFinal();
            n1.addTransition(state.getName(), 'e', n2.getStartState().getName());
        }

        n1.addAbc(n2.getABC());
        return n1;
    }

    
    
    
    /** 
     * Makes an nfa with the 
     * character recieved in
     * the constructor
     * @param n character recieved as char
     * @return NFA
     */
    private NFA newNFA(char n) {
        NFA nfa = new NFA();
        
        String start = Integer.toString(count++);
        nfa.addStartState(start);
        
        String fin = Integer.toString(count++);
        nfa.addFinalState(fin);

        nfa.addTransition(start, n, fin);

        Set<Character> abc;
        abc = new LinkedHashSet<Character>();
        abc.add(n);
        nfa.addAbc(abc);
        
        return nfa;

    }

    
    /** 
     * This method checks for 
     * precedents based on the
     * "()" characters
     * @return NFA
     */
    private NFA base() {
        if (peek() == '(') {
            eat('(');
            NFA reg1 = regex();
            eat(')');
            return reg1;
        }
        
        return newNFA(next());
         
    }

    
    /** 
     * returns first character
     * of the regular expression
     * @return char
     */
    private char peek() {
        return input.charAt(0);
    }

    
    /** 
     * Ensures that non viable characters
     * do not get added to the regular
     * expression
     * @param c
     */
    private void eat(char c) {
        if (peek() == c) {
            input = input.substring(1);
        }
        else {
            throw new RuntimeException("Expected: " + c + "; got: " + peek());
        }
    }

    
    /** 
     * Removes character from
     * the regular expression 
     * and returns it.
     * @return char
     */
    private char next() {
        char c = peek();
        eat(c);
        return c;
    }
    
    
    /** 
     * Checks if there are more characters
     * left in the regular expression
     * @return boolean
     */
    private boolean more() {
        return input.length() > 0;
    }
    
}