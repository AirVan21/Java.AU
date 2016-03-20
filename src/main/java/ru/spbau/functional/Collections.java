package ru.spbau.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by airvan21 on 21.03.16.
 */
public class Collections {

    public static <A, R> List<R> map(Function1<? super A, R> mapper, Iterable<A> container) {
        List<R> result = new ArrayList<>();
        container.forEach(item -> result.add(mapper.apply(item)));

        return result;
    }

    public static <A> List<A> filter(Predicate<? super A> checker, Iterable<A> container) {
        List<A> result = new ArrayList<>();
        container.forEach(item -> { if (checker.apply(item)) result.add(item); } );

        return result;
    }

    public static <A> List<A> takeWhile(Predicate<? super A> checker, Iterable<A> container) {
        List<A> result = new ArrayList<>();

        for (A item : container) {
            if (!checker.apply(item)) {
                break;
            }

            result.add(item);
        }

        return result;
    }

    public static <A> List<A> takeUnless(Predicate<? super A> checker, Iterable<A> container) {
        return takeWhile(checker.not(), container);
    }

    /**
     * Logic: foldl :: (A1 -> A2 -> A1) -> A1 -> [A2] -> A1
     */
    public static <A1, A2> A1 foldl(Function2<A1, A2, A1> f, A1 base, Iterable<A2> container) {
        A1 accumulator = base;

        for (A2 arg2 : container) {
            accumulator = f.apply(accumulator, arg2);
        }

        return accumulator;
    }

    /**
     * Logic: foldr :: (A1 -> A2 -> A2) -> A2 -> [A1] -> A2
     */
    public static <A1, A2> A2 foldr(Function2<A1, A2, A2> f, A2 base, Iterable<A1> container) {
        return foldr(f, base, container.iterator());
    }

    private static <A1, A2> A2 foldr(Function2<A1, A2, A2> f, A2 base, Iterator<A1> it) {
        if (it.hasNext()) {
            return f.apply(it.next(), foldr(f, base, it));
        } else {
            return base;
        }
    }
}
