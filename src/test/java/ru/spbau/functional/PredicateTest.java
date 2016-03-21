package ru.spbau.functional;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class PredicateTest {
    private final Predicate<Integer> isEven  = arg -> arg % 2 == 0;
    private final Predicate<Integer> isOdd   = arg -> arg % 2 != 0;
    private final Predicate<Integer> isDigit = arg -> (arg >= 0) && (arg < 10);

    private final Predicate<FootballTeam> isFC = arg -> true;
    private final Predicate<FootballTeam> isBC = arg -> false;

    private final int EVEN_NUMBER = 2;
    private final int ODD_NUMBER = 3;
    private final int ODD_NON_DIGIT = 11;

    @Test
    public void testOr() {
        assertTrue(isEven.or(isOdd).apply(EVEN_NUMBER));
        assertTrue(isEven.or(isOdd).apply(ODD_NUMBER));

        assertTrue(isOdd.or(isEven).apply(EVEN_NUMBER));
        assertTrue(isOdd.or(isEven).apply(ODD_NUMBER));

        assertFalse(isEven.or(isDigit).apply(ODD_NON_DIGIT));
        assertFalse(isDigit.or(isEven).apply(ODD_NON_DIGIT));

        assertTrue(isFC.or(isBC).apply(new Chelsea()));
    }

    @Test
    public void testAnd() {
        assertTrue(isEven.and(isDigit).apply(EVEN_NUMBER));
        assertTrue(isDigit.and(isEven).apply(EVEN_NUMBER));

        assertFalse(isDigit.and(isEven).apply(ODD_NUMBER));
        assertFalse(isEven.and(isDigit).apply(ODD_NUMBER));

        assertFalse(isFC.and(isBC).apply(new Chelsea()));
    }

    @Test
    public void testNot() {
        assertTrue(isDigit.not().apply(ODD_NON_DIGIT));
        assertTrue(isEven.not().apply(ODD_NUMBER));

        assertFalse(isOdd.not().apply(ODD_NUMBER));
        assertFalse(isEven.not().apply(EVEN_NUMBER));

        assertTrue(isBC.not().apply(new Chelsea()));
    }

    private class FootballTeam {}
    private class Chelsea extends FootballTeam {}
}