package ru.spbau.functional;

import org.junit.Test;
import org.omg.PortableInterceptor.INACTIVE;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class Function2Test {

    private final Function2<Integer, Integer, Integer> sum  = (arg1, arg2) -> arg1 + arg2;
    private final Function2<Integer, Integer, Integer> diff = (arg1, arg2) -> arg1 - arg2;
    private final Function2<Integer, Integer, Integer> mult = (arg1, arg2) -> arg1 * arg2;
    private final Function1<Integer, Integer>          inc  = arg -> arg + 1;

    private final int TWO_CONST = 2;
    private final int FIVE_CONST = 5;

    @Test
    public void testApply() {
        assertEquals(TWO_CONST + FIVE_CONST, (int) sum.apply(TWO_CONST, FIVE_CONST));
        assertEquals(TWO_CONST * FIVE_CONST, (int) mult.apply(TWO_CONST, FIVE_CONST));
    }

    @Test
    public void testCompose() {
        assertEquals(TWO_CONST + FIVE_CONST + 1, (int) sum.compose(inc).apply(TWO_CONST, FIVE_CONST));
        assertEquals(TWO_CONST * FIVE_CONST + 1, (int) mult.compose(inc).apply(TWO_CONST, FIVE_CONST));
    }

    @Test
    public void testBind1() {
        Function1<Integer, Integer> addTwo = sum.bind1(TWO_CONST);

        assertEquals(TWO_CONST + FIVE_CONST, (int) addTwo.apply(FIVE_CONST));
        assertEquals(TWO_CONST, (int) addTwo.apply(0));
        assertNotEquals(TWO_CONST, (int) addTwo.apply(-TWO_CONST));
    }

    @Test
    public void testBind2() {
        Function1<Integer, Integer> minusTwo = diff.bind2(TWO_CONST);

        assertEquals(FIVE_CONST, (int) minusTwo.apply(FIVE_CONST + TWO_CONST));
        assertEquals(0, (int) minusTwo.apply(TWO_CONST));
        assertNotEquals(TWO_CONST, (int) minusTwo.apply(-TWO_CONST));
    }

    @Test
    public void testCurry() {
        assertEquals(sum.apply(TWO_CONST, FIVE_CONST), sum.curry().apply(TWO_CONST).apply(FIVE_CONST));
        assertEquals(sum.apply(FIVE_CONST, TWO_CONST), sum.curry().apply(TWO_CONST).apply(FIVE_CONST));

        assertEquals(diff.apply(FIVE_CONST, TWO_CONST), diff.curry().apply(FIVE_CONST).apply(TWO_CONST));
        assertNotEquals(diff.apply(FIVE_CONST, TWO_CONST), diff.curry().apply(TWO_CONST).apply(FIVE_CONST));
    }
}