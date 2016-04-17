package ru.spbau.functional;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class Function2Test {

    private final static Function2<Integer, Integer, Integer> SUM = (arg1, arg2) -> arg1 + arg2;
    private final static Function2<Integer, Integer, Integer> DIFF = (arg1, arg2) -> arg1 - arg2;
    private final static Function2<Integer, Integer, Integer> MULT = (arg1, arg2) -> arg1 * arg2;
    private final static Function1<Integer, Integer>          INC = arg -> arg + 1;

    private final static Function2<FootballTeam, FootballTeam, String> greet = (arg1, arg2) -> "Match day!";

    private final static int TWO_CONST = 2;
    private final static int FIVE_CONST = 5;

    @Test
    public void testApply() {
        assertEquals(TWO_CONST + FIVE_CONST, (int) SUM.apply(TWO_CONST, FIVE_CONST));
        assertEquals(TWO_CONST * FIVE_CONST, (int) MULT.apply(TWO_CONST, FIVE_CONST));
        assertEquals("Match day!", greet.apply(new Chelsea(), new FootballTeam()));
    }

    @Test
    public void testCompose() {
        assertEquals(TWO_CONST + FIVE_CONST + 1, (int) SUM.compose(INC).apply(TWO_CONST, FIVE_CONST));
        assertEquals(TWO_CONST * FIVE_CONST + 1, (int) MULT.compose(INC).apply(TWO_CONST, FIVE_CONST));
        assertEquals("Match day!".length(), (int) greet.compose(String::length).apply(new Chelsea(), new Arsenal()));
    }

    @Test
    public void testBind1() {
        Function1<Integer, Integer> addTwo = SUM.bind1(TWO_CONST);
        Function1<FootballTeam, String> matchOnStamfordBridge = greet.bind1(new Chelsea());

        assertEquals(TWO_CONST + FIVE_CONST, (int) addTwo.apply(FIVE_CONST));
        assertEquals(TWO_CONST, (int) addTwo.apply(0));
        assertNotEquals(TWO_CONST, (int) addTwo.apply(-TWO_CONST));
        assertEquals("Match day!", matchOnStamfordBridge.apply(new Arsenal()));
    }

    @Test
    public void testBind2() {
        Function1<Integer, Integer> minusTwo = DIFF.bind2(TWO_CONST);
        Function1<FootballTeam, String> matchOnStamfordBridge = greet.bind2(new Chelsea());

        assertEquals(FIVE_CONST, (int) minusTwo.apply(FIVE_CONST + TWO_CONST));
        assertEquals(0, (int) minusTwo.apply(TWO_CONST));
        assertNotEquals(TWO_CONST, (int) minusTwo.apply(-TWO_CONST));
        assertEquals("Match day!", matchOnStamfordBridge.apply(new Arsenal()));
    }

    @Test
    public void testCurry() {
        assertEquals(SUM.apply(TWO_CONST, FIVE_CONST), SUM.curry().apply(TWO_CONST).apply(FIVE_CONST));
        assertEquals(SUM.apply(FIVE_CONST, TWO_CONST), SUM.curry().apply(TWO_CONST).apply(FIVE_CONST));

        assertEquals(DIFF.apply(FIVE_CONST, TWO_CONST), DIFF.curry().apply(FIVE_CONST).apply(TWO_CONST));
        assertNotEquals(DIFF.apply(FIVE_CONST, TWO_CONST), DIFF.curry().apply(TWO_CONST).apply(FIVE_CONST));

        assertEquals(greet.apply(new Chelsea(), new FootballTeam()), greet.curry().apply(new Chelsea()).apply(new FootballTeam()));
    }

    private static class FootballTeam {}
    private static class Chelsea extends FootballTeam {}
    private static class Arsenal extends FootballTeam {}
}