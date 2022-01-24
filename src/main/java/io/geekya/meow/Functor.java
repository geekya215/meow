package io.geekya.meow;

import java.util.function.Function;

@FunctionalInterface
public interface Functor<A, F extends Functor<?, F>> {
    <B> Functor<B, F> map(Function<? super A, ? extends B> f);
}
