package ru.spbau.functional;

/**
 * Created by airvan21 on 20.03.16.
 */
public interface Function1<A, R> {
    R apply(A arg);

    default <R1> Function1<A, R1> compose(Function1<? super R, R1> g) {
        return arg -> g.apply(apply(arg));
    }
}
