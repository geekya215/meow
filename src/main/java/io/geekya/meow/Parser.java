package io.geekya.meow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parser<A> implements IParser<A>, Monad<A, Parser<?>> {
    public static Parser<Character> id = new Parser<>(s -> {
        if (s.isEmpty()) {
            return Maybe.nothing();
        } else {
            return Maybe.just(new Pair<>(s.charAt(0), s.substring(1)));
        }
    });

    private final Function<String, Maybe<Pair<A, String>>> p;

    public Parser(Function<String, Maybe<Pair<A, String>>> p) {
        this.p = p;
    }

    public static Parser<Character> pred(Predicate<Character> p) {
        return id.bind(c -> p.test(c) ? id.pure(c) : id.fail());
    }

    public static Parser<Character> character(Character x) {
        return pred(c -> c == x);
    }

    @Override
    public <B> Parser<B> pure(B b) {
        return new Parser<>(s -> Maybe.just(new Pair<>(b, s)));
    }

    public Maybe<A> parse(String s) {
        return runParser(s).bind(a -> Maybe.just(a.fst()));
    }

    public Parser<A> fail() {
        return new Parser<>(s -> Maybe.nothing());
    }

    public <B, C> Parser<C> combine(Parser<B> p, BiFunction<A, B, C> f) {
        return bind(a -> p.bind(b -> pure(f.apply(a, b))));
    }

    public <B> Parser<A> skip(Parser<B> p) {
        return combine(p, (a, b) -> a);
    }

    public <B> Parser<B> use(Parser<B> p) {
        return combine(p, (a, b) -> b);
    }

    public Parser<A> or(Parser<A> p) {
        return new Parser<>(s -> {
            Maybe<Pair<A, String>> m = runParser(s);
            if (m.isNothing()) {
                return p.runParser(s);
            } else {
                return m;
            }
        });
    }

    public Parser<List<A>> many() {
        return new Parser<>(s -> {
            Maybe<Pair<A, String>> m = runParser(s);
            if (m.isNothing()) {
                return Maybe.just(new Pair<>(new ArrayList<>(), s));
            } else {
                Maybe<Pair<List<A>, String>> r = many().runParser(m.fromJust().snd());
                r.fromJust().fst().add(0, m.fromJust().fst());
                return r;
            }
        });
    }

    public Parser<List<A>> some() {
        return combine(many(), (x, xs) -> {
            xs.add(0, x);
            return xs;
        });
    }

    @Override
    public Maybe<Pair<A, String>> runParser(String s) {
        return p.apply(s);
    }

    @Override
    public <B> Parser<B> map(Function<? super A, ? extends B> f) {
        return bind(a -> pure(f.apply(a)));
    }

    @Override
    public <B> Parser<B> fmap(Applicative<Function<? super A, ? extends B>, Parser<?>> af) {
        return (Parser<B>) af.map(f -> bind(a -> pure(f.apply(a))));
    }

    @Override
    public <B> Parser<B> bind(Function<? super A, ? extends Monad<B, Parser<?>>> mf) {
        return new Parser<>(s -> runParser(s).bind(r -> {
            Parser<B> apply = (Parser<B>) mf.apply(r.fst());
            return apply.runParser(r.snd());
        }));
    }
}
