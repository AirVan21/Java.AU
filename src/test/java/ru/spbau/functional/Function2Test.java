package ru.spbau.functional;

import org.junit.Test;
import org.omg.PortableInterceptor.INACTIVE;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class Function2Test {

    private final static Function2<Integer, Integer, Integer> sum  = (arg1, arg2) -> arg1 + arg2;
    private final static Function2<Integer, Integer, Integer> diff = (arg1, arg2) -> arg1 - arg2;
    private final static Function2<Integer, Integer, Integer> mult = (arg1, arg2) -> arg1 * arg2;
    private final static Function1<Integer, Integer>          inc  = arg -> arg + 1;

    private final static Function2<FootballTeam, FootballTeam, String> greet = (arg1, arg2) -> "Match day!";

    private final static int TWO_CONST = 2;
    private final static int FIVE_CONST = 5;

    @Test
    public void testApply() {
        assertEquals(TWO_CONST + FIVE_CONST, (int) sum.apply(TWO_CONST, FIVE_CONST));
        assertEquals(TWO_CONST * FIVE_CONST, (int) mult.apply(TWO_CONST, FIVE_CONST));
        assertEquals("Match day!", greet.apply(new Chelsea(), new FootballTeam()));
    }

    @Test
    public void testCompose() {
        assertEquals(TWO_CONST + FIVE_CONST + 1, (int) sum.compose(inc).apply(TWO_CONST, FIVE_CONST));
        assertEquals(TWO_CONST * FIVE_CONST + 1, (int) mult.compose(inc).apply(TWO_CONST, FIVE_CONST));
        assertEquals("Match day!".length(), (int) greet.compose(String::length).apply(new Chelsea(), new Arsenal()));
    }

    @Test
    public void testBind1() {
        Function1<Integer, Integer> addTwo = sum.bind1(TWO_CONST);
        Function1<FootballTeam, String> matchOnStamfordBridge = greet.bind1(new Chelsea());

        assertEquals(TWO_CONST + FIVE_CONST, (int) addTwo.apply(FIVE_CONST));
        assertEquals(TWO_CONST, (int) addTwo.apply(0));
        assertNotEquals(TWO_CONST, (int) addTwo.apply(-TWO_CONST));
        assertEquals("Match day!", matchOnStamfordBridge.apply(new Arsenal()));
    }

    @Test
    public void testBind2() {
        Function1<Integer, Integer> minusTwo = diff.bind2(TWO_CONST);
        Function1<FootballTeam, String> matchOnStamfordBridge = greet.bind2(new Chelsea());

        assertEquals(FIVE_CONST, (int) minusTwo.apply(FIVE_CONST + TWO_CONST));
        assertEquals(0, (int) minusTwo.apply(TWO_CONST));
        assertNotEquals(TWO_CONST, (int) minusTwo.apply(-TWO_CONST));
        assertEquals("Match day!", matchOnStamfordBridge.apply(new Arsenal()));
    }

    @Test
    public void testCurry() {
        assertEquals(sum.apply(TWO_CONST, FIVE_CONST), sum.curry().apply(TWO_CONST).apply(FIVE_CONST));
        assertEquals(sum.apply(FIVE_CONST, TWO_CONST), sum.curry().apply(TWO_CONST).apply(FIVE_CONST));

        assertEquals(diff.apply(FIVE_CONST, TWO_CONST), diff.curry().apply(FIVE_CONST).apply(TWO_CONST));
        assertNotEquals(diff.apply(FIVE_CONST, TWO_CONST), diff.curry().apply(TWO_CONST).apply(FIVE_CONST));

        assertEquals(greet.apply(new Chelsea(), new FootballTeam()), greet.curry().apply(new Chelsea()).apply(new FootballTeam()));
    }

    private class FootballTeam {}
    private class Chelsea extends FootballTeam {}
    private class Arsenal extends FootballTeam {}
}