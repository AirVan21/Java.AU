package ru.spbau.functional;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class CollectionsTest {
    private final static Predicate<Integer> IS_EVEN = arg -> arg % 2 == 0;
    private final static Predicate<Integer> IS_ODD = arg -> arg % 2 != 0;
    private final static Predicate<FootballTeam> IS_FC = arg -> true;

    private final List<Integer>  list  = new ArrayList<>();
    private final Set<Integer>   set   = new HashSet<>();
    private final Queue<Chelsea> queue = new ArrayDeque<>();

    private final static int LOAD_SIZE = 10;

    @Before
    public void beforeTest() {
        for (int i = 0; i < LOAD_SIZE; i++) {
            list.add(i);
            set.add(i);
        }

        queue.add(new Chelsea());
        queue.add(new Chelsea());
    }

    @Test
    public void testMap() {
        final List<Boolean> checkEven = new ArrayList<>();
        final List<Boolean> checkOdd  = new ArrayList<>();
        final List<Boolean> checkType = new ArrayList<>();

        list.forEach(item -> checkEven.add(IS_EVEN.apply(item)));
        set.forEach(item -> checkOdd.add(IS_ODD.apply(item)));
        queue.forEach(item -> checkType.add(IS_FC.apply(item)));

        assertEquals(checkEven, Collections.map(IS_EVEN, list));
        assertEquals(checkOdd, Collections.map(IS_ODD, set));
        assertEquals(checkType, Collections.map(IS_FC, queue));
    }

    @Test
    public void testFilter() {
        final List<Integer> checkEven = new ArrayList<>();
        final List<Integer> checkOdd = new ArrayList<>();
        final List<Chelsea> checkType = new ArrayList<>();

        list.forEach(item -> { if (IS_EVEN.apply(item)) checkEven.add(item); });
        set.forEach(item -> { if (IS_ODD.apply(item))  checkOdd.add(item); });
        queue.forEach(item -> { if (IS_FC.apply(item))  checkType.add(item); });

        assertEquals(checkEven, Collections.filter(IS_EVEN, list));
        assertEquals(checkOdd, Collections.filter(IS_ODD, set));
        assertEquals(checkType, Collections.filter(IS_FC, queue));
    }

    @Test
    public void testTakeWhile() {
        final List<Integer> checkEven = new ArrayList<>();
        final List<Integer> checkOdd = new ArrayList<>();
        final List<Chelsea> checkType = new ArrayList<>();

        for (int item : list) {
            if (!IS_EVEN.apply(item)) {
                break;
            }

            checkEven.add(item);
        }

        for (int item : list) {
            if (!IS_ODD.apply(item)) {
                break;
            }

            checkOdd.add(item);
        }

        for (Chelsea item : queue) {
            if (!IS_FC.apply(item)) {
                break;
            }

            checkType.add(item);
        }

        assertEquals(checkEven, Collections.takeWhile(IS_EVEN, list));
        assertEquals(checkOdd, Collections.takeWhile(IS_ODD, set));
        assertEquals(checkType, Collections.takeWhile(IS_FC, queue));
    }

    @Test
    public void testTakeUnless() {
        final List<Integer> checkEven = new ArrayList<>();
        final List<Integer> checkOdd  = new ArrayList<>();
        final List<Chelsea> checkType = new ArrayList<>();

        for (int item : list) {
            if (IS_EVEN.apply(item)) {
                break;
            }

            checkEven.add(item);
        }

        for (int item : list) {
            if (IS_ODD.apply(item)) {
                break;
            }

            checkOdd.add(item);
        }

        for (Chelsea item : queue) {
            if (IS_FC.apply(item)) {
                break;
            }

            checkType.add(item);
        }

        assertEquals(checkEven, Collections.takeUnless(IS_EVEN, list));
        assertEquals(checkOdd, Collections.takeUnless(IS_ODD, set));
        assertEquals(checkType, Collections.takeUnless(IS_FC, queue));
    }

    @Test
    public void testFoldl() {
        final Function2<Integer, Integer, Integer> minus = (arg1, arg2) -> arg1 - arg2;
        final Function2<Integer, Integer, Integer> plus  = (arg1, arg2) -> arg1 + arg2;

        final List<Integer> integerList = Arrays.asList(7, 5, 3, -1);
        assertEquals(new Integer(10 - 7 - 5 - 3 + 1), Collections.foldl(minus, 10, integerList));

        int sum = 0;
        for (int item : set) {
            sum += item;
        }

        assertEquals(new Integer(sum), Collections.foldl(plus, 0, set));
    }

    @Test
    public void testFoldr() {
        final Function2<Integer, Integer, Integer> minus = (arg1, arg2) -> arg1 - arg2;
        final Function2<Integer, Integer, Integer> plus  = (arg1, arg2) -> arg1 + arg2;

        final List<Integer> integerList = Arrays.asList(7, 5, 3, -1);
        assertEquals(new Integer(7 - (5 - (3 - (-1 - 10)))), Collections.foldr(minus, 10, integerList));

        int sum = 0;
        for (int item : set) {
            sum += item;
        }

        assertEquals(new Integer(sum), Collections.foldr(plus, 0, set));
    }

    private static class FootballTeam {}
    private static class Chelsea extends FootballTeam {}
}