package ru.spbau.functional;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class PredicateTest {
    private final static Predicate<Integer> IS_EVEN = arg -> arg % 2 == 0;
    private final static Predicate<Integer> IS_ODD = arg -> arg % 2 != 0;
    private final static Predicate<Integer> IS_DIGIT = arg -> (arg >= 0) && (arg < 10);

    private final static Predicate<FootballTeam> IS_FC = arg -> true;
    private final static Predicate<FootballTeam> IS_BC = arg -> false;
    private final static Predicate<Object>       FAIL = arg -> { fail(); return false; };

    private final static int EVEN_NUMBER = 2;
    private final static int ODD_NUMBER = 3;
    private final static int ODD_NON_DIGIT = 11;

    @Test
    public void testOr() {
        assertTrue(IS_EVEN.or(IS_ODD).apply(EVEN_NUMBER));
        assertTrue(IS_EVEN.or(IS_ODD).apply(ODD_NUMBER));

        assertTrue(IS_ODD.or(IS_EVEN).apply(EVEN_NUMBER));
        assertTrue(IS_ODD.or(IS_EVEN).apply(ODD_NUMBER));

        assertFalse(IS_EVEN.or(IS_DIGIT).apply(ODD_NON_DIGIT));
        assertFalse(IS_DIGIT.or(IS_EVEN).apply(ODD_NON_DIGIT));

        assertTrue(IS_FC.or(IS_BC).apply(new Chelsea()));

        // laziness test
        assertTrue(IS_EVEN.or(FAIL).apply(EVEN_NUMBER));
    }

    @Test
    public void testAnd() {
        assertTrue(IS_EVEN.and(IS_DIGIT).apply(EVEN_NUMBER));
        assertTrue(IS_DIGIT.and(IS_EVEN).apply(EVEN_NUMBER));

        assertFalse(IS_DIGIT.and(IS_EVEN).apply(ODD_NUMBER));
        assertFalse(IS_EVEN.and(IS_DIGIT).apply(ODD_NUMBER));

        assertFalse(IS_FC.and(IS_BC).apply(new Chelsea()));

        // laziness test
        assertFalse(IS_EVEN.and(FAIL).apply(ODD_NUMBER));
    }

    @Test
    public void testNot() {
        assertTrue(IS_DIGIT.not().apply(ODD_NON_DIGIT));
        assertTrue(IS_EVEN.not().apply(ODD_NUMBER));

        assertFalse(IS_ODD.not().apply(ODD_NUMBER));
        assertFalse(IS_EVEN.not().apply(EVEN_NUMBER));

        assertTrue(IS_BC.not().apply(new Chelsea()));
    }

    private static class FootballTeam {}
    private static class Chelsea extends FootballTeam {}
}