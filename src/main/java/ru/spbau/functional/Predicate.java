package ru.spbau.functional;

/**
 * Created by airvan21 on 20.03.16.
 */
public interface Predicate<A> extends Function1<A, Boolean> {

    Predicate<Object> ALWAYS_TRUE  = arg -> true;
    Predicate<Object> ALWAYS_FALSE = arg -> false;

    default Predicate<A> or(Predicate<? super A> predicate) {
        return arg -> apply(arg) || predicate.apply(arg);
    }

    default Predicate<A> and(Predicate<? super A> predicate) {
        return arg -> apply(arg) && predicate.apply(arg);
    }

    default Predicate<A> not() {
        return arg -> !apply(arg);
    }
}
