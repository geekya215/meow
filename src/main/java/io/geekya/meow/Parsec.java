package io.geekya.meow;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Parsec<A> implements Parser<A>, Monad<A, Parsec<?>> {
    private final Function<String, Maybe<Pair<A, String>>> pf;

    private Parsec(Function<String, Maybe<Pair<A, String>>> pf) {
        this.pf = pf;
    }

    private static Parsec<Character> item() {
        return new Parsec<>(s -> s.isEmpty()
          ? Maybe.nothing()
          : Maybe.just(new Pair<>(s.charAt(0), s.substring(1))));
    }

    public static Parsec<Character> sat(Predicate<Character> p) {
        return item().bind(
          x -> p.test(x)
            ? item().result(x) : item().zero());
    }

    public static Parsec<Character> character(Character x) {
        return sat(c -> c == x);
    }

    public static Parsec<String> string(String s) {
        return s.isEmpty()
          ? item().result("")
          : character(s.charAt(0)).bind(x -> string(s.substring(1)).bind(y -> item().result(s)));
    }

    // primitive parsers
    private <B> Parsec<B> result(B b) {
        return pure(b);
    }

    private Parsec<A> zero() {
        return new Parsec<>(s -> Maybe.nothing());
    }

    // parser combinators
    public <B> Parsec<Pair<A, B>> seq(Parsec<B> b) {
        return bind(x -> b.bind(y -> result(new Pair<>(x, y))));
    }

    public Parsec<A> plus(Parsec<A> a) {
        return new Parsec<>(inp -> {
            Maybe<Pair<A, String>> m = runParser(inp);
            return m.isNothing() ? a.runParser(inp) : m;
        });
    }

    public <B> Parsec<B> discardL(Parsec<B> p) {
        return bind(a -> p.bind(b -> result(b)));
    }

    public <B> Parsec<A> discardR(Parsec<B> p) {
        return bind(a -> p.bind(b -> result(a)));
    }

    public Parsec<List<A>> many() {
        return new Parsec<>(inp -> {
            Maybe<Pair<A, String>> m = runParser(inp);
            if (m.isNothing()) {
                return Maybe.just(new Pair<>(new ArrayList<>(), inp));
            } else {
                Pair<A, String> p = m.fromJust();
                Maybe<Pair<List<A>, String>> r = many().runParser(p.snd());
                r.fromJust().fst().add(0, p.fst());
                return r;
            }
        });
    }

    public Parsec<List<A>> some() {
        return bind(a -> many().bind(b -> {
            b.add(0, a);
            return result(b);
        }));
    }

    public Parsec<List<A>> count(Integer n) {
        return n == 0
          ? result(new ArrayList<>())
          : bind(a -> count(n - 1).bind(b -> {
            b.add(0, a);
            return result(b);
        }));
    }

    public Parsec<A> between(Parsec<Character> open, Parsec<Character> close) {
        return open.discardL(this).discardR(close);
    }

    public Parsec<A> trimSpace() {
        return character(' ').many().discardL(this).discardR(character(' ').many());
    }

    public Maybe<A> parse(String inp) {
        return runParser(inp).bind(p -> Maybe.just(p.fst()));
    }

    @Override
    public Maybe<Pair<A, String>> runParser(String inp) {
        return pf.apply(inp);
    }

    @Override
    public <B> Parsec<B> pure(B b) {
        return new Parsec<>(inp -> Maybe.just(new Pair<>(b, inp)));
    }

    @Override
    public <B> Parsec<B> map(Function<? super A, ? extends B> f) {
        return bind(a -> pure(f.apply(a)));
    }

    @Override
    public <B> Parsec<B> fmap(Applicative<Function<? super A, ? extends B>, Parsec<?>> af) {
        return (Parsec<B>) af.map(f -> bind(a -> pure(f.apply(a))));
    }

    @Override
    public <B> Parsec<B> bind(Function<? super A, ? extends Monad<B, Parsec<?>>> mf) {
        return new Parsec<>(inp -> runParser(inp).bind(p -> {
            Parsec<B> a = (Parsec<B>) mf.apply(p.fst());
            return a.runParser(p.snd());
        }));
    }
}
