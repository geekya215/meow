package io.geekya.meow;

import java.util.function.Function;

public interface Applicative<A, App extends Applicative<?, App>> extends Functor<A, App> {
    <B> Applicative<B, App> pure(B b);

    <B> Applicative<B, App> fmap(Applicative<Function<? super A, ? extends B>, App> af);
}
