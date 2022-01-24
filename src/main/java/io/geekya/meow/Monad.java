package io.geekya.meow;

import java.util.function.Function;

public interface Monad<A, M extends Monad<?, M>> extends Applicative<A, M> {
    @Override
    <B> Monad<B, M> pure(B b);

    <B> Monad<B, M> bind(Function<? super A, ? extends Monad<B, M>> mf);
}
