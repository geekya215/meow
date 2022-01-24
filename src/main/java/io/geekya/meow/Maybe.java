package io.geekya.meow;

import java.util.function.Function;

public class Maybe<A> implements Monad<A, Maybe<?>> {
    private Maybe() {
    }

    @SuppressWarnings("unchecked")
    public static <A> Maybe<A> nothing() {
        return (Maybe<A>) Nothing.INSTANCE;
    }

    public static <A> Maybe<A> just(A a) {
        return new Just<>(a);
    }

    public static <A> Maybe<A> maybe(A a) {
        return a == null ? nothing() : just(a);
    }

    @Override
    public final <B> Maybe<B> pure(B b) {
        return just(b);
    }

    @Override
    public <B> Maybe<B> map(Function<? super A, ? extends B> f) {
        if (this instanceof Maybe.Nothing<A>) {
            return nothing();
        } else {
            return map(f);
        }
    }

    @Override
    public <B> Maybe<B> fmap(Applicative<Function<? super A, ? extends B>, Maybe<?>> af) {
        if (this instanceof Maybe.Nothing<A>) {
            return nothing();
        } else {
            return fmap(af);
        }
    }

    @Override
    public <B> Maybe<B> bind(Function<? super A, ? extends Monad<B, Maybe<?>>> mf) {
        if (this instanceof Maybe.Nothing<A>) {
            return nothing();
        } else {
            return bind(mf);
        }
    }

    private static final class Nothing<A> extends Maybe<A> {
        private static final Nothing<?> INSTANCE = new Nothing<>();

        private Nothing() {
        }

        @Override
        public String toString() {
            return "Nothing";
        }
    }

    private static final class Just<A> extends Maybe<A> {
        private final A value;

        private Just(A a) {
            value = a;
        }

        @Override
        public <B> Maybe<B> map(Function<? super A, ? extends B> f) {
            return pure(f.apply(value));
        }

        @Override
        @SuppressWarnings("unchecked")
        public <B> Maybe<B> fmap(Applicative<Function<? super A, ? extends B>, Maybe<?>> af) {
            return (Maybe<B>) af.map(a -> a.apply(value));
        }

        @Override
        public <B> Maybe<B> bind(Function<? super A, ? extends Monad<B, Maybe<?>>> mf) {
            return (Maybe<B>) mf.apply(value);
        }

        @Override
        public String toString() {
            return "Just " + value;
        }
    }
}