package ru.spbau.functional;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by airvan21 on 21.03.16.
 */
public class CollectionsTest {
    private final static Predicate<Integer> isEven  = arg -> arg % 2 == 0;
    private final static Predicate<Integer> isOdd   = arg -> arg % 2 != 0;
    private final static Predicate<FootballTeam> isFC = arg -> true;

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

        list.forEach(item -> checkEven.add(isEven.apply(item)));
        set.forEach(item -> checkOdd.add(isOdd.apply(item)));
        queue.forEach(item -> checkType.add(isFC.apply(item)));

        assertEquals(checkEven, Collections.map(isEven, list));
        assertEquals(checkOdd, Collections.map(isOdd, set));
        assertEquals(checkType, Collections.map(isFC, queue));
    }

    @Test
    public void testFilter() {
        final List<Integer> checkEven = new ArrayList<>();
        final List<Integer> checkOdd = new ArrayList<>();
        final List<Chelsea> checkType = new ArrayList<>();

        list.forEach(item -> { if (isEven.apply(item)) checkEven.add(item); });
        set.forEach(item -> { if (isOdd.apply(item))  checkOdd.add(item); });
        queue.forEach(item -> { if (isFC.apply(item))  checkType.add(item); });

        assertEquals(checkEven, Collections.filter(isEven, list));
        assertEquals(checkOdd, Collections.filter(isOdd, set));
        assertEquals(checkType, Collections.filter(isFC, queue));
    }

    @Test
    public void testTakeWhile() {
        final List<Integer> checkEven = new ArrayList<>();
        final List<Integer> checkOdd = new ArrayList<>();
        final List<Chelsea> checkType = new ArrayList<>();

        for (int item : list) {
            if (!isEven.apply(item)) {
                break;
            }

            checkEven.add(item);
        }

        for (int item : list) {
            if (!isOdd.apply(item)) {
                break;
            }

            checkOdd.add(item);
        }

        for (Chelsea item : queue) {
            if (!isFC.apply(item)) {
                break;
            }

            checkType.add(item);
        }

        assertEquals(checkEven, Collections.takeWhile(isEven, list));
        assertEquals(checkOdd, Collections.takeWhile(isOdd, set));
        assertEquals(checkType, Collections.takeWhile(isFC, queue));
    }

    @Test
    public void testTakeUnless() {
        final List<Integer> checkEven = new ArrayList<>();
        final List<Integer> checkOdd  = new ArrayList<>();
        final List<Chelsea> checkType = new ArrayList<>();

        for (int item : list) {
            if (isEven.apply(item)) {
                break;
            }

            checkEven.add(item);
        }

        for (int item : list) {
            if (isOdd.apply(item)) {
                break;
            }

            checkOdd.add(item);
        }

        for (Chelsea item : queue) {
            if (isFC.apply(item)) {
                break;
            }

            checkType.add(item);
        }

        assertEquals(checkEven, Collections.takeUnless(isEven, list));
        assertEquals(checkOdd, Collections.takeUnless(isOdd, set));
        assertEquals(checkType, Collections.takeUnless(isFC, queue));
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

    private class FootballTeam {}
    private class Chelsea extends FootballTeam {}
}